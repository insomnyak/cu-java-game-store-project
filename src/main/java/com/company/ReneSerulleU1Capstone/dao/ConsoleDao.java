package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Console;
import com.company.ReneSerulleU1Capstone.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ConsoleDao extends ItemDao<Console> {
    // CRUD

    @Override
    Console add(Console console);

    @Override
    Console find(Long consoleId);

    @Override
    List<Console> findAll();

    @Override
    void update(Console console);

    @Override
    void delete(Long consoleId);

    List<Console> findAllByManufacturer(String manufacturer);

    Console mapRowToConsole(ResultSet rs, int rowNum) throws SQLException;
}
