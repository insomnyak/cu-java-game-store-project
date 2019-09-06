package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

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
    public void delete(String itemType, Long id) throws InvalidTypeIdException {
        switch (itemType) {
            case ItemType.game:
                gameDao.delete(id);
            case ItemType.console:
                consoleDao.delete(id);
            case ItemType.tShirt:
                tShirtDao.delete(id);
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
        if (className.equals(ProcessingFeeViewModel.class)) {
            return processingFeeDao.findAll();
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            return salesTaxRateDao.findAll();
        } else if (className.equals(PurchaseViewModel.class)) {
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
    public Object add(Class className, Object obj) throws InvalidClassException {
        matchObjectToClass(className, obj);

        if (className.equals(ProcessingFeeViewModel.class)) {
            return processingFeeDao.add((ProcessingFee) obj);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            return salesTaxRateDao.add((SalesTaxRate) obj);
        }

        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    @Transactional
    public void update(Class className, Object obj, String lookupValue) throws InvalidClassException {
        matchObjectToClass(className, obj);

        if (className.equals(ProcessingFeeViewModel.class)) {
            processingFeeDao.update((ProcessingFee) obj, lookupValue);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            salesTaxRateDao.update((SalesTaxRate) obj, lookupValue);
        }

        throw invalidClassException(className, ProcessingFeeViewModel.class, SalesTaxRateViewModel.class);
    }

    @Transactional
    public void delete(Class className, String value) {
        if (className.equals(ProcessingFeeViewModel.class)) {
            processingFeeDao.delete(value);
        } else if (className.equals(SalesTaxRateViewModel.class)) {
            salesTaxRateDao.delete(value);
        }
    }
}
