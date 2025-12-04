package com.coupon.discount_coupon.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

//@Entity
//@Table(name = "coupons")
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(of = "id")
public class Coupon {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private Instant expirationDate;


}
