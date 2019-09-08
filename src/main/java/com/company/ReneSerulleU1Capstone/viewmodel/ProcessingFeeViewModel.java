package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.ProcessingFee;
import com.company.ReneSerulleU1Capstone.servicelayer.FeeType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(FeeType.processing)
public class ProcessingFeeViewModel extends ProcessingFee implements PurchaseFee {
}
