package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceJdbcTemplateDaoImpl implements InvoiceDao {
    // prepared statement strings
    private final String INSERT_INVOICE_SQL =
            "insert into invoice (name, street, city, state, zipcode," +
                    "item_type, item_id, unit_price, quantity, subtotal," +
                    "tax, processing_fee, total) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String UPDATE_INVOICE_SQL =
            "update invoice set name = ?, street = ?, city = ?, state = ?, zipcode = ?," +
                    "item_type = ?, item_id = ?, unit_price = ?, quantity = ?, subtotal = ?," +
                    "tax = ?, processing_fee = ?, total = ? where invoice_id = ?";
    private final String SELECT_INVOICE_SQL =
            "select * from invoice where invoice_id = ?";
    private final String SELECT_ALL_INVOICES_SQL =
            "select * from invoice";
    private final String DELETE_INVOICE_SQL =
            "delete from invoice where invoice_id = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public InvoiceJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Invoice add(Invoice invoice) {
        jdbcTemplate.update(INSERT_INVOICE_SQL,
                invoice.getName(),
                invoice.getStreet(),
                invoice.getCity(),
                invoice.getState(),
                invoice.getZipcode(),
                invoice.getItemType(),
                invoice.getItemId(),
                invoice.getUnitPrice(),
                invoice.getQuantity(),
                invoice.getSubtotal(),
                invoice.getTax(),
                invoice.getProcessingFee(),
                invoice.getTotal());
        Long id = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        invoice.setInvoiceId(id);
        return invoice;
    }

    @Override
    public Invoice find(Long invoiceId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_INVOICE_SQL, this::mapRowToInvoice, invoiceId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Invoice> findAll() {
        return jdbcTemplate.query(SELECT_ALL_INVOICES_SQL, this::mapRowToInvoice);
    }

    @Override
    public void update(Invoice invoice) {
        jdbcTemplate.update(UPDATE_INVOICE_SQL,
                invoice.getName(),
                invoice.getStreet(),
                invoice.getCity(),
                invoice.getState(),
                invoice.getZipcode(),
                invoice.getItemType(),
                invoice.getItemId(),
                invoice.getUnitPrice(),
                invoice.getQuantity(),
                invoice.getSubtotal(),
                invoice.getTax(),
                invoice.getProcessingFee(),
                invoice.getTotal(),
                invoice.getInvoiceId());
    }

    @Override
    public void delete(Long invoiceId) {
        jdbcTemplate.update(DELETE_INVOICE_SQL, invoiceId);
    }

    @Override
    public Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice() {{
            setInvoiceId(rs.getLong("invoice_id"));
            setName(rs.getString("name"));
            setStreet(rs.getString("street"));
            setCity(rs.getString("city"));
            setState(rs.getString("state"));
            setZipcode(rs.getString("zipcode"));
            setItemType(rs.getString("item_type"));
            setItemId(rs.getLong("item_id"));
            setUnitPrice(rs.getBigDecimal("unit_price"));
            setQuantity(rs.getLong("quantity"));
            setSubtotal(rs.getBigDecimal("subtotal"));
            setTax(rs.getBigDecimal("tax"));
            setProcessingFee(rs.getBigDecimal("processing_fee"));
            setTotal(rs.getBigDecimal("total"));
        }};
        return invoice;
    }
}
