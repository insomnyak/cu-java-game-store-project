package com.company.ReneSerulleU1Capstone.servicelayer;

import java.util.ArrayList;
import java.util.List;

public enum DtoSearchableAttributes {
    GAME(new ArrayList<String>() {{
        add("Studio"); add("ESRB Rating"); add("Title");
    }}),
    CONSOLE(new ArrayList<String>() {{
        add("Manufacturer");
    }}),
    T_SHIRTS(new ArrayList<String>() {{
        add("Color"); add("Size");
    }});

    private List<String> attributes;

    DtoSearchableAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getList() {
        return attributes;
    }
}
