package com.company.ReneSerulleU1Capstone.servicelayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public enum BLSettings {
    MAX_PURCHASE_TOTAL(new BigDecimal(999.99).setScale(2, RoundingMode.HALF_UP));

    private Object qty;

    BLSettings(Object qty) {
        this.qty = qty;
    }

    public BigDecimal toBigDecimal() {
        return (BigDecimal) qty;
    }

    enum ADDITIONAL_FEES {
        MAX_QTY_BEFORE_EXTRA_PROCESSING_FEE(10L),
        PROCESSING_FEE_QTY_GREATER_THAN_10(new BigDecimal(15.49).setScale(2, RoundingMode.HALF_UP));

        private Object val;

        ADDITIONAL_FEES(Object val) {
            this.val = val;
        }

        public BigDecimal toBigDecimal() {
            return (BigDecimal) val;
        }

        public Long toLong() {
            return (Long) val;
        }
    }

    enum DTO_SEARCHABLE_ATTRIBUTES {
        GAME(new ArrayList<String>() {{
            add("Studio"); add("ESRB Rating"); add("Title");
        }}),
        CONSOLE(new ArrayList<String>() {{
            add("Manufacturer");
        }}),
        T_SHIRTS(new ArrayList<String>() {{
            add("Color"); add("Size");
        }});

        private ArrayList<String> list;

        DTO_SEARCHABLE_ATTRIBUTES(ArrayList<String> list) {
        }

        public List<String> toList() {
            return list;
        }
    }
}
