package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.SalesTaxRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SalesTaxRateJdbcTemplateDaoImpl implements SalesTaxRateDao{
    // prepared statement strings
    private final String INSERT_SALES_TAX_RATE_SQL =
            "insert into sales_tax_rate (state, rate) values (?,?)";
    private final String UPDATE_SALES_TAX_RATE_SQL =
            "update sales_tax_rate set state = ?, rate = ? where state = ? limit 1";
    private final String SELECT_SALES_TAX_RATE_SQL =
            "select * from sales_tax_rate where state = ?";
    private final String SELECT_ALL_SALES_TAX_RATES_SQL =
            "select * from sales_tax_rate";
    private final String DELETE_SALES_TAX_RATE_SQL =
            "delete from sales_tax_rate where state = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public SalesTaxRateJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public SalesTaxRate add(SalesTaxRate salesTaxRate) {
        jdbcTemplate.update(INSERT_SALES_TAX_RATE_SQL,
                salesTaxRate.getState(),
                salesTaxRate.getRate());
        return salesTaxRate;
    }

    @Override
    public SalesTaxRate find(String state) {
        try {
            return jdbcTemplate.queryForObject(SELECT_SALES_TAX_RATE_SQL, this::mapRowToSalesTaxRate, state);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<SalesTaxRate> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SALES_TAX_RATES_SQL, this::mapRowToSalesTaxRate);
    }

    @Override
    public void update(SalesTaxRate salesTaxRate, String stateCode) {
        jdbcTemplate.update(UPDATE_SALES_TAX_RATE_SQL,
                salesTaxRate.getState(),
                salesTaxRate.getRate(),
                stateCode);
    }

    @Override
    public void delete(String stateCode) {
        jdbcTemplate.update(DELETE_SALES_TAX_RATE_SQL, stateCode);
    }

    @Override
    public SalesTaxRate mapRowToSalesTaxRate(ResultSet rs, int rowNum) throws SQLException {
        SalesTaxRate s = new SalesTaxRate() {{
            setState(rs.getString("state"));
            setRate(rs.getBigDecimal("rate"));
        }};
        return s;
    }
}
