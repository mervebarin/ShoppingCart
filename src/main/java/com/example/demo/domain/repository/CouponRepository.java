package com.example.demo.domain.repository;

import com.example.demo.domain.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}