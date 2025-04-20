package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {

    @Query("from MyUser where username =:username")
    Optional<MyUser> findByUsername(@Param("username") String username);

    @Query("from MyUser where email =:email")
    Optional<MyUser> findByEmail(@Param("email") String email);

    @Query("""
        SELECT u 
        FROM MyUser u
        WHERE u.id IN :ids
       """)
    List<MyUser> findUserChat(@Param("ids") List<Integer> ids);
}
