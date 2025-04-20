package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<Cart,Integer> {

    @Query("""
      from Cart where myUser.username =?1
      """)
    Optional<Cart> findByUsername(String username);

}
