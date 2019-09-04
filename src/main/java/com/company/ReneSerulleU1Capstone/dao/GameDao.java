package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GameDao {
    // CRUD
    Game add(Game game);
    Game find(Long gameId);
    List<Game> findAll();
    void update(Game game);
    void delete(Long gameId);

    List<Game> findAllByStudio(String studio);
    List<Game> findAllByEsrbRating(String esrdRating);
    List<Game> findAllByTitle(String title);

    Game mapRowToGame(ResultSet rs, int rowNum) throws SQLException;
}
