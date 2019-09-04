package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Console;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ConsoleDao {
    // CRUD
    Console add(Console console);
    Console find(Long consoleId);
    List<Console> findAll();
    void update(Console console);
    void delete(Long consoleId);

    List<Console> findAllByManufacturer(String manufacturer);

    Console mapRowToConsole(ResultSet rs, int rowNum) throws SQLException;
}
