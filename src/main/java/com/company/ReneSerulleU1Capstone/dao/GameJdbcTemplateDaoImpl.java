package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Game;
import com.company.ReneSerulleU1Capstone.model.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GameJdbcTemplateDaoImpl implements GameDao {
    // prepared statement strings
    private final String INSERT_GAME_SQL =
            "insert into game (title, esrb_rating, description, price, studio, quantity) values (?,?,?,?,?,?)";
    private final String UPDATE_GAME_SQL =
            "update game set title = ?, esrb_rating = ?, description = ?, " +
                    "price = ?, studio = ?, quantity = ? where game_id = ?";
    private final String SELECT_GAME_SQL =
            "select * from game where game_id = ?";
    private final String SELECT_ALL_GAMES_SQL =
            "select * from game";
    private final String DELETE_GAME_SQL =
            "delete from game where game_id = ?";
    private final String SELECT_ALL_GAMES_BY_STUDIO_SQL =
            "select * from game where studio = ?";
    private final String SELECT_ALL_GAMES_BY_ESRB_RATING_SQL =
            "select * from game where esrb_rating = ?";
    private final String SELECT_ALL_GAMES_BY_TITLE_SQL =
            "select * from game where title = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public GameJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Game add(Game game) {
        jdbcTemplate.update(INSERT_GAME_SQL,
                game.getTitle(),
                game.getEsrbRating(),
                game.getDescription(),
                game.getPrice(),
                game.getStudio(),
                game.getQuantity());
        Long id = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        game.setGameId(id);
        return game;
    }

    @Override
    public Game find(Long gameId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_GAME_SQL, this::mapRowToGame, gameId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Game> findAll() {
        return jdbcTemplate.query(SELECT_ALL_GAMES_SQL, this::mapRowToGame);
    }

    @Override
    public void update(Game game) {
        jdbcTemplate.update(UPDATE_GAME_SQL,
                game.getTitle(),
                game.getEsrbRating(),
                game.getDescription(),
                game.getPrice(),
                game.getStudio(),
                game.getQuantity(),
                game.getGameId());
    }

    @Override
    public void delete(Long gameId) {
        jdbcTemplate.update(DELETE_GAME_SQL, gameId);
    }

    @Override
    public List<Game> findAllByStudio(String studio) {
        return jdbcTemplate.query(SELECT_ALL_GAMES_BY_STUDIO_SQL, this::mapRowToGame, studio);
    }

    @Override
    public List<Game> findAllByEsrbRating(String esrdRating) {
        return jdbcTemplate.query(SELECT_ALL_GAMES_BY_ESRB_RATING_SQL, this::mapRowToGame, esrdRating);
    }

    @Override
    public List<Game> findAllByTitle(String title) {
        return jdbcTemplate.query(SELECT_ALL_GAMES_BY_TITLE_SQL, this::mapRowToGame, title);
    }

    @Override
    public Game mapRowToGame(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game() {{
            setGameId(rs.getLong("game_id"));
            setTitle(rs.getString("title"));
            setEsrbRating(rs.getString("esrb_rating"));
            setDescription(rs.getString("description"));
            setPrice(rs.getBigDecimal("price"));
            setStudio(rs.getString("studio"));
            setQuantity(rs.getLong("quantity"));
        }};
        return game;
    }
}
