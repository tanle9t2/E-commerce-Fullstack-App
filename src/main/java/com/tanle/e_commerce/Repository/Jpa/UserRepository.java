package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("from User where username = ?1")
    Optional<User> findByUsername(String username);
}
