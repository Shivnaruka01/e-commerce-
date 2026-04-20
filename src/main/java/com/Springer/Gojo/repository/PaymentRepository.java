package com.Springer.Gojo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Springer.Gojo.entity.Order;
import com.Springer.Gojo.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	Optional<Payment> findByOrder(Order order);
}
