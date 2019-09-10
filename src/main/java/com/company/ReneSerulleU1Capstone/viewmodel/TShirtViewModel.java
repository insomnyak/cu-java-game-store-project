package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.TShirt;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * JSON notation is included here to overwrite Jackson's default notation for the parameter $type.
 *
 * @see ItemViewModel for details on $type parameter
 */
@JsonTypeName(ItemType.tShirt)
public class TShirtViewModel extends TShirt implements ItemViewModel {
}
