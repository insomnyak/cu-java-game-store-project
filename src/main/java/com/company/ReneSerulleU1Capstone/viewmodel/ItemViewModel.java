package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.Console;
import com.company.ReneSerulleU1Capstone.model.Game;
import com.company.ReneSerulleU1Capstone.model.TShirt;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
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
 * @see ConsoleViewModel
 * @see GameViewModel
 * @see TShirtViewModel
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"$type"})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleViewModel.class, name = ItemType.console),
        @JsonSubTypes.Type(value = GameViewModel.class, name = ItemType.game),
        @JsonSubTypes.Type(value = TShirtViewModel.class, name = ItemType.tShirt)
})
public interface ItemViewModel {
}
