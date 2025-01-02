package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.CompositeKey.CartItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {
}
