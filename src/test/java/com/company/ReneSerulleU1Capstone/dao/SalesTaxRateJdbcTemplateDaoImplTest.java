package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SalesTaxRateJdbcTemplateDaoImplTest {

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
        resetSalesTaxRateTable();
    }

    private void resetSalesTaxRateTable() {
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

    @After
    public void tearDown() throws Exception {
        resetSalesTaxRateTable();
    }

    @Test
    public void findAll() {
        List<SalesTaxRate> salesTaxRates = salesTaxRateDao.findAll();
        assertEquals(salesTaxRates.size(), 50);
    }

    @Test
    public void addGetUpdateDelete() {
        SalesTaxRate salesTaxRate = new SalesTaxRate() {{
            setState("PR");
            setRate(new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP));
        }};
        salesTaxRateDao.add(salesTaxRate);

        // test get
        SalesTaxRate salesTaxRate1 = salesTaxRateDao.find(salesTaxRate.getState());
        assertEquals(salesTaxRate1, salesTaxRate);

        // test update
        salesTaxRate.setRate(new BigDecimal(0.02).setScale(2, RoundingMode.HALF_UP));
        salesTaxRateDao.update(salesTaxRate, "PR");
        salesTaxRate1 = salesTaxRateDao.find(salesTaxRate.getState());
        assertEquals(salesTaxRate1, salesTaxRate);

        // test delete
        salesTaxRateDao.delete(salesTaxRate.getState());
        salesTaxRate1 = salesTaxRateDao.find(salesTaxRate.getState());
        assertNull(salesTaxRate1);
    }

    @Test
    public void dbConstraintViolations() {
        //(expected = DataIntegrityViolationException.class)
        SalesTaxRate salesTaxRate;

        try {
            salesTaxRate = new SalesTaxRate() {{
                setState("PRs");
                setRate(new BigDecimal(0.01));
            }};
            salesTaxRateDao.add(salesTaxRate);
        } catch (DataIntegrityViolationException ignore) {}

        List<SalesTaxRate> salesTaxRates = salesTaxRateDao.findAll();
        assertEquals(salesTaxRates.size(), 50);
    }
}