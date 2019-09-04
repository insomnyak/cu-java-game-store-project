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
public class TShirtJdbcTemplateDaoImplTest {

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
        List<TShirt> tShirts = tShirtDao.findAll();
        tShirts.forEach(tShirt -> tShirtDao.delete(tShirt.getTShirtId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAllOrByColorSize() {
        TShirt tShirt1 = new TShirt() {{
            setSize("XL");
            setColor("Red");
            setDescription("Spider-man Multi-verse");
            setPrice(new BigDecimal(25.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(45L);

        }};
        tShirtDao.add(tShirt1);

        // test findAll
        List<TShirt> tShirts = tShirtDao.findAll();
        assertEquals(tShirts.size(), 1);

        // test find by color
        tShirts = tShirtDao.findAllByColor("Red");
        assertEquals(tShirts.size(), 1);

        tShirts = tShirtDao.findAllByColor("Black");
        assertEquals(tShirts.size(), 0);

        // test find by Size
        tShirts = tShirtDao.findAllBySize("XL");
        assertEquals(tShirts.size(), 1);

        tShirts = tShirtDao.findAllBySize("XS");
        assertEquals(tShirts.size(), 0);
    }

    @Test
    public void update() {
        TShirt tShirt1 = new TShirt() {{
            setSize("XL");
            setColor("Red");
            setDescription("Spider-man Multi-verse");
            setPrice(new BigDecimal(25.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(45L);

        }};
        tShirtDao.add(tShirt1);

        tShirt1.setQuantity(40L);
        tShirtDao.update(tShirt1);

        TShirt tShirt2 = tShirtDao.find(tShirt1.getTShirtId());
        assertEquals(tShirt2, tShirt1);
    }

    @Test
    public void addGetDelete() {
        TShirt tShirt1 = new TShirt() {{
            setSize("XL");
            setColor("Red");
            setDescription("Spider-man Multi-verse");
            setPrice(new BigDecimal(25.45).setScale(2, RoundingMode.HALF_UP));
            setQuantity(45L);

        }};
        tShirtDao.add(tShirt1);

        // test get
        TShirt tShirt2 = tShirtDao.find(tShirt1.getTShirtId());
        assertEquals(tShirt2, tShirt1);

        // test delete
        tShirtDao.delete(tShirt1.getTShirtId());
        tShirt2 = tShirtDao.find(tShirt1.getTShirtId());
        assertNull(tShirt2);
    }

    @Test
    public void dbConstraintViolations() {
        //(expected = DataIntegrityViolationException.class)
        TShirt tShirt1;

        try {
            tShirt1 = new TShirt() {{
                setSize("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setColor("Red");
                setDescription("Spider-man Multi-verse");
                setPrice(new BigDecimal(25.45));
                setQuantity(45L);

            }};
            tShirtDao.add(tShirt1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            tShirt1 = new TShirt() {{
                setSize("XL");
                setColor("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setDescription("Spider-man Multi-verse");
                setPrice(new BigDecimal(25.45));
                setQuantity(45L);

            }};
            tShirtDao.add(tShirt1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            tShirt1 = new TShirt() {{
                setSize("XL");
                setColor("Red");
                setDescription("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setPrice(new BigDecimal(25.45));
                setQuantity(45L);

            }};
            tShirtDao.add(tShirt1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            tShirt1 = new TShirt() {{
                setSize("XL");
                setColor("Red");
                setDescription("Spider-man Multi-verse");
                setPrice(new BigDecimal(123425.45));
                setQuantity(45L);

            }};
            tShirtDao.add(tShirt1);
        } catch (DataIntegrityViolationException ignore) {}

        List<TShirt> tShirts = tShirtDao.findAll();
        assertEquals(tShirts.size(), 0);
    }
}