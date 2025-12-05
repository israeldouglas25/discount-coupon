package com.coupon.discount_coupon.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_coupons")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Coupon {

    @Id
    @GeneratedValue(generator = "UUID")
    @EqualsAndHashCode.Include
    private UUID id;
    private String code;
    private String description;
    private Double discountValue;
    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private Boolean published = false;
    private Boolean redeemed = false;

    public Coupon(String code, String description, Double discountValue, Instant expirationDate) {
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
    }

}
