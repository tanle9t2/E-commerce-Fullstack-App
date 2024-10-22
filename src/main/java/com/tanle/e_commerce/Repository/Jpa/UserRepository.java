package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Integer> {

    @Query("from MyUser where username = ?1")
    Optional<MyUser> findByUsername(String username);
}
