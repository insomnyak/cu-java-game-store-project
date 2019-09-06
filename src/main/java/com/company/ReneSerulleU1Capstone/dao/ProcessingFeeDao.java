package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.ProcessingFee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ProcessingFeeDao {
    // CRUD
    ProcessingFee add(ProcessingFee processingFee);
    ProcessingFee find(String productType);
    List<ProcessingFee> findAll();
    void update(ProcessingFee processingFee, String productType);
    void delete(String productType);

    ProcessingFee mapRowToProcessingFee(ResultSet rs, int rowNum) throws SQLException;
}
