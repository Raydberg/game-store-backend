package com.gamestore.address.repository;

import com.gamestore.address.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<UserAddress,Long> {
    Optional<UserAddress> findByUserId(Long userId);
}