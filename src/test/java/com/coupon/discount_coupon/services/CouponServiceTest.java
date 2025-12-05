package com.coupon.discount_coupon.services;

import com.coupon.discount_coupon.domain.Coupon;
import com.coupon.discount_coupon.domain.CouponStatus;
import com.coupon.discount_coupon.exceptions.BadRequestException;
import com.coupon.discount_coupon.exceptions.NotFoundException;
import com.coupon.discount_coupon.interfaces.CouponRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CouponServiceTest {

    @Test
    public void criouCupomComCaracteresEspeciais_removeCaracteresEAtiva() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon("AB#C12", "desc", 1.0, Instant.now().plusSeconds(3600));
        Mockito.when(couponRepository.save(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = couponService.create(input);

        assertEquals(200, response.getStatusCode().value());
        Coupon saved = response.getBody();
        assertNotNull(saved);
        assertEquals("ABC12", saved.getCode());
        assertEquals(CouponStatus.ACTIVE, saved.getStatus());
        Mockito.verify(couponRepository).save(ArgumentMatchers.any());
    }

    @Test
    public void criarComDataExpirada_lancaBadRequestException() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon("ABC123", "desc", 1.0, Instant.now().minusSeconds(3600));

        assertThrows(BadRequestException.class, () -> couponService.create(input));
        Mockito.verifyNoInteractions(couponRepository);
    }

    @Test
    public void criarSemCodigo_lancaBadRequestException_quandoNulo() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon(null, "desc", 1.0, Instant.now().plusSeconds(3600));

        assertThrows(BadRequestException.class, () -> couponService.create(input));
        Mockito.verifyNoInteractions(couponRepository);
    }

    @Test
    public void criarSemCodigo_lancaBadRequestException_quandoVazio() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon("", "desc", 1.0, Instant.now().plusSeconds(3600));

        assertThrows(BadRequestException.class, () -> couponService.create(input));
        Mockito.verifyNoInteractions(couponRepository);
    }

    @Test
    public void criarComCodigoMaiorQue7_lancaBadRequestException() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon("ABCDEFGH", "desc", 1.0, Instant.now().plusSeconds(3600));

        assertThrows(BadRequestException.class, () -> couponService.create(input));
        Mockito.verifyNoInteractions(couponRepository);
    }

    @Test
    public void criarComDescontoMenorQueMinimo_lancaBadRequestException() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        Coupon input = new Coupon("ABC123", "desc", 0.4, Instant.now().plusSeconds(3600));

        assertThrows(BadRequestException.class, () -> couponService.create(input));
        Mockito.verifyNoInteractions(couponRepository);
    }

    @Test
    public void findById_existente_retornaCoupon() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon("ABC123", "d", 1.0, Instant.now().plusSeconds(3600));
        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        var response = couponService.findById(id.toString());

        assertEquals(200, response.getStatusCode().value());
        assertSame(coupon, response.getBody());
    }

    @Test
    public void findById_inexistente_lancaNotFoundException() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        UUID id = UUID.randomUUID();
        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> couponService.findById(id.toString()));
    }

    @Test
    public void delete_inexistente_lancaNotFoundException() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        UUID id = UUID.randomUUID();
        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> couponService.delete(id.toString()));
    }

    @Test
    public void delete_existente_alteraStatusParaDeletedQuandoNaoEstaDeleted() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon("ABC123", "d", 1.0, Instant.now().plusSeconds(3600));
        coupon.setStatus(CouponStatus.ACTIVE);

        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));
        Mockito.when(couponRepository.save(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = couponService.delete(id.toString());

        assertEquals(204, response.getStatusCode().value());
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        Mockito.verify(couponRepository).save(coupon);
    }

    @Test
    public void delete_existente_lancaBadRequestQuandoJaDeletado() throws Exception {
        CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        CouponService couponService = new CouponService();
        Field field = CouponService.class.getDeclaredField("couponRepository");
        field.setAccessible(true);
        field.set(couponService, couponRepository);

        UUID id = UUID.randomUUID();
        Coupon coupon = new Coupon("ABC123", "d", 1.0, Instant.now().plusSeconds(3600));
        coupon.setStatus(CouponStatus.DELETED);

        Mockito.when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        assertThrows(BadRequestException.class, () -> couponService.delete(id.toString()));
        Mockito.verify(couponRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

}