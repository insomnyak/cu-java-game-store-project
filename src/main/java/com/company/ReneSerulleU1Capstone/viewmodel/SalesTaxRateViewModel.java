package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.SalesTaxRate;
import com.company.ReneSerulleU1Capstone.servicelayer.FeeType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(FeeType.salesTaxRate)
public class SalesTaxRateViewModel extends SalesTaxRate implements PurchaseFee {
}
