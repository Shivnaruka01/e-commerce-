package com.Springer.Gojo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Springer.Gojo.entity.Order;
import com.Springer.Gojo.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);

	List<Order> findByUserOrderByOrderDateDesc(User user);

	Page<Order> findByUser(User user, Pageable pageable);
}