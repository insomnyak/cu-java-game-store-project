package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.SalesTaxRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface SalesTaxRateDao {
    // CRUD
    SalesTaxRate add(SalesTaxRate salesTaxRate);
    SalesTaxRate find(String state);
    List<SalesTaxRate> findAll();
    void update(SalesTaxRate salesTaxRate, String stateCode);
    void delete(String stateCode);

    SalesTaxRate mapRowToSalesTaxRate(ResultSet rs, int rowNum) throws SQLException;
}
