package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.TShirt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TShirtJdbcTemplateDaoImpl implements TShirtDao {
    // prepared statement strings
    private final String INSERT_T_SHIRT_SQL =
            "insert into t_shirt (size, color, description, price, quantity) values (?,?,?,?,?)";
    private final String UPDATE_T_SHIRT_SQL =
            "update t_shirt set size = ?, color = ?, description = ?, price = ?, quantity = ? where t_shirt_id = ?";
    private final String SELECT_T_SHIRT_SQL =
            "select * from t_shirt where t_shirt_id = ?";
    private final String SELECT_ALL_T_SHIRTS_SQL =
            "select * from t_shirt";
    private final String DELETE_T_SHIRT_SQL =
            "delete from t_shirt where t_shirt_id = ?";
    private final String SELECT_ALL_T_SHIRTS_BY_COLOR_SQL =
            "select * from t_shirt where color = ?";
    private final String SELECT_ALL_T_SHIRTS_BY_SIZE_SQL =
            "select * from t_shirt where size = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public TShirtJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public TShirt add(TShirt tShirt) {
        jdbcTemplate.update(INSERT_T_SHIRT_SQL,
                tShirt.getSize(),
                tShirt.getColor(),
                tShirt.getDescription(),
                tShirt.getPrice(),
                tShirt.getQuantity());
        Long id = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        tShirt.setTShirtId(id);
        return tShirt;
    }

    @Override
    public TShirt find(Long tShirtId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_T_SHIRT_SQL, this::mapRowToTShirt, tShirtId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TShirt> findAll() {
        return jdbcTemplate.query(SELECT_ALL_T_SHIRTS_SQL, this::mapRowToTShirt);
    }

    @Override
    public void update(TShirt tShirt) {
        jdbcTemplate.update(UPDATE_T_SHIRT_SQL,
                tShirt.getSize(),
                tShirt.getColor(),
                tShirt.getDescription(),
                tShirt.getPrice(),
                tShirt.getQuantity(),
                tShirt.getTShirtId());
    }

    @Override
    public void delete(Long tShirtId) {
        jdbcTemplate.update(DELETE_T_SHIRT_SQL, tShirtId);
    }

    @Override
    public List<TShirt> findAllByColor(String color) {
        return jdbcTemplate.query(SELECT_ALL_T_SHIRTS_BY_COLOR_SQL, this::mapRowToTShirt, color);
    }

    @Override
    public List<TShirt> findAllBySize(String size) {
        return jdbcTemplate.query(SELECT_ALL_T_SHIRTS_BY_SIZE_SQL, this::mapRowToTShirt, size);
    }

    @Override
    public TShirt mapRowToTShirt(ResultSet rs, int rowNum) throws SQLException {
        TShirt t = new TShirt() {{
            setTShirtId(rs.getLong("t_shirt_id"));
            setSize(rs.getString("size"));
            setColor(rs.getString("color"));
            setDescription(rs.getString("description"));
            setPrice(rs.getBigDecimal("price"));
            setQuantity(rs.getLong("quantity"));
        }};
        return t;
    }
}
