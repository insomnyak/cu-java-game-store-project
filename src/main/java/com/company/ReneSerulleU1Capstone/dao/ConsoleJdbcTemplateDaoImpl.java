package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ConsoleJdbcTemplateDaoImpl implements ConsoleDao {
    // prepared statement strings
    private final String INSERT_CONSOLE_SQL =
            "insert into console (model, manufacturer, memory_amount, processor, price, quantity) values (?,?,?,?,?,?)";
    private final String UPDATE_CONSOLE_SQL =
            "update console set model = ?, manufacturer = ?, memory_amount = ?, " +
                    "processor = ?, price = ?, quantity = ? where console_id = ?";
    private final String SELECT_CONSOLE_SQL =
            "select * from console where console_id = ?";
    private final String SELECT_ALL_CONSOLES_SQL =
            "select * from console";
    private final String DELETE_CONSOLE_SQL =
            "delete from console where console_id = ?";
    private final String SELECT_ALL_CONSOLES_BY_MANUFACTURER_SQL =
            "select * from console where manufacturer = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ConsoleJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Console add(Console console) {
        jdbcTemplate.update(INSERT_CONSOLE_SQL,
                console.getModel(),
                console.getManufacturer(),
                console.getMemoryAmount(),
                console.getProcessor(),
                console.getPrice(),
                console.getQuantity());
        Long id = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        console.setConsoleId(id);
        return console;
    }

    @Override
    public Console find(Long consoleId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_CONSOLE_SQL, this::mapRowToConsole, consoleId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Console> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CONSOLES_SQL, this::mapRowToConsole);
    }

    @Override
    public void update(Console console) {
        jdbcTemplate.update(UPDATE_CONSOLE_SQL,
                console.getModel(),
                console.getManufacturer(),
                console.getMemoryAmount(),
                console.getProcessor(),
                console.getPrice(),
                console.getQuantity(),
                console.getConsoleId());
    }

    @Override
    public void delete(Long consoleId) {
        jdbcTemplate.update(DELETE_CONSOLE_SQL, consoleId);
    }

    @Override
    public List<Console> findAllByManufacturer(String manufacturer) {
        return jdbcTemplate.query(SELECT_ALL_CONSOLES_BY_MANUFACTURER_SQL, this::mapRowToConsole, manufacturer);
    }

    @Override
    public Console mapRowToConsole(ResultSet rs, int rowNum) throws SQLException {
        Console console = new Console() {{
            setConsoleId(rs.getLong("console_id"));
            setModel(rs.getString("model"));
            setManufacturer(rs.getString("manufacturer"));
            setMemoryAmount(rs.getString("memory_amount"));
            setProcessor(rs.getString("processor"));
            setPrice(rs.getBigDecimal("price"));
            setQuantity(rs.getLong("quantity"));
        }};
        return console;
    }
}
