package com.coupon.discount_coupon.services;

import com.coupon.discount_coupon.domain.Coupon;
import com.coupon.discount_coupon.interfaces.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public ResponseEntity<Coupon> findById(String id) {
        Coupon coupon = couponRepository.findById(UUID.fromString(id)).orElse(null);
        return ResponseEntity.ok(coupon);
    }

    public ResponseEntity<Coupon> create(Coupon newCoupon) {
        Coupon createdCoupon = couponRepository.save(newCoupon);
        return ResponseEntity.ok(createdCoupon);
    }

    public ResponseEntity<Void> delete(String id) {
        couponRepository.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
