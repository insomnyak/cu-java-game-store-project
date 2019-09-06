package com.company.ReneSerulleU1Capstone.model;

import java.util.ArrayList;
import java.util.List;

public interface ItemType {
    String game = "Games";
    String console = "Consoles";
    String tShirt = "T-Shirts";
    String patternCaseSensitive = "^Games|Consoles|T-Shirts$";
    List<String> gameSearchableAttributes = new ArrayList<String>() {{
        add("Studio"); add("ESRB Rating"); add("Title");
    }};
    List<String> consoleSearchableAttributes = new ArrayList<String>() {{
        add("Manufacturer");
    }};
    List<String> tShirtSearchableAttributes = new ArrayList<String>() {{
        add("Color"); add("Size");
    }};
}
