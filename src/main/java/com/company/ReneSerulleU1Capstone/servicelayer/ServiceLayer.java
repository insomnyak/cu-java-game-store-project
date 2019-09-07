package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import javax.management.InvalidAttributeValueException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    public void update(Item item) throws InvalidTypeIdException {
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

    public Item find(String itemType, Long id) throws InvalidTypeIdException {
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

    public List<? extends  Item> findAll(String itemType) throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                return gameDao.findAll();
            case ItemType.console:
                return consoleDao.findAll();
            case ItemType.tShirt:
                return tShirtDao.findAll();
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    public List<? extends Item> findBy(String itemType, String attribute, String value)
            throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                switch (attribute.toLowerCase().trim()) {
                    case "studio":
                        return gameDao.findAllByStudio(value);
                    case "esrbrating":
                    case "esrb rating":
                    case "esrb_rating":
                    case "esrb-rating":
                    case "esrb+rating":
                        return gameDao.findAllByEsrbRating(value);
                    case "title":
                        return gameDao.findAllByTitle(value);
                    default:
                        throw illegalArgumentException(itemType, DtoSearchableAttributes.GAME.getList());
                }
            case ItemType.console:
                switch (attribute.toLowerCase().trim()) {
                    case "manufacturer":
                        return consoleDao.findAllByManufacturer(value);
                    default:
                        throw illegalArgumentException(itemType, DtoSearchableAttributes.CONSOLE.getList());
                }
            case ItemType.tShirt:
                switch (attribute.toLowerCase().trim()) {
                    case "color":
                        return tShirtDao.findAllByColor(value);
                    case "size":
                        return tShirtDao.findAllBySize(value);
                    default:
                        throw illegalArgumentException(itemType, DtoSearchableAttributes.T_SHIRTS.getList());
                }
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    public List<String> findSearchableAttributes(String itemType) throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                return DtoSearchableAttributes.GAME.getList();
            case ItemType.console:
                return DtoSearchableAttributes.CONSOLE.getList();
            case ItemType.tShirt:
                return DtoSearchableAttributes.T_SHIRTS.getList();
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    public List<?> findAll(Class className) throws InvalidClassException {
        if (className.equals(ProcessingFeeViewModel.class)) {
            return processingFeeDao.findAll();
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            return salesTaxRateDao.findAll();
        }
        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    public List<String> findAllProductTypes() throws InvalidClassException {
        List<String> productTypes = new ArrayList<>();
        List<?> processingFees = findAll(ProcessingFeeViewModel.class);
        if (processingFees == null || processingFees.isEmpty())
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Processing Fee not found. Please contact support");
        processingFees.forEach(pf -> productTypes.add(((ProcessingFee) pf).getProductType()));
        return productTypes;
    }

    @Transactional
    public Object find(Class className, Long id) throws InvalidTypeIdException, InvalidClassException {
        if (className.equals(PurchaseViewModel.class)) {
            Invoice invoice = invoiceDao.find(id);
            if (invoice == null)
                throw new NoSuchElementException(String.format("Invoice id %d", id));
            return buildHelper(invoice);
        }
        throw invalidClassException(className, PurchaseViewModel.class);
    }

    @Transactional
    public PurchaseViewModel add(PurchaseViewModel pvm) throws InvalidTypeIdException, InvalidAttributeValueException {
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

    public void setCalculatedAttributes(PurchaseViewModel pvm, Item item, SalesTaxRate salesTaxRate,
                                        ProcessingFee processingFee) throws InvalidAttributeValueException {
        BigDecimal unitPrice = item.getPrice();
        Long quantity = pvm.getQuantity();
        BigDecimal subtotal = unitPrice.multiply(new BigDecimal(quantity));
        BigDecimal tax = subtotal.multiply(salesTaxRate.getRate()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fee = quantity <= BLSettings.MAX_QTY_BEFORE_EXTRA_FEE.getLong() ? processingFee.getFee() :
                processingFee.getFee().add(
                        BLSettings.FEE_QTY_GREATER_THAN_10.getValue());
        BigDecimal total = subtotal.add(tax).add(fee);

        if (total.compareTo(BLSettings.MAX_PURCHASE_TOTAL.getValue()) > 0) {
            throw new InvalidAttributeValueException(String.format("Your total cannot exceed %s. " +
                    "Please reduce your order quantity.", BLSettings.MAX_PURCHASE_TOTAL.getValue()));
        }

        pvm.setUnitPrice(unitPrice);
        pvm.setSubtotal(subtotal);
        pvm.setTax(tax);
        pvm.setProcessingFee(fee);
        pvm.setTotal(total);
    }

    public void validateAttributeValues(PurchaseViewModel pvm, String itemType, Item item,
                                        SalesTaxRate salesTaxRate, ProcessingFee processingFee) {
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
        String state = pvm.getState().trim();
        String stateCode = "";
        if (state.length() > 2) {
            if (states == null || states.isEmpty()) {
                throw new IllegalArgumentException("State value not valid. Please provide " +
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

    public PurchaseViewModel buildHelper(Invoice invoice) throws InvalidTypeIdException {
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

    public void matchObjectToClass(Class className, Object obj) {
        if (!obj.getClass().equals(className))
            throw new IllegalArgumentException(String.format("Object must be an instance of %s.",
                    className));
    }

    public InvalidTypeIdException invalidTypeIdException(String itemType) {
        return new InvalidTypeIdException(null, "Invalid.type: check $type value. " +
                "Must adhere to the regex: " + ItemType.patternCaseSensitive,
                null, itemType);
    }

    public InvalidClassException invalidClassException(Class className, Class... supportedClasses) {
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

    public IllegalArgumentException illegalArgumentException(String itemType, List<String> acceptedValues) {
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
