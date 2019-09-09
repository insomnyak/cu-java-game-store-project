package com.company.ReneSerulleU1Capstone.servicelayer;

import com.company.ReneSerulleU1Capstone.viewmodel.ProcessingFeeViewModel;
import com.company.ReneSerulleU1Capstone.viewmodel.SalesTaxRateViewModel;

import java.util.HashMap;
import java.util.Map;

public interface FeeType {
    String processing = "Processing Fee";
    String salesTaxRate = "Sales Tax Rate";
    String pathProcessingFee = "processingFee";
    String pathSalesTaxRate = "salesTaxRate";
    Map<String, Class> paths = new HashMap<String, Class>() {{
        put(pathProcessingFee, ProcessingFeeViewModel.class);
        put(pathSalesTaxRate, SalesTaxRateViewModel.class);
    }};
    String pathRegex = "^processingFee|salesTaxRate$";
}
