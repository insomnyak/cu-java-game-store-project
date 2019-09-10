package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.Game;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * JSON notation is included here to overwrite Jackson's default notation for the parameter $type.
 *
 * @see ItemViewModel for details on $type parameter
 */
@JsonTypeName(ItemType.game)
public class GameViewModel extends Game implements ItemViewModel {
}
