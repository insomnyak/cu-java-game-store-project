package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Invoice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface InvoiceDao {
    // CRUD
    Invoice add(Invoice invoice);
    Invoice find(Long invoiceId);
    List<Invoice> findAll();
    void update(Invoice invoice);
    void delete(Long invoiceId);

    Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException;
}
