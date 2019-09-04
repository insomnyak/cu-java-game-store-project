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
public class ProcessingFeeJdbcTemplateDaoImplTest {

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
        resetProcessingFeeTable();
    }

    private void resetProcessingFeeTable() {
        List<ProcessingFee> processingFees = processingFeeDao.findAll();
        processingFees.forEach(processingFee -> processingFeeDao.delete(processingFee));

        processingFeeDao.add(new ProcessingFee() {{ setProductType("Consoles"); setFee(new BigDecimal(14.99)); }});
        processingFeeDao.add(new ProcessingFee() {{ setProductType("Shirts"); setFee(new BigDecimal(1.98)); }});
        processingFeeDao.add(new ProcessingFee() {{ setProductType("Games"); setFee(new BigDecimal(1.49)); }});
    }

    @After
    public void tearDown() throws Exception {
        resetProcessingFeeTable();
    }

    @Test
    public void findAll() {
        List<ProcessingFee> processingFees = processingFeeDao.findAll();
        assertEquals(processingFees.size(), 3);
    }

    @Test
    public void addGetUpdateDelete() {
        ProcessingFee processingFee1 = new ProcessingFee() {{
            setProductType("Unknown");
            setFee(new BigDecimal(24.33).setScale(2, RoundingMode.HALF_UP));
        }};
        processingFeeDao.add(processingFee1);

        // test get
        List<ProcessingFee> processingFees = processingFeeDao.find(processingFee1.getProductType());
        assertEquals(processingFees.get(0), processingFee1);

        // test update
        processingFee1.setFee(new BigDecimal(29.33).setScale(2, RoundingMode.HALF_UP));
        processingFeeDao.update(processingFee1, new ProcessingFee() {{
            setProductType("Unknown");
            setFee(new BigDecimal(24.33).setScale(2, RoundingMode.HALF_UP));
        }});
        processingFees = processingFeeDao.find(processingFee1.getProductType());
        assertEquals(processingFees.get(0), processingFee1);

        // test delete
        processingFeeDao.delete(processingFee1);
        processingFees = processingFeeDao.find(processingFee1.getProductType());
        assertEquals(processingFees.size(), 0);
    }

    @Test
    public void dbConstraintViolations() {
        //(expected = DataIntegrityViolationException.class)
        ProcessingFee processingFee1;

        try {
            processingFee1 = new ProcessingFee() {{
                setProductType("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setFee(new BigDecimal(24.33));
            }};
            processingFeeDao.add(processingFee1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            processingFee1 = new ProcessingFee() {{
                setProductType("Unknown");
                setFee(new BigDecimal(12324.33));
            }};
            processingFeeDao.add(processingFee1);
        } catch (DataIntegrityViolationException ignore) {}

        List<ProcessingFee> processingFees = processingFeeDao.findAll();
        assertEquals(processingFees.size(), 3);
    }
}