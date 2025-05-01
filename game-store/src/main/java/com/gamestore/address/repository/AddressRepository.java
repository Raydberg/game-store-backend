package com.gamestore.address.repository;

import com.gamestore.address.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<UserAddress,Long> {
}
