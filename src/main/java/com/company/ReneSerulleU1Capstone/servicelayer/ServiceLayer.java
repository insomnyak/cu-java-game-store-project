package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.*;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import javax.management.InvalidAttributeValueException;
import javax.naming.ServiceUnavailableException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Primary Service Layer for the Capstone Game Store.
 * This layer contains methods related to customer actions.
 */
@Controller @Primary
public class ServiceLayer {

    protected GameDao gameDao;
    protected ConsoleDao consoleDao;
    protected TShirtDao tShirtDao;
    protected ProcessingFeeDao processingFeeDao;
    protected SalesTaxRateDao salesTaxRateDao;
    protected InvoiceDao invoiceDao;

    /**
     * This bean is a map of the full state name (key) and it's corresponding 2-letter state code (value).
     * It is instantiated in the Main Class and use to allow additional values when a user submits an invoice.
     *
     * @see #validateAttributeValues(PurchaseViewModel, String, Item, SalesTaxRate, ProcessingFee)
     */
    @Autowired
    Map<String, String> states;

    @Autowired
    public ServiceLayer(GameDao gameDao, ConsoleDao consoleDao, TShirtDao tShirtDao,
                        ProcessingFeeDao processingFeeDao,
                        SalesTaxRateDao salesTaxRateDao, InvoiceDao invoiceDao) {
        this.gameDao = gameDao;
        this.consoleDao = consoleDao;
        this.tShirtDao = tShirtDao;
        this.processingFeeDao = processingFeeDao;
        this.salesTaxRateDao = salesTaxRateDao;
        this.invoiceDao = invoiceDao;
    }

    /**
     * Method checks that the item is an instance of a class that implements {@link ItemViewModel}, maps the item to an
     *      object of it's respective DTO using {@link MapProperties} ({@link #convertItemViewModelToDto(ItemViewModel)}),
     *      and then calls the helper {@link #update(Item)}
     *      method.
     * @param item: An object that implements ItemViewModel, such as ConsoleViewModel
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertItemViewModelToDto(ItemViewModel)}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #convertItemViewModelToDto(ItemViewModel)}
     *
     * @see #update(Item)
     * @see InventoryServiceLayer#update(List)
     * @see ItemViewModel
     * @see ConsoleViewModel
     * @see GameViewModel
     * @see TShirtViewModel
     */
    @Transactional
    public void update(ItemViewModel item) throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        if (item instanceof GameViewModel) {
            GameViewModel game = (GameViewModel) item;
            if (game.getGameId() == null) {
                throw new IllegalArgumentException("Invalid:gameId must not be null.");
            }
            if (gameDao.countId(game.getGameId()) == 0) {
                throw new IllegalArgumentException("Invalid:gameId does not exist in the database.");
            }
            update(convertItemViewModelToDto(game));
        } else if (item instanceof ConsoleViewModel) {
            ConsoleViewModel console = (ConsoleViewModel) item;
            if (console.getConsoleId() == null) {
                throw new IllegalArgumentException("Invalid:consoleId must not be null.");
            }
            if (consoleDao.countId(console.getConsoleId()) == 0) {
                throw new IllegalArgumentException("Invalid:consoleId does not exist in the database.");
            }
            update(convertItemViewModelToDto(console));
        } else if (item instanceof TShirtViewModel) {
            TShirtViewModel tShirt = (TShirtViewModel) item;
            if (tShirt.gettShirtId() == null) {
                throw new IllegalArgumentException("Invalid:tShirtId must not be null.");
            }
            if (tShirtDao.countId(tShirt.gettShirtId()) == 0) {
                throw new IllegalArgumentException("Invalid:tShirtId does not exist in the database.");
            }
            update(convertItemViewModelToDto(tShirt));
        } else {
            throw invalidTypeIdException(item.getClass().getTypeName());
        }
    }

    /**
     * Method accepts an {@link Item} object that is an instance of one of its inherited classes,
     *      and execute the respective DAO update method on the object.
     * @param item: object that inherits {@link Item}
     * @throws InvalidTypeIdException: Thrown when the object is not an instance of a Class that is implemented
     *      within the method. See {@link #invalidTypeIdException(String)}
     * @see ConsoleDao
     * @see GameDao
     * @see TShirtDao
     */
    @Transactional
    protected void update(Item item) throws InvalidTypeIdException {
        if (item instanceof Game) {
            gameDao.update((Game) item);
        } else if (item instanceof Console) {
            consoleDao.update((Console) item);
        } else if (item instanceof TShirt) {
            tShirtDao.update((TShirt) item);
        } else {
            throw invalidTypeIdException(item.getClass().getTypeName());
        }
    }

    /**
     * Method finds a DTO object of the given itemType and id. It calls its helper method
     *      {@link #findItem(String, Long)}. The helper method returns an object that inherits {@link Item}.
     *      This method uses {@link #convertDtoToItemViewModel(Item)} to convert the Item and return the respective
     *      object that implements ItemViewModel.
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @param id: the id (primary key) of the object to search for.
     *
     * @return returns an object that implements {@link ItemViewModel}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #convertDtoToItemViewModel(Item)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertDtoToItemViewModel(Item)}
     *
     * @see ConsoleViewModel
     * @see GameViewModel
     * @see TShirtViewModel
     */
    public ItemViewModel findItem(String itemType, Long id)
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        Item item = find(itemType, id);
        if (item == null) throw new NoSuchElementException(String.format(
                "Item of type %s with id %s not found.", itemType, id));
        return convertDtoToItemViewModel(item);
    }

    /**
     * Method finds a DTO object of the given itemType and id. It call the respective DAO find-by-id method to
     *      return an object the inherits {@link Item}.
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @param id: the id (primary key) of the object to search for.
     *
     * @return returns an object that inherits {@link Item}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     */
    public Item find(String itemType, Long id)
            throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                return gameDao.find(id);
            case ItemType.console:
                return consoleDao.find(id);
            case ItemType.tShirt:
                return tShirtDao.find(id);
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    /**
     * Returns a {@link List} of {@link ItemViewModel} of the specified itemType, which must adhere to one of the
     *      values defined in {@link ItemType}. The methods calls the respective DAO findAll() method.
     *      The DAO returns a list of objects that inherit from {@link Item}.
     *      This method then uses {@link #convertListDtoToItemViewModel(List)} to convert the list based on the object's class.
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @return list of object that implement {@link ItemViewModel}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertListDtoToItemViewModel(List)}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #convertListDtoToItemViewModel(List)}
     */
    public List<ItemViewModel> findAll(String itemType)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        List<ItemViewModel> output = new ArrayList<>();
        switch (itemType) {
            case ItemType.game: {
                List<Game> games = gameDao.findAll();
                output.addAll(convertListDtoToItemViewModel(games));
                break;
            }
            case ItemType.console: {
                List<Console> consoles = consoleDao.findAll();
                output.addAll(convertListDtoToItemViewModel(consoles));
                break;
            }
            case ItemType.tShirt: {
                List<TShirt> tShirts = tShirtDao.findAll();
                output.addAll(convertListDtoToItemViewModel(tShirts));
                break;
            }
            default:
                throw invalidTypeIdException(itemType);
        }
        return output;
    }

    /**
     *Returns a {@link List} of {@link ItemViewModel} of the specified itemType.
     *      The methods calls the respective DAO findAllBy...(value) method.
     *      The DAO returns a list of objects that inherit from {@link Item}.
     *      This method then uses {@link #convertListDtoToItemViewModel(List)} to convert the list based on the object's class.
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @param attribute: a column name variable of the respective DTO object. These attributes must be any defined in
     *                 {@link com.company.ReneSerulleU1Capstone.servicelayer.BLSettings.DTO_SEARCHABLE_ATTRIBUTES},
     *                 and must be a valid attribute of the specified itemType.
     * @param value: the value to search for
     * @return list of object that implement {@link ItemViewModel}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertListDtoToItemViewModel(List)}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #convertListDtoToItemViewModel(List)}
     * @throws IllegalArgumentException: Thrown if an invalid attribute name is provided. The error will output a
     *      list of accepted attributes for the given itemType. {@link #illegalArgumentException(String, List)}
     */
    public List<ItemViewModel> findBy(String itemType, String attribute, String value)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        List<ItemViewModel> output = new ArrayList<>();
        switch (itemType) {
            case ItemType.game:
                switch (attribute.toLowerCase().trim()) {
                    case "studio": {
                        List<Game> games = gameDao.findAllByStudio(value);
                        output.addAll(convertListDtoToItemViewModel(games));
                        break;
                    }
                    case "esrbrating":
                    case "esrb rating":
                    case "esrb_rating":
                    case "esrb-rating":
                    case "esrb+rating": {
                        List<Game> games = gameDao.findAllByEsrbRating(value);
                        output.addAll(convertListDtoToItemViewModel(games));
                        break;
                    }
                    case "title": {
                        List<Game> games = gameDao.findAllByTitle(value);
                        output.addAll(convertListDtoToItemViewModel(games));
                        break;
                    }
                    default:
                        throw illegalArgumentException(itemType, BLSettings.DTO_SEARCHABLE_ATTRIBUTES.GAME.toList());
                }
                break;
            case ItemType.console:
                switch (attribute.toLowerCase().trim()) {
                    case "manufacturer": {
                        List<Console> consoles = consoleDao.findAllByManufacturer(value);
                        output.addAll(convertListDtoToItemViewModel(consoles));
                        break;
                    }
                    default:
                        throw illegalArgumentException(itemType, BLSettings.DTO_SEARCHABLE_ATTRIBUTES.CONSOLE.toList());
                }
                break;
            case ItemType.tShirt:
                switch (attribute.toLowerCase().trim()) {
                    case "color": {
                        List<TShirt> tShirts = tShirtDao.findAllByColor(value);
                        output.addAll(convertListDtoToItemViewModel(tShirts));
                        break;
                    }
                    case "size": {
                        List<TShirt> tShirts = tShirtDao.findAllBySize(value);
                        output.addAll(convertListDtoToItemViewModel(tShirts));
                        break;
                    }
                    default:
                        throw illegalArgumentException(itemType, BLSettings.DTO_SEARCHABLE_ATTRIBUTES.T_SHIRTS.toList());
                }
                break;
            default:
                throw invalidTypeIdException(itemType);
        }
        return output;
    }

    /**
     * Method returns a {@link List} of searchable attributes (or column names) for the given itemType.
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @return list of accepted attribute names for the given itemType.
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     */
    public List<String> findSearchableAttributes(String itemType) throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                return BLSettings.DTO_SEARCHABLE_ATTRIBUTES.GAME.toList();
            case ItemType.console:
                return BLSettings.DTO_SEARCHABLE_ATTRIBUTES.CONSOLE.toList();
            case ItemType.tShirt:
                return BLSettings.DTO_SEARCHABLE_ATTRIBUTES.T_SHIRTS.toList();
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    /**
     * Method returns all tuples from a table. The method uses the className of the ViewModel to decided which
     *      table to access and DAO findAll() method to invoke.
     *      The DAO return a List of the respective DTO class.
     *      The method then uses {@link #convertListDtoToPurchaseFeeViewModel(List)} to convert the List.
     * @param className: The className of an accepted ViewModel class.
     * @return list of ViewModel objects
     * @throws InvalidClassException: Thrown if a Class that's not processable is provided. The error outputs
     *      a list of accepted classes. {@link #invalidClassException(Class, Class[])}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     *
     * @see PurchaseViewModel
     * @see SalesTaxRate
     */
    public List<?> findAll(Class className)
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        List<PurchaseFeeViewModel> output = new ArrayList<>();
        if (className.equals(ProcessingFeeViewModel.class)) {
            List<ProcessingFee> processingFees = processingFeeDao.findAll();
            output.addAll(convertListDtoToPurchaseFeeViewModel(processingFees));
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            List<SalesTaxRate> salesTaxRates = salesTaxRateDao.findAll();
            output.addAll(convertListDtoToPurchaseFeeViewModel(salesTaxRates));
        } else {
            throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
        }
        return output;
    }

    /**
     * This method is used to provide a list of {@link ProcessingFee} or {@link SalesTaxRate} as a map.
     *      It uses {@link #findAll(Class)} to get the list, and then is populates the map object.
     * @param className: a ViewModel class name.
     * @return Map with key of type String and value of type Decimal
     * @throws InvalidClassException: Thrown if a Class that's not processable is provided. The error outputs
     *      a list of accepted classes. {@link #invalidClassException(Class, Class[])}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #findAll(Class)}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws InvalidTypeIdException: {@link #invalidTypeIdException(String)}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws HttpServerErrorException: thrown if the provided className is null.
     *
     * @see ProcessingFeeViewModel
     * @see SalesTaxRate
     */
    public Map<String, BigDecimal> findAllAsMap(Class className)
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        List<?> list = findAll(className);
        if (list == null || list.isEmpty()) throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("List of type %s not found. Please contact support", className));

        Map<String, BigDecimal> map = new HashMap<>();

        if (className.equals(ProcessingFeeViewModel.class)) {
            list.stream().forEach(val -> {
                ProcessingFee pf = (ProcessingFeeViewModel) val;
                map.put(pf.getProductType(), pf.getFee());
            });
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            list.stream().forEach(val -> {
                SalesTaxRate str = (SalesTaxRateViewModel) val;
                map.put(str.getState(), str.getRate());
            });
        }

        return map;
    }

    /**
     * Method finds and lists all available products types defined in the ProcessingFee table of the data layer.
     * @return list of String comprised of the products types defined in the ProcessingFee table of the data layer.
     * @throws InvalidClassException: Thrown if a Class that's not processable is provided. The error outputs
     *      a list of accepted classes. {@link #invalidClassException(Class, Class[])}
     * @throws InstantiationException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws InvalidTypeIdException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws IllegalAccessException: {@link MapProperties}, {@link #findAll(Class)}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     */
    public List<String> findAllProductTypes()
            throws InvalidClassException, InstantiationException, InvalidTypeIdException, IllegalAccessException {
        List<String> productTypes = new ArrayList<>();
        List<?> processingFees = findAll(ProcessingFeeViewModel.class);
        if (processingFees == null || processingFees.isEmpty())
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Processing Fee not found. Please contact support");
        processingFees.forEach(pf -> productTypes.add(((ProcessingFeeViewModel) pf).getProductType()));
        return productTypes;
    }

    /**
     * Method finds an invoice with specified id and builds an object of type className.
     * @param className: a ViewModel class name.
     * @param id: the id (primary key) to search for
     * @return {@link PurchaseViewModel} a composite object of {@link Invoice} parameters and {@link Item} object
     * @throws InvalidTypeIdException: {@link MapProperties}, {@link #convertListDtoToPurchaseFeeViewModel(List)}
     * @throws InvalidClassException: Thrown if a Class that's not processable is provided. The error outputs
     *      a list of accepted classes. {@link #invalidClassException(Class, Class[])}
     */
    public PurchaseViewModel find(Class className, Long id)
            throws InvalidTypeIdException, InvalidClassException, InstantiationException, IllegalAccessException {
        if (className.equals(PurchaseViewModel.class)) {
            Invoice invoice = invoiceDao.find(id);
            if (invoice == null)
                throw new NoSuchElementException(String.format("Invoice id %d not found.", id));
            return buildHelper(invoice);
        }
        throw invalidClassException(className, PurchaseViewModel.class);
    }

    /**
     * Method un-packages a PurchaseViewModel, validates the user entered data based on the Business Logic,
     *      calculates the Invoice missing fields, and if valid saves it to the database. The method also updates
     *      the quantity of the specified {@link Item} accordingly.
     * @param pvm: {@link PurchaseViewModel} that is provided from the user in order to add a new invoice to the DB.
     * @return returns the same PurchaseViewModel object with the calculated fields and the new invoice id.
     * @throws InvalidTypeIdException: {@link MapProperties}, {@link #convertDtoToItemViewModel(Item)}
     * @throws InvalidAttributeValueException: thrown if one of the specified attribute values, or calculated
     *      properties violates the Business Logic
     * @throws ServiceUnavailableException: thrown when the user provides a stateCode value with more than two
     *      characters and the service is not available to compare this value to the list of state names.
     * @throws IllegalAccessException: {@link MapProperties}, {@link #findAll(Class)}, {@link #convertDtoToItemViewModel(Item)}
     * @throws InstantiationException: {@link MapProperties}, {@link #findAll(Class)}, {@link #convertDtoToItemViewModel(Item)}
     * @throws NoSuchElementException
     *
     * Helper Methods
     * @see #setCalculatedAttributes(PurchaseViewModel, Item, SalesTaxRate, ProcessingFee)
     * @see #validateAttributeValues(PurchaseViewModel, String, Item, SalesTaxRate, ProcessingFee)
     * @see BLSettings
     */
    @Transactional
    public PurchaseViewModel add(PurchaseViewModel pvm)
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException,
            IllegalAccessException, InstantiationException {
        String itemType = pvm.getItemType();
        if (!itemType.matches(ItemType.patternCaseSensitive)) {
            throw new InvalidTypeIdException(null, String.format("Invalid itemType. Must adhere to %s",
                    ItemType.patternCaseSensitive), null, "itemType");
        }
        Item item = find(itemType, pvm.getItemId());

        if (item == null) throw new NoSuchElementException(
                String.format("%s id %d was not found", itemType, pvm.getItemId()));

        SalesTaxRate salesTaxRate = new SalesTaxRate();
        ProcessingFee processingFee = new ProcessingFee();

        validateAttributeValues(pvm, itemType, item, salesTaxRate, processingFee);

        // BL: Order must update item.quantity based on purchase.quantity
        item.setQuantity(item.getQuantity() - pvm.getQuantity());
        update(item);

        // Set calculated fields in pvm
        setCalculatedAttributes(pvm, item, salesTaxRate, processingFee);

        Invoice invoice = new Invoice() {{
            setName(pvm.getName());
            setStreet(pvm.getStreet());
            setCity(pvm.getCity());
            setState(pvm.getState());
            setZipcode(pvm.getZipcode());
            setItemId(pvm.getItemId());
            setItemType(pvm.getItemType());
            setUnitPrice(pvm.getUnitPrice());
            setQuantity(pvm.getQuantity());
            setSubtotal(pvm.getSubtotal());
            setTax(pvm.getTax());
            setProcessingFee(pvm.getProcessingFee());
            setTotal(pvm.getTotal());
        }};

        invoice = invoiceDao.add(invoice);

        pvm.setInvoiceId(invoice.getInvoiceId());
        pvm.setItem(convertDtoToItemViewModel(item));

        return pvm;
    }

    /**
     * This is a helper method that sets the calculated fields of {@link Invoice}:
     *      unitPrice, subTotal, tax, fee, and total.
     *      Method also checks that the Total purchase amount is not greater than the max allowable amount by the DB.
     * @param pvm: {@link PurchaseViewModel} object with parameters to be validated and set
     * @param item: the {@link Item} object respective to the itemId specified in the pvm
     * @param salesTaxRate: the {@link SalesTaxRate} object for the specified state in the pvm
     * @param processingFee: the {@link ProcessingFee} object for the specified itemType (productType) in the
     * @throws InvalidAttributeValueException: thrown if one of the specified attribute values, or calculated
     *      properties violates the Business Logic.
     *
     * @see BLSettings
     */
    protected void setCalculatedAttributes(PurchaseViewModel pvm, Item item, SalesTaxRate salesTaxRate,
                                        ProcessingFee processingFee) throws InvalidAttributeValueException {
        BigDecimal unitPrice = item.getPrice();
        Long quantity = pvm.getQuantity();
        BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
        BigDecimal tax = subtotal.multiply(salesTaxRate.getRate()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal fee = quantity <= BLSettings.ADDITIONAL_FEES.MAX_QTY_BEFORE_EXTRA_PROCESSING_FEE.toLong() ?
                processingFee.getFee() :
                processingFee.getFee().add(
                        BLSettings.ADDITIONAL_FEES.PROCESSING_FEE_QTY_GREATER_THAN_10.toBigDecimal());
        BigDecimal total = subtotal.add(tax).add(fee);

        if (total.compareTo(BLSettings.MAX_PURCHASE_TOTAL.toBigDecimal()) > 0) {
            throw new InvalidAttributeValueException(String.format("Your total cannot exceed %s. " +
                    "Please reduce your order quantity.", BLSettings.MAX_PURCHASE_TOTAL.toBigDecimal()));
        }

        pvm.setUnitPrice(unitPrice);
        pvm.setSubtotal(subtotal);
        pvm.setTax(tax);
        pvm.setProcessingFee(fee);
        pvm.setTotal(total);
    }

    /**
     * This helper method validates the {@link PurchaseViewModel} object parameters to ensure these comply with
     *      the Business Logic. Checks:
     *         1) Order quantity is greater than 0
     *         2) Order quantity is less than or equal to the Item's remaining quantity
     *         3) Order must contain a valid state code. This Service Layer allows for the user to pass the full
     *              state name, which it then maps to the stateCode using a bean loaded from a JSON file. If the
     *              JSON file fails to load, the logic reverts to the base requirement.
     * @param pvm: {@link PurchaseViewModel} object with parameters to be validated and set
     * @param itemType: user entered value that must match the productType in the available
     *                ProcessingFees. Must match the pattern {@link ItemType#patternCaseSensitive}.
     * @param item: the {@link Item} object respective to the itemId specified in the pvm
     * @param salesTaxRate: instantiated {@link SalesTaxRate} object whose values will be set as validation rules are
     *                   passed
     * @param processingFee: instantiated {@link ProcessingFee} object whose values will be set as validation rules
     *                     are passed
     * @throws ServiceUnavailableException: Thrown if the user provided a stateCode greater than 2 characters and
     *      the JSON state name to code isn't available.
     * @throws IllegalArgumentException: Thrown is business logic (#1 & #2) fails.
     * @throws NoSuchElementException: thrown is state name, or stateCode is not found
     * @throws HttpServerErrorException: by this method, the itemType is validated. This error is thrown if the
     *      ProcessingFee DAO doesn't return an object for itemType.
     */
    protected void validateAttributeValues(PurchaseViewModel pvm, String itemType, Item item,
                                        SalesTaxRate salesTaxRate, ProcessingFee processingFee)
            throws ServiceUnavailableException {
        // BL: Order quantity must be > 0
        if (pvm.getQuantity() <= 0)
            throw new IllegalArgumentException(
                    String.format("Your quantity for %s id %d must be " +
                                    (item.getQuantity() > 1 ? " between 1 and %d." : "at least 1."),
                            itemType, pvm.getItemId(), item.getQuantity()));

        // BL: Order quantity must be <= item.quantity
        if (pvm.getQuantity() > item.getQuantity())
            throw new IllegalArgumentException(
                    String.format("Your quantity is to high for %s id %d. Max quantity for this item is %d",
                            itemType, pvm.getItemId(), item.getQuantity()));

        // BL: Order must contain a valid state code
        String state = pvm.getState().trim()
                .replaceAll("[-+_]", " ").replace(".", "");
        String stateCode = "";
        if (state.length() > 2) {
            if (states == null || states.isEmpty()) {
                throw new ServiceUnavailableException("State value not valid. Please provide " +
                        "the state code (a 2-letter abbreviation). " +
                        "NOTE: please use the /salesTaxRate service to get the list of accepted state codes.");
            }
            stateCode = states.get(state.toLowerCase());
            if (stateCode == null || stateCode.isEmpty()) {
                throw new NoSuchElementException("State value not valid. Please provide either " +
                        "1) the state code (a 2-letter abbreviation), or " +
                        "2) the full state name. "+
                        "NOTE: please use the /salesTaxRate service to get the list of accepted state codes.");
            }
            pvm.setState(stateCode);
        } else if (state.length() == 2) {
            stateCode = state;
        }
        SalesTaxRate str = salesTaxRateDao.find(stateCode);
        if (stateCode.isEmpty() || str == null) {
            throw new NoSuchElementException("State Code not found. " +
                    "NOTE: please use the /salesTaxRate service to get the list of accepted state codes.");
        }

        salesTaxRate.setState(str.getState());
        salesTaxRate.setRate(str.getRate());

        ProcessingFee pf = null;
        switch (itemType) {
            case ItemType.game:
                pf = processingFeeDao.find(ItemType.game);
                break;
            case ItemType.console:
                pf = processingFeeDao.find(ItemType.console);
                break;
            case ItemType.tShirt:
                pf = processingFeeDao.find(ItemType.tShirt);
                break;
        }
        if (pf == null) throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("Processing Fee not found. Please contact support: " +
                        "{%s not found in Processing Fee Table}.", itemType));

        processingFee.setProductType(pf.getProductType());
        processingFee.setFee(pf.getFee());
    }

    /**
     * Helper method to build a PurchaseViewModel when an invoice is found
     * @param invoice
     * @return
     * @throws InvalidTypeIdException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    protected PurchaseViewModel buildHelper(Invoice invoice)
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        PurchaseViewModel pvm = new PurchaseViewModel();
        pvm.setInvoiceId(invoice.getInvoiceId());
        pvm.setName(invoice.getName());
        pvm.setStreet(invoice.getStreet());
        pvm.setCity(invoice.getCity());
        pvm.setState(invoice.getState());
        pvm.setZipcode(invoice.getZipcode());
        pvm.setItemType(invoice.getItemType());
        pvm.setItemId(invoice.getItemId());
        pvm.setUnitPrice(invoice.getUnitPrice());
        pvm.setQuantity(invoice.getQuantity());
        pvm.setSubtotal(invoice.getSubtotal());
        pvm.setTax(invoice.getTax());
        pvm.setProcessingFee(invoice.getProcessingFee());
        pvm.setTotal(invoice.getTotal());

        switch (invoice.getItemType()) {
            case ItemType.game:
                pvm.setItem(convertDtoToItemViewModel(gameDao.find(invoice.getItemId())));
                break;
            case ItemType.console:
                pvm.setItem(convertDtoToItemViewModel(consoleDao.find(invoice.getItemId())));
                break;
            case ItemType.tShirt:
                pvm.setItem(convertDtoToItemViewModel(tShirtDao.find(invoice.getItemId())));
                break;
            default:
                throw invalidTypeIdException(invoice.getItemType());
        }

        return pvm;
    }

    /**
     * Method converts a list of DTO objects to a list of ItemViewModel objects using
     * @param list
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvalidTypeIdException
     *
     * @see MapProperties
     * @see #convertDtoToItemViewModel(Item)
     */
    protected List<ItemViewModel> convertListDtoToItemViewModel(List<?> list)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        List<ItemViewModel> output = new ArrayList<>();
        if (list.isEmpty()) return output;

        for (Object o : list) {
            output.add(convertDtoToItemViewModel((Item) o));
        }
        return output;
    }

    /**
     * Method converts an Item DTO object (i.e. Game, Console, TShirt) to its respective ItemViewModel.
     * @param item: object of type {@link Item}
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvalidTypeIdException
     *
     * @see MapProperties
     */
    protected ItemViewModel convertDtoToItemViewModel(Item item)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        Class className = item.getClass().getSimpleName().isEmpty() ? item.getClass().getSuperclass() : item.getClass();
        if (className == Game.class) {
            MapProperties<Game, GameViewModel> map = new MapProperties<>((Game) item, GameViewModel.class);
            return map.mapFirstToSecond(false);
        } else if (className == Console.class) {
            MapProperties<Console, ConsoleViewModel> map = new MapProperties<>((Console) item,
                    ConsoleViewModel.class);
            return map.mapFirstToSecond(false);
        } else if (className == TShirt.class) {
            MapProperties<TShirt, TShirtViewModel> map = new MapProperties<>((TShirt) item,
                    TShirtViewModel.class);
            return map.mapFirstToSecond(false);
        } else {
            throw invalidTypeIdException(className.getTypeName());
        }
    }

    /**
     * Method converts an {@link ItemViewModel} object to its respective {@link Item} DTO
     *      object (i.e. Game, Console, TShirt)
     * @param item: object of type {@link ItemViewModel}
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvalidTypeIdException
     *
     * @see MapProperties
     */
    protected Item convertItemViewModelToDto(ItemViewModel item)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        Class className = item.getClass();
        if (className == GameViewModel.class) {
            MapProperties<GameViewModel, Game> map = new MapProperties<>((GameViewModel) item, Game.class);
            return map.mapFirstToSecond(false);
        } else if (className == ConsoleViewModel.class) {
            MapProperties<ConsoleViewModel, Console> map = new MapProperties<>((ConsoleViewModel) item,
                    Console.class);
            return map.mapFirstToSecond(false);
        } else if (className == TShirtViewModel.class) {
            MapProperties<TShirtViewModel, TShirt> map = new MapProperties<>((TShirtViewModel) item,
                    TShirt.class);
            return map.mapFirstToSecond(false);
        } else {
            throw invalidTypeIdException(className.getTypeName());
        }
    }

    /**
     * Method converts a list of Fee related DTO objects to the respective
     *      list of {@link PurchaseFeeViewModel} ViewModel Objects.
     * @param list: list of fee related object (i.e. {@link ProcessingFee}, {@link SalesTaxRate})
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvalidTypeIdException
     *
     * @see MapProperties
     */
    protected List<PurchaseFeeViewModel> convertListDtoToPurchaseFeeViewModel(List<?> list)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        List<PurchaseFeeViewModel> output = new ArrayList<>();
        if (list.isEmpty()) return output;

        for (Object o : list) {
            output.add(convertDtoToPurchaseFeeViewModel(o));
        }
        return output;
    }

    /**
     * Method converts a fee related DTO object to the respective {@link PurchaseFeeViewModel} ViewModel object.
     * @param o: fee related object (i.e. {@link ProcessingFee}, {@link SalesTaxRate})
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvalidTypeIdException
     *
     * @see MapProperties
     */
    protected PurchaseFeeViewModel convertDtoToPurchaseFeeViewModel(Object o)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        Class className = o.getClass().getSimpleName().isEmpty() ? o.getClass().getSuperclass() : o.getClass();
        if (className == ProcessingFee.class) {
            MapProperties<ProcessingFee, ProcessingFeeViewModel> map =
                    new MapProperties<>((ProcessingFee) o, ProcessingFeeViewModel.class);
            return map.mapFirstToSecond(false);
        } else if (className == SalesTaxRate.class) {
            MapProperties<SalesTaxRate, SalesTaxRateViewModel> map =
                    new MapProperties<>((SalesTaxRate) o, SalesTaxRateViewModel.class);
            return map.mapFirstToSecond(false);
        } else {
            throw invalidTypeIdException(className.getTypeName());
        }
    }

    protected void matchObjectToClass(Class className, Object obj) {
        if (!obj.getClass().equals(className))
            throw new IllegalArgumentException(String.format("Object must be an instance of %s.",
                    className));
    }

    protected InvalidTypeIdException invalidTypeIdException(String itemType) {
        return new InvalidTypeIdException(null, "Invalid.type: check $type value. " +
                "Must adhere to the regex: " + ItemType.patternCaseSensitive,
                null, itemType);
    }

    protected InvalidClassException invalidClassException(Class className, Class... supportedClasses) {
        String classes = "";
        if (supportedClasses != null && supportedClasses.length > 0) {
            classes += supportedClasses[0];
            for (int i = 1; i < supportedClasses.length; i++) {
                classes += " or " + supportedClasses[i];
            }
        }
        return new InvalidClassException(String.format(
                "%s is not supported. %s", className,
                classes.length() > 0 ? "Please use " + classes + "." : "No support classes provided."));
    }

    protected IllegalArgumentException illegalArgumentException(String itemType, List<String> acceptedValues) {
        StringBuilder values = new StringBuilder();
        int len = acceptedValues.size();
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                values.append(acceptedValues.get(i));
            } else {
                values.append(", ").append(acceptedValues.get(i));
            }
        }
        return new IllegalArgumentException(
                String.format("Invalid values for %s. Please select from: %s", itemType, values.toString()));
    }

}
