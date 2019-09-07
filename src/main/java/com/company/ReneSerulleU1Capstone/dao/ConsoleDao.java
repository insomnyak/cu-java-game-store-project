package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Console;
import com.company.ReneSerulleU1Capstone.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ConsoleDao extends ItemDao<Console> {

    List<Console> findAllByManufacturer(String manufacturer);
    Console mapRowToConsole(ResultSet rs, int rowNum) throws SQLException;

}
