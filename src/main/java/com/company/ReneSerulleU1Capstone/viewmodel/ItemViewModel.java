package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.Console;
import com.company.ReneSerulleU1Capstone.model.Game;
import com.company.ReneSerulleU1Capstone.model.TShirt;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"$type"})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleViewModel.class, name = ItemType.console),
        @JsonSubTypes.Type(value = GameViewModel.class, name = ItemType.game),
        @JsonSubTypes.Type(value = TShirtViewModel.class, name = ItemType.tShirt)
})
public interface ItemViewModel {
}
