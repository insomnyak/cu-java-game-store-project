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

@Controller @Primary
public class ServiceLayer {

    protected GameDao gameDao;
    protected ConsoleDao consoleDao;
    protected TShirtDao tShirtDao;
    protected ProcessingFeeDao processingFeeDao;
    protected SalesTaxRateDao salesTaxRateDao;
    protected InvoiceDao invoiceDao;

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
            update(convertItemVmToDto(game));
        } else if (item instanceof ConsoleViewModel) {
            ConsoleViewModel console = (ConsoleViewModel) item;
            if (console.getConsoleId() == null) {
                throw new IllegalArgumentException("Invalid:consoleId must not be null.");
            }
            if (consoleDao.countId(console.getConsoleId()) == 0) {
                throw new IllegalArgumentException("Invalid:consoleId does not exist in the database.");
            }
            update(convertItemVmToDto(console));
        } else if (item instanceof TShirtViewModel) {
            TShirtViewModel tShirt = (TShirtViewModel) item;
            if (tShirt.gettShirtId() == null) {
                throw new IllegalArgumentException("Invalid:tShirtId must not be null.");
            }
            if (tShirtDao.countId(tShirt.gettShirtId()) == 0) {
                throw new IllegalArgumentException("Invalid:tShirtId does not exist in the database.");
            }
            update(convertItemVmToDto(tShirt));
        } else {
            throw invalidTypeIdException(item.getClass().getTypeName());
        }
    }

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

    public ItemViewModel findItem(String itemType, Long id)
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        Item item = find(itemType, id);
        if (item == null) throw new NoSuchElementException(String.format(
                "Item of type %s with id %s not found.", itemType, id));
        return convertDtoToItemVM(item);
    }

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

    public List<ItemViewModel> findAll(String itemType)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        List<ItemViewModel> output = new ArrayList<>();
        switch (itemType) {
            case ItemType.game: {
                List<Game> games = gameDao.findAll();
                output.addAll(convertListDtoToItemVM(games));
                break;
            }
            case ItemType.console: {
                List<Console> consoles = consoleDao.findAll();
                output.addAll(convertListDtoToItemVM(consoles));
                break;
            }
            case ItemType.tShirt: {
                List<TShirt> tShirts = tShirtDao.findAll();
                output.addAll(convertListDtoToItemVM(tShirts));
                break;
            }
            default:
                throw invalidTypeIdException(itemType);
        }
        return output;
    }

    public List<ItemViewModel> findBy(String itemType, String attribute, String value)
            throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        List<ItemViewModel> output = new ArrayList<>();
        switch (itemType) {
            case ItemType.game:
                switch (attribute.toLowerCase().trim()) {
                    case "studio": {
                        List<Game> games = gameDao.findAllByStudio(value);
                        output.addAll(convertListDtoToItemVM(games));
                        break;
                    }
                    case "esrbrating":
                    case "esrb rating":
                    case "esrb_rating":
                    case "esrb-rating":
                    case "esrb+rating": {
                        List<Game> games = gameDao.findAllByEsrbRating(value);
                        output.addAll(convertListDtoToItemVM(games));
                        break;
                    }
                    case "title": {
                        List<Game> games = gameDao.findAllByTitle(value);
                        output.addAll(convertListDtoToItemVM(games));
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
                        output.addAll(convertListDtoToItemVM(consoles));
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
                        output.addAll(convertListDtoToItemVM(tShirts));
                        break;
                    }
                    case "size": {
                        List<TShirt> tShirts = tShirtDao.findAllBySize(value);
                        output.addAll(convertListDtoToItemVM(tShirts));
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

    public List<?> findAll(Class className)
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        List<PurchaseFee> output = new ArrayList<>();
        if (className.equals(ProcessingFeeViewModel.class)) {
            List<ProcessingFee> processingFees = processingFeeDao.findAll();
            output.addAll(convertListDtoToPurchaseFee(processingFees));
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            List<SalesTaxRate> salesTaxRates = salesTaxRateDao.findAll();
            output.addAll(convertListDtoToPurchaseFee(salesTaxRates));
        } else {
            throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
        }
        return output;
    }

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

    public PurchaseViewModel find(Class className, Long id) throws InvalidTypeIdException, InvalidClassException {
        if (className.equals(PurchaseViewModel.class)) {
            Invoice invoice = invoiceDao.find(id);
            if (invoice == null)
                throw new NoSuchElementException(String.format("Invoice id %d not found.", id));
            return buildHelper(invoice);
        }
        throw invalidClassException(className, PurchaseViewModel.class);
    }

    @Transactional
    public PurchaseViewModel add(PurchaseViewModel pvm)
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException,
            IllegalAccessException, InstantiationException {
        String itemType = pvm.getItemType();
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
        pvm.setItem(item);

        return pvm;
    }

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

    protected PurchaseViewModel buildHelper(Invoice invoice) throws InvalidTypeIdException {
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
                pvm.setItem(gameDao.find(invoice.getItemId()));
                break;
            case ItemType.console:
                pvm.setItem(consoleDao.find(invoice.getItemId()));
                break;
            case ItemType.tShirt:
                pvm.setItem(tShirtDao.find(invoice.getItemId()));
                break;
            default:
                throw invalidTypeIdException(invoice.getItemType());
        }

        return pvm;
    }

    protected List<ItemViewModel> convertListDtoToItemVM(List<?> list)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        List<ItemViewModel> output = new ArrayList<>();
        if (list.isEmpty()) return output;

        for (Object o : list) {
            output.add(convertDtoToItemVM((Item) o));
        }
        return output;
    }

    protected ItemViewModel convertDtoToItemVM(Item item)
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

    protected Item convertItemVmToDto(ItemViewModel item)
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

    protected List<PurchaseFee> convertListDtoToPurchaseFee(List<?> list)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        List<PurchaseFee> output = new ArrayList<>();
        if (list.isEmpty()) return output;

        for (Object o : list) {
            output.add(convertDtoToPurchaseFee(o));
        }
        return output;
    }

    protected PurchaseFee convertDtoToPurchaseFee(Object o)
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
