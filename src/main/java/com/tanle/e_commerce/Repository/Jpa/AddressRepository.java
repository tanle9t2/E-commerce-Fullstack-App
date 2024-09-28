package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
