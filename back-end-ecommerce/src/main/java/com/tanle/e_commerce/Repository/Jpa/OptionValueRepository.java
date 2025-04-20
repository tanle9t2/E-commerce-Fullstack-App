package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue,Integer> {
}
