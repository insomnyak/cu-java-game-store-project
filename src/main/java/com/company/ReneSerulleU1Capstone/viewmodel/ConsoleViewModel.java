package com.company.ReneSerulleU1Capstone.viewmodel;

import com.company.ReneSerulleU1Capstone.model.Console;
import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(ItemType.console)
public class ConsoleViewModel extends Console implements ItemViewModel {
}
