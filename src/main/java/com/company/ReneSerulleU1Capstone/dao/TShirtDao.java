package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.TShirt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TShirtDao extends ItemDao<TShirt> {

    List<TShirt> findAllByColor(String color);
    List<TShirt> findAllBySize(String size);

    TShirt mapRowToTShirt(ResultSet rs, int rowNum) throws SQLException;

}
