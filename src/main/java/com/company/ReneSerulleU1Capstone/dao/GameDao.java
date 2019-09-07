package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GameDao extends ItemDao<Game> {

    List<Game> findAllByStudio(String studio);
    List<Game> findAllByEsrbRating(String esrdRating);
    List<Game> findAllByTitle(String title);

    Game mapRowToGame(ResultSet rs, int rowNum) throws SQLException;

}
