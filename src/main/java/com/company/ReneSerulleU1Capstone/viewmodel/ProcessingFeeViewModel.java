package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.ProcessingFee;
import com.company.ReneSerulleU1Capstone.servicelayer.FeeType;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * JSON notation is included here to overwrite Jackson's default notation for the parameter $type.
 *
 * @see PurchaseFeeViewModel for details on $type parameter
 */
@JsonTypeName(FeeType.processing)
public class ProcessingFeeViewModel extends ProcessingFee implements PurchaseFeeViewModel {
}
