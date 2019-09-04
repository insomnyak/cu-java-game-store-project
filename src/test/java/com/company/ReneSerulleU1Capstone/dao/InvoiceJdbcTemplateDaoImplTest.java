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
public class InvoiceJdbcTemplateDaoImplTest {

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
        List<Invoice> invoices = invoiceDao.findAll();
        invoices.forEach(invoice -> invoiceDao.delete(invoice.getInvoiceId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
        Invoice invoice1 = new Invoice() {{
            setName("RKS");
            setStreet("23 Chambers");
            setCity("NYC");
            setState("NY");
            setZipcode("10025");
            setItemType("console");
            setItemId(1234L);
            setUnitPrice(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(2L);
            setSubtotal(new BigDecimal(123.46).setScale(2, RoundingMode.HALF_UP));
            setTax(new BigDecimal(3.45).setScale(2, RoundingMode.HALF_UP));
            setProcessingFee(new BigDecimal(12.44).setScale(2, RoundingMode.HALF_UP));
            setTotal(new BigDecimal(444.44).setScale(2, RoundingMode.HALF_UP));
        }};
        invoiceDao.add(invoice1);

        List<Invoice> invoices = invoiceDao.findAll();
        assertEquals(invoices.size(), 1);
    }

    @Test
    public void update() {
        Invoice invoice1 = new Invoice() {{
            setName("RKS");
            setStreet("23 Chambers");
            setCity("NYC");
            setState("NY");
            setZipcode("10025");
            setItemType("console");
            setItemId(1234L);
            setUnitPrice(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(2L);
            setSubtotal(new BigDecimal(123.46).setScale(2, RoundingMode.HALF_UP));
            setTax(new BigDecimal(3.45).setScale(2, RoundingMode.HALF_UP));
            setProcessingFee(new BigDecimal(12.44).setScale(2, RoundingMode.HALF_UP));
            setTotal(new BigDecimal(444.44).setScale(2, RoundingMode.HALF_UP));
        }};
        invoiceDao.add(invoice1);

        invoice1.setTotal(new BigDecimal(450).setScale(2, RoundingMode.HALF_UP));
        invoice1.setZipcode("10019");

        invoiceDao.update(invoice1);

        Invoice invoice2 = invoiceDao.find(invoice1.getInvoiceId());
        assertEquals(invoice2, invoice1);
    }

    @Test
    public void addGetDelete() {
        Invoice invoice1 = new Invoice() {{
            setName("RKS");
            setStreet("23 Chambers");
            setCity("NYC");
            setState("NY");
            setZipcode("10025");
            setItemType("console");
            setItemId(1234L);
            setUnitPrice(new BigDecimal(123.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(2L);
            setSubtotal(new BigDecimal(123.46).setScale(2, RoundingMode.HALF_UP));
            setTax(new BigDecimal(3.45).setScale(2, RoundingMode.HALF_UP));
            setProcessingFee(new BigDecimal(12.44).setScale(2, RoundingMode.HALF_UP));
            setTotal(new BigDecimal(444.44).setScale(2, RoundingMode.HALF_UP));
        }};
        invoiceDao.add(invoice1);

        // test get
        Invoice invoice2 = invoiceDao.find(invoice1.getInvoiceId());
        assertEquals(invoice2, invoice1);

        // test delete
        invoiceDao.delete(invoice1.getInvoiceId());
        invoice2 = invoiceDao.find(invoice1.getInvoiceId());
        assertNull(invoice2);
    }

    @Test
    public void dbConstraintViolations() {
        //(expected = DataIntegrityViolationException.class)
        Invoice invoice1;

        try {
            invoice1 = new Invoice() {{
                setName("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setStreet("23 Chambers");
                setCity("NYC");
                setState("NY");
                setZipcode("10025");
                setItemType("console");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setCity("NYC");
                setState("NY");
                setZipcode("10025");
                setItemType("console");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("23 Chambers");
                setCity("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setState("NY");
                setZipcode("10025");
                setItemType("console");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("23 Chambers");
                setCity("NYC");
                setState("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setZipcode("10025");
                setItemType("console");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("23 Chambers");
                setCity("NYC");
                setState("NY");
                setZipcode("100256");
                setItemType("console");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("23 Chambers");
                setCity("NYC");
                setState("NY");
                setZipcode("10025");
                setItemType("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setItemId(1234L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            invoice1 = new Invoice() {{
                setName("RKS");
                setStreet("23 Chambers");
                setCity("NYC");
                setState("NY");
                setZipcode("10025");
                setItemType("console");
                setItemId(123456789012L);
                setUnitPrice(new BigDecimal(123.45));
                setQuantity(2L);
                setSubtotal(new BigDecimal(123.46));
                setTax(new BigDecimal(3.45));
                setProcessingFee(new BigDecimal(12.44));
                setTotal(new BigDecimal(444.44));
            }};
            invoiceDao.add(invoice1);
        } catch (DataIntegrityViolationException ignore) {}

        List<Invoice> invoices = invoiceDao.findAll();
        assertEquals(invoices.size(), 0);
    }
}