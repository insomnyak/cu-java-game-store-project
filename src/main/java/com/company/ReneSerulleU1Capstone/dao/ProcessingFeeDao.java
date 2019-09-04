package com.company.ReneSerulleU1Capstone.dao;

import com.company.ReneSerulleU1Capstone.model.ProcessingFee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ProcessingFeeDao {
    // CRUD
    ProcessingFee add(ProcessingFee processingFee);
    List<ProcessingFee> find(String productType);
    List<ProcessingFee> findAll();
    void update(ProcessingFee processingFee, ProcessingFee oldProcessingFee);
    void delete(ProcessingFee processingFee);

    ProcessingFee mapRowToProcessingFee(ResultSet rs, int rowNum) throws SQLException;
}
