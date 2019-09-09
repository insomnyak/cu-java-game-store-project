package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.*;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            Game newGame =  gameDao.add((Game) convertItemVmToDto(item));
            gameVM.setGameId(newGame.getGameId());
            return gameVM;
        } else if (item instanceof Console) {
            ConsoleViewModel consoleVM = (ConsoleViewModel) item;
            Console newConsole = consoleDao.add((Console) convertItemVmToDto(item));
            consoleVM.setConsoleId(newConsole.getConsoleId());
            return consoleVM;
        } else if (item instanceof TShirt) {
            TShirtViewModel tshirtVM = (TShirtViewModel) item;
            TShirt newTShirt = tShirtDao.add((TShirt) convertItemVmToDto(item));
            tshirtVM.settShirtId(newTShirt.gettShirtId());
            return tshirtVM;
        }
        throw invalidTypeIdException(item.getClass().getTypeName());
    }

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

    public PurchaseFee findFeeType(String pathFeeType, String lookupValue)
            throws IllegalAccessException, InvalidClassException, InstantiationException {
        for (Map.Entry<String, Class> feeType : FeeType.paths.entrySet()) {
            if (pathFeeType.equals(feeType.getKey())) {
                return find(feeType.getValue(), lookupValue);
            }
        }
        throw new IllegalArgumentException(String.format("Invalid feeType. Must be one of: %s",
                FeeType.paths.keySet().toString()));
    }


    public PurchaseFee find(Class className, String value)
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
                } catch (InvalidTypeIdException ignore) {}
            });
            return pvms;
        }
        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class,
                PurchaseViewModel.class);
    }

    @Transactional
    public List<?> add(ArrayList<PurchaseFee> purchaseFees) throws InvalidClassException {
        List<Object> purchaseFees1 = new ArrayList<>();
        for (PurchaseFee purchaseFee : purchaseFees) {
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

    @Transactional
    public void update(Map<String, PurchaseFee> purchaseFeeMap) throws InvalidClassException {
        for (Map.Entry<String, PurchaseFee> entry : purchaseFeeMap.entrySet()) {
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
            output.add(convertItemVmToDto((ItemViewModel) o));
        }
        return output;
    }
}
