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
public class ConsoleJdbcTemplateDaoImplTest {

    @Autowired
    private ConsoleDao consoleDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private ProcessingFeeDao processingFeeDao;
    @Autowired
    private SalesTaxRateDao salesTaxRateDao;
    @Autowired
    private TShirtDao tShirtDao;

    @Before
    public void setUp() throws Exception {
        List<Console> consoles = consoleDao.findAll();
        consoles.forEach(console -> consoleDao.delete(console.getConsoleId()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAllOrByManufacturer() {
        Console console1 = new Console() {{
            setModel("PS4");
            setManufacturer("Sony");
            setMemoryAmount("20TB");
            setProcessor("11.0 GHz");
            setPrice(new BigDecimal(254.99).setScale(2, RoundingMode.HALF_UP));
            setQuantity(33L);
        }};
        consoleDao.add(console1);

        console1 = new Console() {{
            setModel("PS4 4K");
            setManufacturer("Sony");
            setMemoryAmount("20TB");
            setProcessor("15.0 GHz");
            setPrice(new BigDecimal(324.99).setScale(2, RoundingMode.HALF_UP));
            setQuantity(23L);
        }};
        consoleDao.add(console1);

        List<Console> consoles = consoleDao.findAll();
        assertEquals(consoles.size(), 2);

        consoles = consoleDao.findAllByManufacturer("Sony");
        assertEquals(consoles.size(), 2);

        consoles = consoleDao.findAllByManufacturer("Microsoft");
        assertEquals(consoles.size(), 0);
    }

    @Test
    public void update() {
        Console console1 = new Console() {{
            setModel("PS4");
            setManufacturer("Sony");
            setMemoryAmount("20TB");
            setProcessor("11.0 GHz");
            setPrice(new BigDecimal(254.99).setScale(2, RoundingMode.HALF_UP));
            setQuantity(33L);
        }};
        consoleDao.add(console1);

        console1.setQuantity(44L);
        consoleDao.update(console1);

        Console console2 = consoleDao.find(console1.getConsoleId());
        assertEquals(console2, console1);
    }

    @Test
    public void addGetDelete() {
        Console console1 = new Console() {{
            setModel("PS4");
            setManufacturer("Sony");
            setMemoryAmount("20TB");
            setProcessor("11.0 GHz");
            setPrice(new BigDecimal(254.99).setScale(2, RoundingMode.HALF_UP));
            setQuantity(33L);
        }};
        consoleDao.add(console1);

        // test get
        Console console2 = consoleDao.find(console1.getConsoleId());
        assertEquals(console2, console1);

        // test delete
        consoleDao.delete(console1.getConsoleId());
        console2 = consoleDao.find(console1.getConsoleId());
        assertNull(console2);

    }

    @Test
    public void dbConstraintViolations() {
        // (expected = DataIntegrityViolationException.class)
        Console console1;

        try {
            console1 = new Console() {{
                setModel("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setManufacturer("Sony");
                setMemoryAmount("20TB");
                setProcessor("11.0 GHz");
                setPrice(new BigDecimal(254.99));
                setQuantity(33L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            console1 = new Console() {{
                setModel("PS4");
                setManufacturer("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setMemoryAmount("20TB");
                setProcessor("11.0 GHz");
                setPrice(new BigDecimal(254.99));
                setQuantity(33L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            console1 = new Console() {{
                setModel("PS4");
                setManufacturer("Sony");
                setMemoryAmount("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setProcessor("11.0 GHz");
                setPrice(new BigDecimal(254.99));
                setQuantity(33L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            console1 = new Console() {{
                setModel("PS4");
                setManufacturer("Sony");
                setMemoryAmount("20TB");
                setProcessor("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setPrice(new BigDecimal(254.99));
                setQuantity(33L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            console1 = new Console() {{
                setModel("PS4");
                setManufacturer("Sony");
                setMemoryAmount("20TB");
                setProcessor("11.0 GHz");
                setPrice(new BigDecimal(123254.99));
                setQuantity(33L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            console1 = new Console() {{
                setModel("PS4");
                setManufacturer("Sony");
                setMemoryAmount("20TB");
                setProcessor("11.0 GHz");
                setPrice(new BigDecimal(254.99));
                setQuantity(123456789012L);
            }};
            consoleDao.add(console1);
        } catch (DataIntegrityViolationException ignore) {}

        List<Console> consoles = consoleDao.findAll();
        assertEquals(consoles.size(), 0);
    }
}