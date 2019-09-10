package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.dao.*;
import com.company.ReneSerulleU1Capstone.model.*;
import com.company.ReneSerulleU1Capstone.viewmodel.*;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.InvalidAttributeValueException;
import javax.naming.ServiceUnavailableException;
import java.io.InvalidClassException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    private GameViewModel game1a;
    private Game game1b;
    private GameViewModel game1bVM;
    private ConsoleViewModel console1a;
    private Console console1b;
    private TShirtViewModel tShirt1a;
    private TShirt tShirt1b;
    private ProcessingFee processingFee1;
    private ProcessingFee processingFee2;
    private ProcessingFee processingFee3;
    private SalesTaxRate salesTaxRate1;
    private SalesTaxRate salesTaxRate2;
    // add, get Console
    private Invoice invoice1a;
    private Invoice invoice1b;
    // add, get Game
    private Invoice invoice2a;
    private Invoice invoice2b;
    // add, get TShirt
    private Invoice invoice3a;
    private Invoice invoice3b;

    // update
    private GameViewModel game2a;
    private Game game2b;
    private Game game2c;
    private ConsoleViewModel console2a;
    private Console console2b;
    private Console console2c;
    private TShirtViewModel tShirt2a;
    private TShirt tShirt2b;
    private TShirt tShirt2c;
    private ProcessingFee processingFee4a;
    private ProcessingFee processingFee4b;
    private SalesTaxRate salesTaxRate3a;
    private SalesTaxRate salesTaxRate3b;

    // add, get User purchase
    private PurchaseViewModel purchaseViewModel1;
    private PurchaseViewModel purchaseViewModel2;
    private PurchaseViewModel purchaseViewModel3;

    // user purchase with invalid quantity
    private PurchaseViewModel purchaseViewModel4;

    // user purchase with invalid stateCode
    private PurchaseViewModel purchaseViewModel5;

    // user purchase with invalid itemType
    private PurchaseViewModel purchaseViewModel6;

    // user purchase with invalid ItemId
    private PurchaseViewModel purchaseViewModel7;

    // user purchase with invalid ItemId
    private PurchaseViewModel purchaseViewModel8;

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
        // add, get
        game1a = null;
        game1b = null;
        console1a = null;
        console1b = null;
        tShirt1a = null;
        tShirt1b = null;
        processingFee1 = null;
        processingFee2 = null;
        processingFee3 = null;
        salesTaxRate1 = null;
        salesTaxRate2 = null;
        // add, get Console
        invoice1a = null;
        invoice1b = null;
        // add, get Game
        invoice2a = null;
        invoice2b = null;
        // add, get TShirt
        invoice3a = null;
        invoice3b = null;

        // update
        game2a = null;
        game2b = null;
        game2c = null;
        console2a = null;
        console2b = null;
        console2c = null;
        tShirt2a = null;
        tShirt2b = null;
        tShirt2c = null;
        processingFee4a = null;
        processingFee4b = null;
        salesTaxRate3a = null;
        salesTaxRate3b = null;

        // add, get User purchase
        purchaseViewModel1 = null;
        purchaseViewModel2 = null;
        purchaseViewModel3 = null;

        // user purchase with invalid quantity
        purchaseViewModel4 = null;

        // user purchase with invalid stateCode
        purchaseViewModel5 = null;

        // user purchase with invalid itemType
        purchaseViewModel6 = null;

        // user purchase with invalid ItemId
        purchaseViewModel7 = null;

        // user purchase with invalid ItemId
        purchaseViewModel8 = null;
    }

    // Processing Fee > Product Type: get list
    @Test
    public void findAllProductTypes()
            throws InvalidClassException, IllegalAccessException, InvalidTypeIdException, InstantiationException {
        List<String> productTypes = new ArrayList<>();
        productTypes = sl.findAllProductTypes();
        assertEquals(productTypes.size(), 3);
    }

    // SalesTaxRate: add, find, findAll, update, delete
    @Test
    public void addGetGetAllUpdateDeleteSalesTaxRates()
            throws InvalidClassException, InstantiationException, IllegalAccessException, InvalidTypeIdException {
        SalesTaxRateViewModel s1 = new SalesTaxRateViewModel();
        s1.setState("NY");
        s1.setRate(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP));
        sl.add(SalesTaxRateViewModel.class, s1);

        PurchaseFeeViewModel str1 = sl.find(SalesTaxRateViewModel.class, "NY");
        assertEquals(str1, s1);

        s1 = new SalesTaxRateViewModel();
        s1.setState("HI");
        s1.setRate(new BigDecimal(0.05).setScale(2, RoundingMode.HALF_UP));
        sl.add(SalesTaxRateViewModel.class, s1);

        str1 = sl.find(SalesTaxRateViewModel.class, "HI");
        assertEquals(str1, s1);

        List<?> salesTaxRates= sl.findAll(SalesTaxRateViewModel.class);
        assertEquals(salesTaxRates.size(), 2);
        assertEquals(salesTaxRates.get(1), s1);

        s1 = new SalesTaxRateViewModel();
        s1.setState("FL");
        s1.setRate(new BigDecimal(0.16).setScale(2, RoundingMode.HALF_UP));
        sl.add(SalesTaxRateViewModel.class, s1);

        s1.setRate(new BigDecimal(0.06).setScale(2, RoundingMode.HALF_UP));
        sl.update(SalesTaxRateViewModel.class, s1, "FL");

        str1 = sl.find(SalesTaxRateViewModel.class, "FL");
        assertEquals(str1, s1);

        sl.delete(SalesTaxRateViewModel.class, "ZZ");
        assertNull(sl.find(SalesTaxRateViewModel.class, "ZZ"));
    }

    // Processing Fee: add, find, findAll, update, delete
    @Test
    public void addGetGetAllUpdateDeleteProcessingFees()
            throws InvalidClassException, InstantiationException, IllegalAccessException, InvalidTypeIdException {
        ProcessingFeeViewModel p1 = new ProcessingFeeViewModel();
        p1.setProductType("Consoles");
        p1.setFee(new BigDecimal(14.99).setScale(2, RoundingMode.HALF_UP));
        sl.add(ProcessingFeeViewModel.class, p1);

        PurchaseFeeViewModel p2 = sl.find(ProcessingFeeViewModel.class, "Consoles");
        assertEquals(p2, p1);

        p1.setProductType("T-Shirts");
        p1.setFee(new BigDecimal(1.98).setScale(2, RoundingMode.HALF_UP));
        sl.add(ProcessingFeeViewModel.class, p1);

        p2 = sl.find(ProcessingFeeViewModel.class, "T-Shirts");
        assertEquals(p2, p1);

        p1.setProductType("Games");
        p1.setFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));
        sl.add(ProcessingFeeViewModel.class, p1);

        p2 = sl.find(ProcessingFeeViewModel.class, "Games");
        assertEquals(p2, p1);

        List<?> fees = sl.findAll(ProcessingFeeViewModel.class);
        assertEquals(fees.size(), 3);
        assertEquals(fees.get(2), p2);

        p1.setProductType("GamesTest");
        p1.setFee(new BigDecimal(1.37).setScale(2, RoundingMode.HALF_UP));
        sl.add(ProcessingFeeViewModel.class, p1);

        p1.setProductType("Games");
        p1.setFee(new BigDecimal(1.49).setScale(2, RoundingMode.HALF_UP));
        p2 = sl.find(ProcessingFeeViewModel.class, "Games");
        assertEquals(p2, p1);

        sl.delete(ProcessingFeeViewModel.class, "Candy");
        p2 = sl.find(ProcessingFeeViewModel.class, "Candy");
        assertNull(p2);
    }

    // ItemType == Games: add, find, findAll, update, delete
    @Test
    public void addGetGetAllUpdateDeleteGames()
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        GameViewModel g1 = (GameViewModel) sl.add(game1a);
        Game g2 = (Game) sl.find(ItemType.game, g1.getGameId());

        assertEquals(g2, g1);

        List<ItemViewModel> games = sl.findAll(ItemType.game);
        assertEquals(games.size(), 1);

        games = sl.findBy(ItemType.game, "title", g1.getTitle());
        assertEquals(games.size(), 1);

        games = sl.findBy(ItemType.game, "title", "random");
        assertEquals(games.size(), 0);

        games = sl.findBy(ItemType.game, "studio", g1.getStudio());
        assertEquals(games.size(), 1);

        games = sl.findBy(ItemType.game, "studio", "random");
        assertEquals(games.size(), 0);

        games = sl.findBy(ItemType.game, "esrb+rating", g1.getEsrbRating());
        assertEquals(games.size(), 1);

        games = sl.findBy(ItemType.game, "esrb+rating", "random");
        assertEquals(games.size(), 0);

        g1 = (GameViewModel) sl.add(game2a);
        g1.setQuantity(4L);
        sl.update((ItemViewModel) g1);
        g2 = (Game) sl.find(ItemType.game, g1.getGameId());
        assertEquals(g2, g1);

        sl.delete(ItemType.game, 3L);
        g2 = (Game) sl.find(ItemType.game, 3L);
        assertNull(g2);
    }

    // ItemType == Consoles: add, find, findAll, update, delete
    @Test
    public void addGetGetAllUpdateDeleteConsoles()
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        ConsoleViewModel c1 = (ConsoleViewModel) sl.add(console1a);
        Console c2 = (Console) sl.find("Consoles", c1.getConsoleId());

        assertEquals(c2, c1);

        List<ItemViewModel> consoles = sl.findAll("Consoles");
        assertEquals(consoles.size(), 1);

        consoles = sl.findBy("Consoles", "manufacturer", "Sony");
        assertEquals(consoles.size(), 1);

        consoles = sl.findBy("Consoles", "manufacturer", "Blizzard");
        assertEquals(consoles.size(), 0);

        c1 = (ConsoleViewModel) sl.add(console2a);
        c1.setQuantity(53L);
        sl.update((ItemViewModel) c1);
        c2 = (Console) sl.find("Consoles", c1.getConsoleId());
        assertEquals(c2, c1);

        sl.delete("Consoles", 3L);
        c2 = (Console) sl.find("Consoles", 3L);
        assertNull(c2);
    }

    // ItemType == T-Shirts: add, find, findAll, update, delete
    @Test
    public void addGetGetAllUpdateDeleteTShirts()
            throws InvalidTypeIdException, IllegalAccessException, InstantiationException {
        TShirtViewModel t1 = (TShirtViewModel) sl.add(tShirt1a);
        TShirt t2 = (TShirt) sl.find("T-Shirts", t1.gettShirtId());
        assertEquals(t2, t1);

        List<ItemViewModel> tShirts = sl.findAll("T-Shirts");
        assertEquals(tShirts.size(), 1);

        tShirts = sl.findBy("T-Shirts", "Size", "L");
        assertEquals(tShirts.size(), 1);

        tShirts = sl.findBy("T-Shirts", "Size", "M");
        assertEquals(tShirts.size(), 0);

        tShirts = sl.findBy("T-Shirts", "Color", "Black");
        assertEquals(tShirts.size(), 1);

        tShirts = sl.findBy("T-Shirts", "Size", "Blue");
        assertEquals(tShirts.size(), 0);

        t1 = (TShirtViewModel) sl.add(tShirt2a);
        t1.setDescription("Jumanji");
        t1.setQuantity(20L);
        sl.update((ItemViewModel) t1);
        t2 = (TShirt) sl.find("T-Shirts", t1.gettShirtId());
        assertEquals(t2, t1);

        sl.delete("T-Shirts", 3L);
        t2 = (TShirt) sl.find("T-Shirts", 3L);
        assertNull(t2);
    }

    // PurchaseViewModel: add, get, getAll
    @Test
    public void addGetGetAllPurchaseViewModels()
            throws InvalidTypeIdException, InvalidClassException, InvalidAttributeValueException,
            ServiceUnavailableException, InstantiationException, IllegalAccessException {
        PurchaseViewModel pvm1 = sl.add(purchaseViewModel1);
        PurchaseViewModel pvm2 = (PurchaseViewModel) sl.find(PurchaseViewModel.class, pvm1.getInvoiceId());
        assertEquals(pvm2, pvm1);

        List<?> pvms = sl.findAll(PurchaseViewModel.class);
        assertEquals(pvms.size(), 1);
    }

    // PurchaseViewModel > invalid quantity: expect error
    @Test(expected = IllegalArgumentException.class)
    public void addPurchaseViewModelWithInvalidQuantity() throws InvalidTypeIdException,
            InvalidAttributeValueException, ServiceUnavailableException, InstantiationException,
            IllegalAccessException {
        sl.add(purchaseViewModel4);
    }

    // PurchaseViewModel > invalid stateCode: expect error
    @Test(expected = NoSuchElementException.class)
    public void addPurchaseViewModelWithInvalidStateCode() throws InvalidTypeIdException,
            InvalidAttributeValueException, ServiceUnavailableException, InstantiationException,
            IllegalAccessException {
        sl.add(purchaseViewModel5);
    }

    // PurchaseViewModel > invalid itemType: expect error
    @Test(expected = InvalidTypeIdException.class)
    public void addPurchaseViewModelWithInvalidItemType() throws InvalidTypeIdException,
            InvalidAttributeValueException, ServiceUnavailableException, InstantiationException,
            IllegalAccessException {
        sl.add(purchaseViewModel6);
    }

    // PurchaseViewModel > invalid itemId: expect error
    @Test(expected = NoSuchElementException.class)
    public void addPurchaseViewModelWithInvalidItemId()
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException,
            InstantiationException, IllegalAccessException {
        sl.add(purchaseViewModel7);
    }

    // PurchaseViewModel > invalid total (>999.99): expect error
    @Test(expected = InvalidAttributeValueException.class)
    public void addPurchaseViewModelWithLargeTotal()
            throws InvalidTypeIdException, InvalidAttributeValueException, ServiceUnavailableException,
            InstantiationException, IllegalAccessException {
        sl.add(purchaseViewModel8);
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
        doReturn(1L).when(consoleDao).countId(2L);
    }

    public void setUpGameDaoMock() {
        gameDao = mock(GameJdbcTemplateDaoImpl.class);

        List<Game> games1 = new ArrayList<>();
        games1.add(game1b);

        List<Game> emptyList = new ArrayList<>();

        doReturn(game1b).when(gameDao).add(game1a);
        doReturn(game1b).when(gameDao).find(1L);
        doReturn(games1).when(gameDao).findAll();
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
        doReturn(1L).when(gameDao).countId(2L);
    }

    public void setUpTShirtDaoMock() {
        tShirtDao = mock(TShirtJdbcTemplateDaoImpl.class);

        List<TShirt> tShirts1 = new ArrayList<>();
        tShirts1.add(tShirt1b);

        List<TShirt> emptyList = new ArrayList<>();

        doReturn(tShirt1b).when(tShirtDao).add(tShirt1a);
        doReturn(tShirt1b).when(tShirtDao).find(1L);
        doReturn(tShirts1).when(tShirtDao).findAll();
        doReturn(tShirts1).when(tShirtDao).findAllBySize("L");
        doReturn(tShirts1).when(tShirtDao).findAllByColor("Black");
        doReturn(emptyList).when(tShirtDao).findAllBySize("M");
        doReturn(emptyList).when(tShirtDao).findAllByColor("Blue");
        doReturn(tShirt2b).when(tShirtDao).add(tShirt2a);
        doNothing().when(tShirtDao).update(tShirt2c);
        doReturn(tShirt2c).when(tShirtDao).find(2L);
        doNothing().when(tShirtDao).delete(3L);
        doReturn(null).when(tShirtDao).find(3L);
        doReturn(1L).when(tShirtDao).countId(2L);
    }

    public void setUpProcessingFeeDaoMock() {
        processingFeeDao = mock(ProcessingFeeJdbcTemplateDaoImpl.class);

        List<ProcessingFee> processingFees1 = new ArrayList<>();
        processingFees1.add(processingFee1);
        processingFees1.add(processingFee2);
        processingFees1.add(processingFee3);

        doReturn(processingFee1).when(processingFeeDao).add(processingFee1);
        doReturn(processingFee2).when(processingFeeDao).add(processingFee2);
        doReturn(processingFee3).when(processingFeeDao).add(processingFee3);
        doReturn(processingFee1).when(processingFeeDao).find("Consoles");
        doReturn(processingFee3).when(processingFeeDao).find("Games");
        doReturn(processingFee2).when(processingFeeDao).find("T-Shirts");
        doReturn(processingFees1).when(processingFeeDao).findAll();
        doReturn(processingFee4a).when(processingFeeDao).add(processingFee4a);
        doNothing().when(processingFeeDao).update(processingFee4b, "GamesTest");
        doReturn(null).when(processingFeeDao).find("Xbox");
        doNothing().when(processingFeeDao).delete("Candy");
        doReturn(null).when(processingFeeDao).find("Candy");
    }

    public void setUpSalesTaxRateDaoMock() {
        salesTaxRateDao = mock(SalesTaxRateJdbcTemplateDaoImpl.class);

        List<SalesTaxRate> salesTaxRates1 = new ArrayList<>();
        salesTaxRates1.add(salesTaxRate1);
        salesTaxRates1.add(salesTaxRate2);

        doReturn(salesTaxRate1).when(salesTaxRateDao).add(salesTaxRate1);
        doReturn(salesTaxRate2).when(salesTaxRateDao).add(salesTaxRate2);
        doReturn(salesTaxRate1).when(salesTaxRateDao).find("NY");
        doReturn(salesTaxRate2).when(salesTaxRateDao).find("HI");
        doReturn(salesTaxRates1).when(salesTaxRateDao).findAll();
        doReturn(salesTaxRate3a).when(salesTaxRateDao).add(salesTaxRate3a);
        doNothing().when(salesTaxRateDao).update(salesTaxRate3b, "FL");
        doReturn(salesTaxRate3b).when(salesTaxRateDao).find("FL");
        doNothing().when(processingFeeDao).delete("ZZ");
        doReturn(null).when(processingFeeDao).find("ZZ");
    }

    public void setUpInvoiceDaoMock() {
        invoiceDao = mock(InvoiceJdbcTemplateDaoImpl.class);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice1b);

        doReturn(invoice1b).when(invoiceDao).add(invoice1a);
        doReturn(invoice1b).when(invoiceDao).find(1L);
        doReturn(invoice2b).when(invoiceDao).add(invoice2a);
        doReturn(invoice2b).when(invoiceDao).find(2L);
        doReturn(invoice3b).when(invoiceDao).add(invoice3a);
        doReturn(invoice3b).when(invoiceDao).find(3L);
        doReturn(invoices).when(invoiceDao).findAll();
        doReturn(null).when(invoiceDao).find(4L);
    }

    public void constructSampleData() {
        // add, get
        game1a = new GameViewModel();
        game1b = new Game();
        game1bVM = new GameViewModel();
        console1a = new ConsoleViewModel();
        console1b = new Console();
        tShirt1a = new TShirtViewModel();
        tShirt1b = new TShirt();
        processingFee1 = new ProcessingFee();
        processingFee2 = new ProcessingFee();
        processingFee3 = new ProcessingFee();
        salesTaxRate1 = new SalesTaxRate();
        salesTaxRate2 = new SalesTaxRate();
        // add, get Console
        invoice1a = new Invoice();
        invoice1b = new Invoice();
        // add, get Game
        invoice2a = new Invoice();
        invoice2b = new Invoice();
        // add, get TShirt
        invoice3a = new Invoice();
        invoice3b = new Invoice();

        // update
        game2a = new GameViewModel();
        game2b = new Game();
        game2c = new Game();
        console2a = new ConsoleViewModel();
        console2b = new Console();
        console2c = new Console();
        tShirt2a = new TShirtViewModel();
        tShirt2b = new TShirt();
        tShirt2c = new TShirt();
        processingFee4a = new ProcessingFee();
        processingFee4b = new ProcessingFee();
        salesTaxRate3a = new SalesTaxRate();
        salesTaxRate3b = new SalesTaxRate();

        // add, get User purchase
        purchaseViewModel1 = new PurchaseViewModel();
        purchaseViewModel2 = new PurchaseViewModel();
        purchaseViewModel3 = new PurchaseViewModel();

        // user purchase with invalid quantity
        purchaseViewModel4 = new PurchaseViewModel();

        // user purchase with invalid stateCode
        purchaseViewModel5 = new PurchaseViewModel();

        // user purchase with invalid itemType
        purchaseViewModel6 = new PurchaseViewModel();

        // user purchase with invalid ItemId
        purchaseViewModel7 = new PurchaseViewModel();

        // user purchase with invalid ItemId
        purchaseViewModel8 = new PurchaseViewModel();

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

        game1bVM.setGameId(1L);
        game1bVM.setTitle("Final Fantasy VIII");
        game1bVM.setEsrbRating("A+");
        game1bVM.setDescription("rpg");
        game1bVM.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game1bVM.setStudio("Square Enix");
        game1bVM.setQuantity(15L);

        game2a.setTitle("Final Fantasy XV");
        game2a.setEsrbRating("A+");
        game2a.setDescription("rpg");
        game2a.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2a.setStudio("Square Enix");
        game2a.setQuantity(15L);

        game2b.setGameId(2L);
        game2b.setTitle("Final Fantasy XV");
        game2b.setEsrbRating("A+");
        game2b.setDescription("rpg");
        game2b.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2b.setStudio("Square Enix");
        game2b.setQuantity(15L);

        game2c.setGameId(2L);
        game2c.setTitle("Final Fantasy XV");
        game2c.setEsrbRating("A+");
        game2c.setDescription("rpg");
        game2c.setPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        game2c.setStudio("Square Enix");
        game2c.setQuantity(4L);

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

        console2a.setModel("Xbox");
        console2a.setManufacturer("Microsoft");
        console2a.setMemoryAmount("10TB");
        console2a.setProcessor("1.6 GHz");
        console2a.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2a.setQuantity(55L);

        console2b.setConsoleId(2L);
        console2b.setModel("Xbox");
        console2b.setManufacturer("Microsoft");
        console2b.setMemoryAmount("10TB");
        console2b.setProcessor("1.6 GHz");
        console2b.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2b.setQuantity(55L);

        console2c.setConsoleId(2L);
        console2c.setModel("Xbox");
        console2c.setManufacturer("Microsoft");
        console2c.setMemoryAmount("10TB");
        console2c.setProcessor("1.6 GHz");
        console2c.setPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        console2c.setQuantity(53L);

        tShirt1a.setSize("L");
        tShirt1a.setColor("Black");
        tShirt1a.setDescription("Game of Thrones");
        tShirt1a.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt1a.setQuantity(25L);

        tShirt1b.settShirtId(1L);
        tShirt1b.setSize("L");
        tShirt1b.setColor("Black");
        tShirt1b.setDescription("Game of Thrones");
        tShirt1b.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt1b.setQuantity(25L);

        tShirt2a.setSize("L");
        tShirt2a.setColor("Navy");
        tShirt2a.setDescription("Game of Thrones");
        tShirt2a.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt2a.setQuantity(25L);

        tShirt2b.settShirtId(2L);
        tShirt2b.setSize("L");
        tShirt2b.setColor("Navy");
        tShirt2b.setDescription("Game of Thrones");
        tShirt2b.setPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        tShirt2b.setQuantity(25L);

        tShirt2c.settShirtId(2L);
        tShirt2c.setSize("L");
        tShirt2c.setColor("Navy");
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
        invoice1a.setQuantity(2L);
        invoice1a.setUnitPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setSubtotal(new BigDecimal(411.98).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setTax(new BigDecimal(20.60).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setProcessingFee(new BigDecimal(14.99).setScale(2, RoundingMode.HALF_UP));
        invoice1a.setTotal(new BigDecimal(447.57).setScale(2, RoundingMode.HALF_UP));

        invoice1b.setInvoiceId(1L);
        invoice1b.setName("rene");
        invoice1b.setStreet("23rd st");
        invoice1b.setCity("NYC");
        invoice1b.setState("NY");
        invoice1b.setZipcode("10019");
        invoice1b.setItemType("Consoles");
        invoice1b.setItemId(2L);
        invoice1b.setQuantity(2L);
        invoice1b.setUnitPrice(new BigDecimal(205.99).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setSubtotal(new BigDecimal(411.98).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setTax(new BigDecimal(20.60).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setProcessingFee(new BigDecimal(14.99).setScale(2, RoundingMode.HALF_UP));
        invoice1b.setTotal(new BigDecimal(447.57).setScale(2, RoundingMode.HALF_UP));

        invoice2a.setName("rene");
        invoice2a.setStreet("23rd st");
        invoice2a.setCity("NYC");
        invoice2a.setState("NY");
        invoice2a.setZipcode("10019");
        invoice2a.setItemType("Games");
        invoice2a.setItemId(2L);
        invoice2a.setQuantity(11L);
        invoice2a.setUnitPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setSubtotal(new BigDecimal(285.89).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setTax(new BigDecimal(14.29).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setProcessingFee(new BigDecimal(16.98).setScale(2, RoundingMode.HALF_UP));
        invoice2a.setTotal(new BigDecimal(317.16).setScale(2, RoundingMode.HALF_UP));

        invoice2b.setInvoiceId(2L);
        invoice2b.setName("rene");
        invoice2b.setStreet("23rd st");
        invoice2b.setCity("NYC");
        invoice2b.setState("NY");
        invoice2b.setZipcode("10019");
        invoice2b.setItemType("Games");
        invoice2b.setItemId(2L);
        invoice2b.setQuantity(11L);
        invoice2b.setUnitPrice(new BigDecimal(25.99).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setSubtotal(new BigDecimal(285.89).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setTax(new BigDecimal(14.29).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setProcessingFee(new BigDecimal(16.98).setScale(2, RoundingMode.HALF_UP));
        invoice2b.setTotal(new BigDecimal(317.16).setScale(2, RoundingMode.HALF_UP));

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
        invoice3a.setName("rene");
        invoice3a.setStreet("Volcano Road");
        invoice3a.setCity("Honolulu");
        invoice3a.setState("HI");
        invoice3a.setZipcode("10019");
        invoice3a.setItemType("T-Shirts");
        invoice3a.setItemId(2L);
        invoice3a.setQuantity(5L);
        invoice3b.setUnitPrice(new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setSubtotal(new BigDecimal(99.95).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setTax(new BigDecimal(5.00).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setProcessingFee(new BigDecimal(1.98).setScale(2, RoundingMode.HALF_UP));
        invoice3b.setTotal(new BigDecimal(106.93).setScale(2, RoundingMode.HALF_UP));

        purchaseViewModel1.setName("rene");
        purchaseViewModel1.setStreet("23rd st");
        purchaseViewModel1.setCity("NYC");
        purchaseViewModel1.setState("NY");
        purchaseViewModel1.setZipcode("10019");
        purchaseViewModel1.setItemType("Consoles");
        purchaseViewModel1.setItemId(2L);
        purchaseViewModel1.setQuantity(2L);

        purchaseViewModel2.setName("rene");
        purchaseViewModel2.setStreet("23rd st");
        purchaseViewModel2.setCity("NYC");
        purchaseViewModel2.setState("NY");
        purchaseViewModel2.setZipcode("10019");
        purchaseViewModel2.setItemType("Games");
        purchaseViewModel2.setItemId(2L);
        purchaseViewModel2.setQuantity(11L);

        purchaseViewModel3.setName("rene");
        purchaseViewModel3.setStreet("Volcano Road");
        purchaseViewModel3.setCity("Honolulu");
        purchaseViewModel3.setState("HI");
        purchaseViewModel3.setZipcode("10019");
        purchaseViewModel3.setItemType("T-Shirts");
        purchaseViewModel3.setItemId(2L);
        purchaseViewModel3.setQuantity(5L);

        purchaseViewModel4.setName("rene");
        purchaseViewModel4.setStreet("Volcano Road");
        purchaseViewModel4.setCity("Honolulu");
        purchaseViewModel4.setState("HI");
        purchaseViewModel4.setZipcode("10019");
        purchaseViewModel4.setItemType("T-Shirts");
        purchaseViewModel4.setItemId(2L);
        purchaseViewModel4.setQuantity(100L);

        purchaseViewModel5.setName("rene");
        purchaseViewModel5.setStreet("Volcano Road");
        purchaseViewModel5.setCity("Honolulu");
        purchaseViewModel5.setState("ZZ");
        purchaseViewModel5.setZipcode("10019");
        purchaseViewModel5.setItemType("T-Shirts");
        purchaseViewModel5.setItemId(2L);
        purchaseViewModel5.setQuantity(5L);

        purchaseViewModel6.setName("rene");
        purchaseViewModel6.setStreet("Volcano Road");
        purchaseViewModel6.setCity("Honolulu");
        purchaseViewModel6.setState("HI");
        purchaseViewModel6.setZipcode("10019");
        purchaseViewModel6.setItemType("Xbox");
        purchaseViewModel6.setItemId(2L);
        purchaseViewModel6.setQuantity(5L);

        purchaseViewModel7.setName("rene");
        purchaseViewModel7.setStreet("Volcano Road");
        purchaseViewModel7.setCity("Honolulu");
        purchaseViewModel7.setState("HI");
        purchaseViewModel7.setZipcode("10019");
        purchaseViewModel7.setItemType("T-Shirts");
        purchaseViewModel7.setItemId(3L);
        purchaseViewModel7.setQuantity(5L);

        purchaseViewModel8.setName("rene");
        purchaseViewModel8.setStreet("23rd st");
        purchaseViewModel8.setCity("NYC");
        purchaseViewModel8.setState("NY");
        purchaseViewModel8.setZipcode("10019");
        purchaseViewModel8.setItemType("Consoles");
        purchaseViewModel8.setItemId(2L);
        purchaseViewModel8.setQuantity(5L);
    }
}