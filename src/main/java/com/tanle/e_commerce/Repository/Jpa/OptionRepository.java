package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option,Integer> {
}
