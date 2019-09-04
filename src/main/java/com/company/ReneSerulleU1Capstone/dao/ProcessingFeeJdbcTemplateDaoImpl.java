package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.ProcessingFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProcessingFeeJdbcTemplateDaoImpl implements ProcessingFeeDao {
    // prepared statement strings
    private final String INSERT_PROCESSING_FEE_SQL =
            "insert into processing_fee (product_type, fee) values (?,?)";
    private final String UPDATE_PROCESSING_FEE_SQL =
            "update processing_fee set product_type = ?, fee = ? where product_type = ? and fee = ?";
    private final String SELECT_PROCESSING_FEE_SQL =
            "select * from processing_fee where product_type = ?";
    private final String SELECT_ALL_PROCESSING_FEES_SQL =
            "select * from processing_fee";
    private final String DELETE_PROCESSING_FEE_SQL =
            "delete from processing_fee where product_type = ? and fee = ?";

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ProcessingFeeJdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public ProcessingFee add(ProcessingFee processingFee) {
        jdbcTemplate.update(INSERT_PROCESSING_FEE_SQL,
                processingFee.getProductType(),
                processingFee.getFee());
        return processingFee;
    }

    @Override
    public List<ProcessingFee> find(String productType) {
        return jdbcTemplate.query(SELECT_PROCESSING_FEE_SQL, this::mapRowToProcessingFee, productType);
    }

    @Override
    public List<ProcessingFee> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PROCESSING_FEES_SQL, this::mapRowToProcessingFee);
    }

    @Override
    public void update(ProcessingFee processingFee, ProcessingFee oldProcessingFee) {
        jdbcTemplate.update(UPDATE_PROCESSING_FEE_SQL,
                processingFee.getProductType(),
                processingFee.getFee(),
                oldProcessingFee.getProductType(),
                oldProcessingFee.getFee());
    }

    @Override
    public void delete(ProcessingFee processingFee) {
        jdbcTemplate.update(DELETE_PROCESSING_FEE_SQL, processingFee.getProductType(), processingFee.getFee());
    }

    @Override
    public ProcessingFee mapRowToProcessingFee(ResultSet rs, int rowNum) throws SQLException {
        ProcessingFee p = new ProcessingFee() {{
            setProductType(rs.getString("product_type"));
            setFee(rs.getBigDecimal("fee"));
        }};
        return p;
    }
}
