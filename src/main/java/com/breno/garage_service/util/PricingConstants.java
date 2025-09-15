package com.breno.garage_service.util;

import java.math.BigDecimal;

public final class PricingConstants {

    private PricingConstants() {}

    public static final int FREE_MINUTES = 30;
    public static final BigDecimal DISCOUNT_LOW = BigDecimal.valueOf(0.90); // -10%
    public static final BigDecimal NORMAL_PRICE = BigDecimal.ONE;           // 0%
    public static final BigDecimal SURCHARGE_MEDIUM = BigDecimal.valueOf(1.10); // +10%
    public static final BigDecimal SURCHARGE_HIGH = BigDecimal.valueOf(1.25);   // +25%
}
