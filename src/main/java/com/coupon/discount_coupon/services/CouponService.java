package com.coupon.discount_coupon.services;

import com.coupon.discount_coupon.domain.Coupon;
import com.coupon.discount_coupon.domain.CouponStatus;
import com.coupon.discount_coupon.exceptions.BadRequestException;
import com.coupon.discount_coupon.exceptions.NotFoundException;
import com.coupon.discount_coupon.interfaces.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public ResponseEntity<List<Coupon>> getAll() {
        return ResponseEntity.ok(couponRepository.findAll());
    }

    public ResponseEntity<Coupon> findById(String id) {
        return couponRepository.findById(UUID.fromString(id))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Cupom não encontrado com id: " + id));
    }

    public ResponseEntity<Coupon> create(Coupon coupon) {
        var newCoupon = getCoupon(coupon);

        if (newCoupon.getExpirationDate().isBefore(Instant.now())) {
            throw new BadRequestException("Data de expiração inválida");
        }

        String newCode = coupon.getCode().replaceAll("[^A-Za-z0-9]", "");
        newCoupon.setCode(newCode);
        newCoupon.setStatus(CouponStatus.ACTIVE);

        var createdCoupon = couponRepository.save(newCoupon);
        return ResponseEntity.ok(createdCoupon);
    }

    public ResponseEntity<Void> delete(String id) {
        Optional<Coupon> coupon = couponRepository.findById(UUID.fromString(id));

        if (coupon.isEmpty()) {
            throw new NotFoundException("Cupom não encontrado com id: " + id);
        }

        if (coupon.get().getStatus() != CouponStatus.DELETED) {
            Coupon existingCoupon = coupon.get();
            existingCoupon.setStatus(CouponStatus.DELETED);
            couponRepository.save(existingCoupon);
            return ResponseEntity.noContent().build();
        } else {
            throw new BadRequestException("Cupom já foi deletado com id: " + id);
        }
    }

    private static Coupon getCoupon(Coupon coupon) {
        var newCoupon = new Coupon(coupon.getCode(), coupon.getDescription(), coupon.getDiscountValue(), coupon.getExpirationDate());

        if (newCoupon.getCode() == null || newCoupon.getCode().isEmpty()) {
            throw new BadRequestException("Codigo do cupom é obrigatório");
        }

        if (newCoupon.getCode().length() > 7) {
            throw new BadRequestException("Codigo do cupom deve ter no máximo 7 caracteres sendo um caractere especial");
        }

        if (newCoupon.getDiscountValue() < 0.5) {
            throw new BadRequestException("Valor de desconto deve ser no mínimo 0.5");
        }
        return newCoupon;
    }
}
