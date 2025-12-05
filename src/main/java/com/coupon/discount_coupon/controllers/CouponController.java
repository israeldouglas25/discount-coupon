package com.coupon.discount_coupon.controllers;

import com.coupon.discount_coupon.domain.Coupon;
import com.coupon.discount_coupon.services.CouponService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public ResponseEntity<List<Coupon>> getAll() {
        return couponService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(@PathVariable String id) {
        return couponService.findById(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Coupon> create(@RequestBody @Valid Coupon coupon, UriComponentsBuilder uriBuilder) {
        var createdCoupon = couponService.create(coupon).getBody();
        URI uri = uriBuilder.path("/coupons/{id}").buildAndExpand(createdCoupon.getId()).toUri();
        return ResponseEntity.created(uri).body(createdCoupon);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return couponService.delete(id);
    }
}
