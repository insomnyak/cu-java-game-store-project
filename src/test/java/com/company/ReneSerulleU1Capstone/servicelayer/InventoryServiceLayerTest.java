package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InventoryServiceLayerTest {

    @Autowired
    InventoryServiceLayer sl;
    @Autowired
    ConsoleDao consoleDao;
    @Autowired
    GameDao gameDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    ProcessingFeeDao processingFeeDao;
    @Autowired
    SalesTaxRateDao salesTaxRateDao;
    @Autowired
    TShirtDao tShirtDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void updateItem() {
    }

    @Test
    public void findItem() {
    }

    @Test
    public void findAllItems() {
    }

    @Test
    public void findItemsBy() {
    }

    @Test
    public void addPurchaseViewModel() {
    }

    @Test
    public void setPurchaseViewModelCalculatedAttributes() {
    }

    @Test
    public void validatePurchaseViewModelValues() {
    }

    @Test
    public void findPurchaseViewModel() {
    }

    @Test
    public void addItem() {
    }

    @Test
    public void deleteItem() {
    }

    @Test
    public void findAllPurchaseViewModels() {
    }
}