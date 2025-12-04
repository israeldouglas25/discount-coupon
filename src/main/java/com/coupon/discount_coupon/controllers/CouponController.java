package com.coupon.discount_coupon.controllers;

import com.coupon.discount_coupon.domain.Coupon;
import com.coupon.discount_coupon.domain.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(@PathVariable String id) {
        Coupon coupon = new Database().getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody Coupon newCoupon) {
        Coupon createdCoupon = new Database().addCoupon(newCoupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        new Database().removeCoupon(id);
        return ResponseEntity.noContent().build();
    }
}
