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
public class GameJdbcTemplateDaoImplTest {

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
        List<Game> games = gameDao.findAll();
        games.forEach(game -> gameDao.delete(game.getGameId()));
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {
        Game game1 = new Game() {{
            setTitle("Final Fantasy XV");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(61.99));
            setStudio("games");
            setQuantity(12L);
        }};
        gameDao.add(game1);

        game1 = new Game() {{
            setTitle("Final Fantasy VIII");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(45.99));
            setStudio("games");
            setQuantity(1L);
        }};
        gameDao.add(game1);

        List<Game> games = gameDao.findAll();
        assertEquals(games.size(), 2);
    }

    @Test
    public void update() {
        Game game1 = new Game() {{
            setTitle("Final Fantasy XV");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(61.99).setScale(2, RoundingMode.HALF_UP));
            setStudio("games");
            setQuantity(12L);
        }};
        gameDao.add(game1);

        game1.setQuantity(8L);
        game1.setDescription("testing");

        gameDao.update(game1);

        Game game2 = gameDao.find(game1.getGameId());
        assertEquals(game2, game1);
    }

    @Test
    public void findAllByStudioEsrbRatingTitle() {
        Game game1 = new Game() {{
            setTitle("Final Fantasy XV");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(61.99).setScale(2, RoundingMode.HALF_UP));
            setStudio("games");
            setQuantity(12L);
        }};
        gameDao.add(game1);

        game1 = new Game() {{
            setTitle("Final Fantasy VIII");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(45.99).setScale(2, RoundingMode.HALF_UP));
            setStudio("games");
            setQuantity(1L);
        }};
        gameDao.add(game1);

        // by EsrbRating
        List<Game> games = gameDao.findAllByEsrbRating("A+");
        assertEquals(games.size(), 2);

        games = gameDao.findAllByEsrbRating("B+");
        assertEquals(games.size(), 0);

        // by Studio
        games = gameDao.findAllByStudio("games");
        assertEquals(games.size(), 2);

        games = gameDao.findAllByStudio("something else");
        assertEquals(games.size(), 0);

        // by Title
        games = gameDao.findAllByTitle("Final Fantasy XV");
        assertEquals(games.size(), 1);

        games = gameDao.findAllByEsrbRating("Mario Brothers");
        assertEquals(games.size(), 0);
    }

    @Test
    public void addGetDelete() {
        Game game1 = new Game() {{
            setTitle("Final Fantasy XV");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(61.99).setScale(2, RoundingMode.HALF_UP));
            setStudio("games");
            setQuantity(12L);
        }};
        gameDao.add(game1);

        // Test get
        Game game2 = gameDao.find(game1.getGameId());
        assertEquals(game2, game1);

        // Test delete
        gameDao.delete(game1.getGameId());
        game2 = gameDao.find(game1.getGameId());
        assertNull(game2);
    }

    @Test
    public void dbConstraintViolations() {
        // (expected = DataIntegrityViolationException.class)
        Game game1 = new Game() {{
            setTitle("Final Fantasy XV");
            setEsrbRating("A+");
            setDescription("rpg");
            setPrice(new BigDecimal(61.99));
            setStudio("games");
            setQuantity(12L);
        }};

        try {
            game1 = new Game() {{
                setTitle("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setEsrbRating("A+");
                setDescription("rpg");
                setPrice(new BigDecimal(61.99));
                setStudio("games");
                setQuantity(12L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            game1 = new Game() {{
                setTitle("Final Fantasy XV");
                setEsrbRating("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setDescription("rpg");
                setPrice(new BigDecimal(61.99));
                setStudio("games");
                setQuantity(12L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            game1 = new Game() {{
                setTitle("Final Fantasy XV");
                setEsrbRating("A+");
                setDescription("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setPrice(new BigDecimal(61.99));
                setStudio("games");
                setQuantity(12L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            game1 = new Game() {{
                setTitle("Final Fantasy XV");
                setEsrbRating("A+");
                setDescription("rpg");
                setPrice(new BigDecimal(61.99));
                setStudio("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setQuantity(12L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            game1 = new Game() {{
                setTitle("Final Fantasy XV");
                setEsrbRating("A+");
                setDescription("rpg");
                setPrice(new BigDecimal(100000.123));
                setStudio("games");
                setQuantity(12L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        try {
            game1 = new Game() {{
                setTitle("Final Fantasy XV");
                setEsrbRating("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setDescription("rpg");
                setPrice(new BigDecimal(61.99));
                setStudio("games");
                setQuantity(123456789012L);
            }};
            gameDao.add(game1);
        } catch (DataIntegrityViolationException ignore) {}

        List<Game> games = gameDao.findAll();
        assertEquals(games.size(), 0);
    }
}