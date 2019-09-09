package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.TShirt;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(ItemType.tShirt)
public class TShirtViewModel extends TShirt implements ItemViewModel {
}
