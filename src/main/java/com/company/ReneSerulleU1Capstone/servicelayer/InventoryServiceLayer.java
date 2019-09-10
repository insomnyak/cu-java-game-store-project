package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.*;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service layer inherits from the Primary Service Layer. It contains additional methods related to
 * the store management.
 */
@Controller
public class InventoryServiceLayer extends ServiceLayer {

    @Autowired
    public InventoryServiceLayer(GameDao gameDao,
                                 ConsoleDao consoleDao,
                                 TShirtDao tShirtDao,
                                 ProcessingFeeDao processingFeeDao,
                                 SalesTaxRateDao salesTaxRateDao,
                                 InvoiceDao invoiceDao) {
        super(gameDao, consoleDao, tShirtDao, processingFeeDao, salesTaxRateDao, invoiceDao);
    }

    /**
     * Methods adds a list of object that implement {@link ItemViewModel} to the database.
     * @param items
     * @return return the list of newly added objects to the database along with the respective new ids.
     * @throws InvalidTypeIdException
     * @throws IllegalAccessException
     * @throws InstantiationException
     *
     * @see #add(ItemViewModel): helper method
     */
    @Transactional
    public List<ItemViewModel> add(List<ItemViewModel> items)
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        List<ItemViewModel> addedItems = new ArrayList<>();
        for (ItemViewModel item : items) {
            addedItems.add(add(item));
        }
        return addedItems;
    }

    @Transactional
    public ItemViewModel add(ItemViewModel item) throws InvalidTypeIdException, InstantiationException, IllegalAccessException {
        if (item instanceof GameViewModel) {
            GameViewModel gameVM = (GameViewModel) item;
            Game newGame =  gameDao.add((Game) convertItemViewModelToDto(item));
            gameVM.setGameId(newGame.getGameId());
            return gameVM;
        } else if (item instanceof Console) {
            ConsoleViewModel consoleVM = (ConsoleViewModel) item;
            Console newConsole = consoleDao.add((Console) convertItemViewModelToDto(item));
            consoleVM.setConsoleId(newConsole.getConsoleId());
            return consoleVM;
        } else if (item instanceof TShirt) {
            TShirtViewModel tshirtVM = (TShirtViewModel) item;
            TShirt newTShirt = tShirtDao.add((TShirt) convertItemViewModelToDto(item));
            tshirtVM.settShirtId(newTShirt.gettShirtId());
            return tshirtVM;
        }
        throw invalidTypeIdException(item.getClass().getTypeName());
    }

    /**
     * Updates multiple object that implement {@link ItemViewModel}.
     * @param items
     * @throws InvalidTypeIdException
     * @throws IllegalAccessException
     * @throws InstantiationException
     *
     * @see #update(Item): helper method
     */
    @Transactional
    public void update(List<ItemViewModel> items) throws InvalidTypeIdException, IllegalAccessException,
            InstantiationException {
        for (ItemViewModel item : items) {
            update(item);
        }
    }

    @Transactional
    public void delete(String itemType, Long id) throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                gameDao.delete(id);
                break;
            case ItemType.console:
                consoleDao.delete(id);
                break;
            case ItemType.tShirt:
                tShirtDao.delete(id);
                break;
            default:
                throw invalidTypeIdException(itemType);
        }
    }

    /**
     * Returns a an object that implements {@link PurchaseFeeViewModel}.
     * @param pathFeeType: the type of fee: i.e. processingFee, salesTaxRate
     * @param lookupValue: the value to find
     * @return
     * @throws IllegalAccessException
     * @throws InvalidClassException
     * @throws InstantiationException
     *
     * @see #find(Class, String): helper method that connects to data layer
     */
    public PurchaseFeeViewModel findFeeType(String pathFeeType, String lookupValue)
            throws IllegalAccessException, InvalidClassException, InstantiationException {
        for (Map.Entry<String, Class> feeType : FeeType.paths.entrySet()) {
            if (pathFeeType.equals(feeType.getKey())) {
                return find(feeType.getValue(), lookupValue);
            }
        }
        throw new IllegalArgumentException(String.format("Invalid feeType. Must be one of: %s",
                FeeType.paths.keySet().toString()));
    }


    public PurchaseFeeViewModel find(Class className, String value)
            throws InvalidClassException, IllegalAccessException, InstantiationException {
        if (className.equals(ProcessingFeeViewModel.class)) {
            ProcessingFee pf = processingFeeDao.find(value);
            if (pf == null) return null;
            MapProperties<ProcessingFee, ProcessingFeeViewModel> map =
                    new MapProperties<>(pf, ProcessingFeeViewModel.class);
            return map.mapFirstToSecond(false);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            SalesTaxRate str = salesTaxRateDao.find(value);
            if (str == null) return null;
            MapProperties<SalesTaxRate, SalesTaxRateViewModel> map =
                    new MapProperties<>(str, SalesTaxRateViewModel.class);
            return map.mapFirstToSecond(false);
        }
        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    public List<?> findAllFeeType(String pathFeeType)
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        for (Map.Entry<String, Class> feeType : FeeType.paths.entrySet()) {
            if (pathFeeType.equals(feeType.getKey())) {
                return findAll(feeType.getValue());
            }
        }
        throw new IllegalArgumentException(String.format("Invalid feeType. Must be one of: %s",
                FeeType.paths.keySet().toString()));
    }

    @Override
    public List<?> findAll(Class className) throws InvalidClassException, IllegalAccessException,
            InstantiationException, InvalidTypeIdException {
        try {
            return super.findAll(className);
        } catch (InvalidClassException ignore) {}

        if (className.equals(PurchaseViewModel.class)) {
            List<PurchaseViewModel> pvms = new ArrayList<>();
            List<Invoice> invoices = invoiceDao.findAll();
            if (invoices == null || invoices.isEmpty()) return null;
            invoices.forEach(invoice -> {
                try {
                    pvms.add(buildHelper(invoice));
                } catch (InvalidTypeIdException | IllegalAccessException | InstantiationException ignore) {}
            });
            return pvms;
        }
        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class,
                PurchaseViewModel.class);
    }

    /**
     * Adds fees that implement {@link PurchaseFeeViewModel} to the database. If any exist, an update is
     *      performed instead.
     * @param purchaseFees
     * @return
     * @throws InvalidClassException
     *
     * @see #add(Class, Object): helper method that executes the DAO statements
     */
    @Transactional
    public List<?> add(ArrayList<PurchaseFeeViewModel> purchaseFees) throws InvalidClassException {
        List<Object> purchaseFees1 = new ArrayList<>();
        for (PurchaseFeeViewModel purchaseFee : purchaseFees) {
            purchaseFees1.add(add(purchaseFee.getClass(), purchaseFee));
        }
        return purchaseFees1;
    }

    @Transactional
    public Object add(Class className, Object obj) throws InvalidClassException {
        matchObjectToClass(className, obj);

        if (className.equals(ProcessingFeeViewModel.class)) {
            ProcessingFee pf = (ProcessingFee) obj;
            // check if productType exists. If it exists, trigger an update instead
            ProcessingFee pf2 = processingFeeDao.find(pf.getProductType());
            if (pf2 == null) {
                return processingFeeDao.add(pf);
            } else {
                processingFeeDao.update(pf, pf.getProductType());
                return pf;
            }
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            SalesTaxRate str = (SalesTaxRate) obj;
            // check if state exists. If it exists, trigger an update instead
            SalesTaxRate str2 = salesTaxRateDao.find(str.getState());
            if (str2 == null) {
                return salesTaxRateDao.add(str);
            } else {
                salesTaxRateDao.update(str, str.getState());
                return str;
            }
        }

        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    /**
     * Method updates a fee. The key is the original value to find in the Database. the value is the object with
     *      the update parameters that will be used to set the respective tuple in the DB.
     * @param purchaseFeeMap
     * @throws InvalidClassException
     *
     * @see #update(Class, Object, String): helper method that executes the DAO statements.
     */
    @Transactional
    public void update(Map<String, PurchaseFeeViewModel> purchaseFeeMap) throws InvalidClassException {
        for (Map.Entry<String, PurchaseFeeViewModel> entry : purchaseFeeMap.entrySet()) {
            update(entry.getValue().getClass(), entry.getValue(), entry.getKey());
        }
    }

    @Transactional
    public void update(Class className, Object obj, String lookupValue) throws InvalidClassException {
        matchObjectToClass(className, obj);

        if (className.equals(ProcessingFeeViewModel.class)) {
            processingFeeDao.update((ProcessingFee) obj, lookupValue);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            salesTaxRateDao.update((SalesTaxRate) obj, lookupValue);
        } else {
            throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
        }
    }

    /**
     * Deletes a fee of specificed pathFeeType and lookupValue.
     * @param pathFeeType
     * @param lookupValue
     * @throws InvalidClassException
     *
     * @see FeeType#paths: the pathFeeType must be defined in this Class variable.
     * @see #delete(Class, String): helper method that executes DAO statements.
     */
    @Transactional
    public void deleteFeeType(String pathFeeType, String lookupValue) throws InvalidClassException {
        boolean isValidFeeType = false;
        for (Map.Entry<String, Class> feeType : FeeType.paths.entrySet()) {
            if (pathFeeType.equals(feeType.getKey())) {
                isValidFeeType = true;
                delete(feeType.getValue(), lookupValue);
            }
        }
        if (!isValidFeeType) {
            throw new IllegalArgumentException(String.format("Invalid feeType. Must be one of: %s",
                    FeeType.paths.keySet().toString()));
        }
    }

    @Transactional
    public void delete(Class className, String value) throws InvalidClassException {
        if (className.equals(ProcessingFeeViewModel.class)) {
            processingFeeDao.delete(value);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            salesTaxRateDao.delete(value);
        }   else {
            throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
        }
    }

    protected List<Item> convertListItemVMtoDto(List<ItemViewModel> list)
            throws InstantiationException, IllegalAccessException, InvalidTypeIdException {
        List<Item> output = new ArrayList<>();
        if (list.isEmpty()) return output;

        for (Object o : list) {
            output.add(convertItemViewModelToDto((ItemViewModel) o));
        }
        return output;
    }

    public void resetFeeType(String pathFeeType) {
        boolean isValidFeeType = false;
        switch (pathFeeType) {
            case FeeType.pathProcessingFee:
                resetProcessingFeeTable();
                break;
            case FeeType.pathSalesTaxRate:
                resetSalesTaxRateTable();
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid feeType. Must be one of: %s",
                        FeeType.paths.keySet().toString()));
        }
    }

    protected void resetSalesTaxRateTable() {
        List<SalesTaxRate> salesTaxRates = salesTaxRateDao.findAll();
        salesTaxRates.forEach(salesTaxRate -> salesTaxRateDao.delete(salesTaxRate.getState()));

        salesTaxRateDao.add(new SalesTaxRate() {{ setState("AL"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("AK"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("AZ"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("AR"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("CA"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("CO"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("CT"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("DE"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("FL"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("GA"); setRate(new BigDecimal(0.07)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("HI"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("ID"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("IL"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("IN"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("IA"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("KS"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("KY"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("LA"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("ME"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MD"); setRate(new BigDecimal(0.07)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MA"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MI"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MN"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MS"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MO"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("MT"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NE"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NV"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NH"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NJ"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NM"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NY"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("NC"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("ND"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("OH"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("OK"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("OR"); setRate(new BigDecimal(0.07)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("PA"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("RI"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("SC"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("SD"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("TN"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("TX"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("UT"); setRate(new BigDecimal(0.04)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("VT"); setRate(new BigDecimal(0.07)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("VA"); setRate(new BigDecimal(0.06)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("WA"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("WV"); setRate(new BigDecimal(0.05)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("WI"); setRate(new BigDecimal(0.03)); }});
        salesTaxRateDao.add(new SalesTaxRate() {{ setState("WY"); setRate(new BigDecimal(0.04)); }});
    }

    protected void resetProcessingFeeTable() {
        List<ProcessingFee> processingFees = processingFeeDao.findAll();
        processingFees.forEach(processingFee -> processingFeeDao.delete(processingFee.getProductType()));

        processingFeeDao.add(new ProcessingFee() {{ setProductType("Consoles"); setFee(new BigDecimal(14.99)); }});
        processingFeeDao.add(new ProcessingFee() {{ setProductType("T-Shirts"); setFee(new BigDecimal(1.98)); }});
        processingFeeDao.add(new ProcessingFee() {{ setProductType("Games"); setFee(new BigDecimal(1.49)); }});
    }
}
