package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseFee;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Iterator;
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
    public List<Item> add(List<Item> items) throws InvalidTypeIdException {
        List<Item> addedItems = new ArrayList<>();
        for (Item item : items) {
            addedItems.add(add(item));
        }
        return addedItems;
    }

    @Transactional
    public Item add(Item item) throws InvalidTypeIdException {
        if (item instanceof Game) {
            return gameDao.add((Game) item);
        } else if (item instanceof Console) {
            return consoleDao.add((Console) item);
        } else if (item instanceof TShirt) {
            return tShirtDao.add((TShirt) item);
        }
        throw invalidTypeIdException(item.getClass().getTypeName());
    }

    @Transactional
    public void update(List<Item> items) throws InvalidTypeIdException {
        for (Item item : items) {
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

    public Object find(Class className, String value) throws InvalidClassException {
        if (className.equals(ProcessingFeeViewModel.class)) {
            return processingFeeDao.find(value);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            return salesTaxRateDao.find(value);
        }
        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    @Override
    public List<?> findAll(Class className) throws InvalidClassException {
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
    public void delete(Class className, String value) throws InvalidClassException {
        if (className.equals(ProcessingFeeViewModel.class)) {
            processingFeeDao.delete(value);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            salesTaxRateDao.delete(value);
        }   else {
            throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
        }
    }
}
