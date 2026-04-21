package com.Springer.Gojo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Springer.Gojo.entity.Cart;
import com.Springer.Gojo.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUser(User user);
}
