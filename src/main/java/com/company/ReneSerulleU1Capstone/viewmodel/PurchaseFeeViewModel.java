package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.servicelayer.FeeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This interface is used to accepts and send objects that implement this interface.
 * The annotation allows Jackson to desearilize a JSON object that is received from the
 *      controller paths and map it to the appropriate Class object that implements this
 *      interface. This requires a parameter to be passed in the JSON. For this implementation the
 *      parameter is $type, which is required for all in-bound requests where a RequestBody is
 *      provided, such as POST and PUT methods.
 *
 * @see SalesTaxRateViewModel
 * @see ProcessingFeeViewModel
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"$type"})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProcessingFeeViewModel.class, name = FeeType.processing),
        @JsonSubTypes.Type(value = SalesTaxRateViewModel.class, name = FeeType.salesTaxRate),
})
public interface PurchaseFeeViewModel {
}
