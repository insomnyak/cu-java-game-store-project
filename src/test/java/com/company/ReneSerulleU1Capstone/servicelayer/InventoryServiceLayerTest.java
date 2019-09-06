package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.PurchaseViewModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static junit.framework.TestCase.*;

public class InventoryServiceLayerTest {

    ConsoleDao consoleDao;
    GameDao gameDao;
    InvoiceDao invoiceDao;
    ProcessingFeeDao processingFeeDao;
    SalesTaxRateDao salesTaxRateDao;
    TShirtDao tShirtDao;

    InventoryServiceLayer sl;

    // add, get
    private Game game1a = new Game();
    private Game game1b = new Game();
    private Console console1a = new Console();
    private Console console1b = new Console();
    private TShirt tShirt1a = new TShirt();
    private TShirt tShirt1b = new TShirt();
    private ProcessingFee processingFee1 = new ProcessingFee();
    private ProcessingFee processingFee2 = new ProcessingFee();
    private ProcessingFee processingFee3 = new ProcessingFee();
    private SalesTaxRate salesTaxRate1 = new SalesTaxRate();
    private SalesTaxRate salesTaxRate2 = new SalesTaxRate();
    // add, get Console
    private Invoice invoice1a = new Invoice();
    private Invoice invoice1b = new Invoice();
    // add, get Game
    private Invoice invoice2a = new Invoice();
    private Invoice invoice2b = new Invoice();
    // add, get TShirt
    private Invoice invoice3a = new Invoice();
    private Invoice invoice3b = new Invoice();

    // update
    private Game game2a = new Game();
    private Game game2b = new Game();
    private Game game2c = new Game();
    private Console console2a = new Console();
    private Console console2b = new Console();
    private Console console2c = new Console();
    private TShirt tShirt2a = new TShirt();
    private TShirt tShirt2b = new TShirt();
    private TShirt tShirt2c = new TShirt();
    private ProcessingFee processingFee4a = new ProcessingFee();
    private ProcessingFee processingFee4b = new ProcessingFee();
    private SalesTaxRate salesTaxRate3a = new SalesTaxRate();
    private SalesTaxRate salesTaxRate3b = new SalesTaxRate();

    @Before
    public void setUp() throws Exception {
        constructSampleData();

        setUpConsoleDaoMock();
        setUpGameDaoMock();
        setUpTShirtDaoMock();
        setUpInvoiceDaoMock();
        setUpProcessingFeeDaoMock();
        setUpSalesTaxRateDaoMock();

        sl = new InventoryServiceLayer(gameDao, consoleDao, tShirtDao,
                processingFeeDao, salesTaxRateDao, invoiceDao);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void update() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findBy() {
    }

    @Test
    public void findSearchableAttributes() {
    }

    @Test
    public void findAllProductTypes() {
    }

    @Test
    public void testFind() {
    }

    @Test
    public void add() {
    }

    @Test
    public void setCalculatedAttributes() {
    }

    @Test
    public void validateAttributeValues() {
    }

    @Test
    public void testAdd() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void testFind1() {
    }

    @Test
    public void testFindAll() {
    }

    @Test
    public void testAdd1() {
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testDelete() {
    }

    public void constructSampleData() {
        game1a.setTitle("Final Fantasy VIII");
        game1a.setEsrbRating("A+");
        game1a.setDescription("rpg");
        game1a.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game1a.setStudio("Square Enix");
        game1a.setQuantity(15L);

        game1b.setGameId(1L);
        game1b.setTitle("Final Fantasy VIII");
        game1b.setEsrbRating("A+");
        game1b.setDescription("rpg");
        game1b.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game1b.setStudio("Square Enix");
        game1b.setQuantity(15L);

        game2a.setTitle("Final Fantasy VIII");
        game2a.setEsrbRating("A+");
        game2a.setDescription("rpg");
        game2a.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2a.setStudio("Square Enix");
        game2a.setQuantity(15L);

        game2b.setGameId(2L);
        game2b.setTitle("Final Fantasy VIII");
        game2b.setEsrbRating("A+");
        game2b.setDescription("rpg");
        game2b.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2b.setStudio("Square Enix");
        game2b.setQuantity(15L);

        game2c.setGameId(2L);
        game2c.setTitle("Final Fantasy VIII");
        game2c.setEsrbRating("A+");
        game2c.setDescription("rpg");
        game2c.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2c.setStudio("Square Enix");
        game2c.setQuantity(10L);

        console1a.setModel("PS4");
        console1a.setManufacturer("Sony");
        console1a.setMemoryAmount("10TB");
        console1a.setProcessor("1.6 GHz");
        console1a.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console1a.setQuantity(55L);

        console1b.setConsoleId(1L);
        console1b.setModel("PS4");
        console1b.setManufacturer("Sony");
        console1b.setMemoryAmount("10TB");
        console1b.setProcessor("1.6 GHz");
        console1b.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console1b.setQuantity(55L);

        console2a.setModel("PS4");
        console2a.setManufacturer("Sony");
        console2a.setMemoryAmount("10TB");
        console2a.setProcessor("1.6 GHz");
        console2a.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2a.setQuantity(55L);

        console2b.setConsoleId(2L);
        console2b.setModel("PS4");
        console2b.setManufacturer("Sony");
        console2b.setMemoryAmount("10TB");
        console2b.setProcessor("1.6 GHz");
        console2b.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2b.setQuantity(55L);

        console2c.setConsoleId(2L);
        console2c.setModel("PS4");
        console2c.setManufacturer("Sony");
        console2c.setMemoryAmount("10TB");
        console2c.setProcessor("1.6 GHz");
        console2c.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2c.setQuantity(31L);

        tShirt1a.setSize("L");
        tShirt1a.setColor("Black");
        tShirt1a.setDescription("Game of Thrones");
        tShirt1a.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt1a.setQuantity(25L);

        tShirt1b.setTShirtId(1L);
        tShirt1b.setSize("L");
        tShirt1b.setColor("Black");
        tShirt1b.setDescription("Game of Thrones");
        tShirt1b.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt1b.setQuantity(25L);

        tShirt2a.setSize("L");
        tShirt2a.setColor("Black");
        tShirt2a.setDescription("Game of Thrones");
        tShirt2a.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt2a.setQuantity(25L);

        tShirt2b.setTShirtId(2L);
        tShirt2b.setSize("L");
        tShirt2b.setColor("Black");
        tShirt2b.setDescription("Game of Thrones");
        tShirt2b.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt2b.setQuantity(25L);

        tShirt2c.setTShirtId(2L);
        tShirt2c.setSize("L");
        tShirt2c.setColor("Black");
        tShirt2c.setDescription("Jumanji");
        tShirt2c.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt2c.setQuantity(20L);

        processingFee1.setProductType("Consoles");
        processingFee1.setFee(new BigDecimal(14.99).setScale(2, RoundingMode.HALF_UP));

        processingFee2.setProductType("T-Shirts");
        processingFee2.setFee(new BigDecimal(1.98).setScale(2, RoundingMode.HALF_UP));

        processingFee3.setProductType("Games");
        processingFee3.setFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));

        processingFee4a.setProductType("GamesTest");
        processingFee4a.setFee(new BigDecimal(1.37).setScale(2, RoundingMode.HALF_UP));

        processingFee4b.setProductType("Games");
        processingFee4b.setFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));

        salesTaxRate1.setState("NY");
        salesTaxRate1.setRate(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP));

        salesTaxRate2.setState("HI");
        salesTaxRate2.setRate(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP));

        salesTaxRate3a.setState("FL");
        salesTaxRate3a.setRate(new BigDecimal(0.16).setScale(2, RoundingMode.HALF_UP));

        salesTaxRate3b.setState("FL");
        salesTaxRate3b.setRate(new BigDecimal(0.06).setScale(2, RoundingMode.HALF_UP));

        invoice1a.setName("rene");
        invoice1a.setStreet("23rd st");
        invoice1a.setCity("NYC");
        invoice1a.setState("NY");
        invoice1a.setZipcode("10019");
        invoice1a.setItemType("Consoles");
        invoice1a.setItemId(2L);
        invoice1a.setQuantity(24L);
        invoice1a.setUnitPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setSubtotal(new BigDecimal(4943.76).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setTax(new BigDecimal(247.19).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setProcessingFee(new BigDecimal(30.48).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setTotal(new BigDecimal(5221.43).setScale(2, RoundingMode.HALF_UP));

        invoice1b.setInvoiceId(1L);
        invoice1b.setName("rene");
        invoice1b.setStreet("23rd st");
        invoice1b.setCity("NYC");
        invoice1b.setState("NY");
        invoice1b.setZipcode("10019");
        invoice1b.setItemType("Consoles");
        invoice1b.setItemId(2L);
        invoice1b.setQuantity(24L);
        invoice1b.setUnitPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setSubtotal(new BigDecimal(4943.76).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setTax(new BigDecimal(247.19).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setProcessingFee(new BigDecimal(30.48).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setTotal(new BigDecimal(5221.43).setScale(2, RoundingMode.HALF_UP));

        invoice2a.setName("rene");
        invoice2a.setStreet("23rd st");
        invoice2a.setCity("NYC");
        invoice2a.setState("NY");
        invoice2a.setZipcode("10019");
        invoice2a.setItemType("Games");
        invoice2a.setItemId(2L);
        invoice2a.setQuantity(5L);
        invoice2a.setUnitPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setSubtotal(new BigDecimal(129.95).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setTax(new BigDecimal(6.50).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setProcessingFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setTotal(new BigDecimal(137.94).setScale(2, RoundingMode.HALF_UP));

        invoice2b.setInvoiceId(2L);
        invoice2b.setName("rene");
        invoice2b.setStreet("23rd st");
        invoice2b.setCity("NYC");
        invoice2b.setState("NY");
        invoice2b.setZipcode("10019");
        invoice2b.setItemType("Consoles");
        invoice2b.setItemId(2L);
        invoice2b.setQuantity(24L);
        invoice2b.setUnitPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setSubtotal(new BigDecimal(129.95).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setTax(new BigDecimal(6.50).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setProcessingFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setTotal(new BigDecimal(137.94).setScale(2, RoundingMode.HALF_UP));

        invoice3a.setName("rene");
        invoice3a.setStreet("Volcano Road");
        invoice3a.setCity("Honolulu");
        invoice3a.setState("HI");
        invoice3a.setZipcode("10019");
        invoice3a.setItemType("T-Shirts");
        invoice3a.setItemId(2L);
        invoice3a.setQuantity(5L);
        invoice3a.setUnitPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        invoice3a.setSubtotal(new BigDecimal(99.95).setScale(2, RoundingMode.HALF_UP));
        invoice3a.setTax(new BigDecimal(5.00).setScale(2, RoundingMode.HALF_UP));
        invoice3a.setProcessingFee(new BigDecimal(1.98).setScale(2, RoundingMode.HALF_UP));
        invoice3a.setTotal(new BigDecimal(106.93).setScale(2, RoundingMode.HALF_UP));

        invoice3b.setInvoiceId(3L);
        invoice3b.setName("rene");
        invoice3b.setStreet("23rd st");
        invoice3b.setCity("NYC");
        invoice3b.setState("NY");
        invoice3b.setZipcode("10019");
        invoice3b.setItemType("Consoles");
        invoice3b.setItemId(2L);
        invoice3b.setQuantity(24L);
        invoice3b.setUnitPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setSubtotal(new BigDecimal(99.95).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setTax(new BigDecimal(5.00).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setProcessingFee(new BigDecimal(1.98).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setTotal(new BigDecimal(106.93).setScale(2, RoundingMode.HALF_UP));
    }

    public void setUpConsoleDaoMock() {
        consoleDao = mock(ConsoleJdbcTemplateDaoImpl.class);

        List<Console> consoles1 = new ArrayList<>();
        consoles1.add(console1b);

        List<Console> emptyList = new ArrayList<>();

        doReturn(console1b).when(consoleDao).add(console1a);
        doReturn(console1b).when(consoleDao).find(1L);
        doReturn(consoles1).when(consoleDao).findAllByManufacturer("Sony");
        doReturn(console2b).when(consoleDao).add(console2a);
        doNothing().when(consoleDao).update(console2c);
        doReturn(console2c).when(consoleDao).find(2L);
        doNothing().when(consoleDao).delete(3L);
        doReturn(null).when(consoleDao).find(3L);
        doReturn(consoles1).when(consoleDao).findAll();
        doReturn(emptyList).when(consoleDao).findAllByManufacturer("Blizzard");
    }

    public void setUpGameDaoMock() {
        gameDao = mock(GameJdbcTemplateDaoImpl.class);

        List<Game> games1 = new ArrayList<>();
        games1.add(game1b);

        List<Game> emptyList = new ArrayList<>();

        doReturn(game1b).when(gameDao).add(game1a);
        doReturn(game1b).when(gameDao).find(1L);
        doReturn(games1).when(gameDao).findAllByTitle("Final Fantasy VIII");
        doReturn(games1).when(gameDao).findAllByEsrbRating("A+");
        doReturn(games1).when(gameDao).findAllByStudio("Square Enix");
        doReturn(emptyList).when(gameDao).findAllByTitle("random");
        doReturn(emptyList).when(gameDao).findAllByEsrbRating("random");
        doReturn(emptyList).when(gameDao).findAllByStudio("random");
        doReturn(game2b).when(gameDao).add(game2a);
        doNothing().when(gameDao).update(game2c);
        doReturn(game2c).when(gameDao).find(2L);
        doNothing().when(gameDao).delete(3L);
        doReturn(null).when(gameDao).find(3L);
    }

    public void setUpTShirtDaoMock() {
        tShirtDao = mock(TShirtJdbcTemplateDaoImpl.class);
    }

    public void setUpProcessingFeeDaoMock() {
        processingFeeDao = mock(ProcessingFeeJdbcTemplateDaoImpl.class);
    }

    public void setUpSalesTaxRateDaoMock() {
        salesTaxRateDao = mock(SalesTaxRateJdbcTemplateDaoImpl.class);
    }

    public void setUpInvoiceDaoMock() {
        invoiceDao = mock(InvoiceJdbcTemplateDaoImpl.class);
    }
}