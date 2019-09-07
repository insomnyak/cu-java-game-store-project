package com.company.ReneSerulleU1Capstone.servicelayer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum BLSettings {
    MAX_QTY_BEFORE_EXTRA_FEE(new BigDecimal(10)),
    FEE_QTY_GREATER_THAN_10(new BigDecimal(15.49).setScale(2, RoundingMode.HALF_UP)),
    MAX_PURCHASE_TOTAL(new BigDecimal(999.99).setScale(2, RoundingMode.HALF_UP));

    private BigDecimal qty;

    BLSettings(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getValue() {
        return qty;
    }

    public Long getLong() {
        return qty.longValue();
    }
}
