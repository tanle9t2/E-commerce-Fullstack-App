package com.tanle.e_commerce.Repository.Jpa;

import aj.org.objectweb.asm.commons.Remapper;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findTokenByToken(String jwtToken);
}
