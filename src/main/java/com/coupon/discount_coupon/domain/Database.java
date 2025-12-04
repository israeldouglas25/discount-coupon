package com.coupon.discount_coupon.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final List<Coupon> coupon = new ArrayList<>();

    public Database() {
        coupon.add(new Coupon("cupom1","CUPOM10", "Desconto de 10 reais", BigDecimal.valueOf(10), Instant.parse("2025-12-15T23:59:59Z")));
        coupon.add(new Coupon("cupom2","CUPOM20", "Desconto de 20 reais", BigDecimal.valueOf(20), Instant.parse("2025-12-10T23:59:59Z")));
        coupon.add(new Coupon("cupom3","CUPOM30", "Desconto de 30 reais", BigDecimal.valueOf(30), Instant.parse("2025-12-05T23:59:59Z")));
    }

    public List<Coupon> getCoupons() {
        return coupon;
    }

    public Coupon addCoupon(Coupon newCoupon) {
        coupon.add(newCoupon);
        return newCoupon;
    }

    public Coupon getCouponById(String id) {
        return coupon.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void removeCoupon(String id) {
        coupon.removeIf(c -> c.getId().equals(id));
    }
}
