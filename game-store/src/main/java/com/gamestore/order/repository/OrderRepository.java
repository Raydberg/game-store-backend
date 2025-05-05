package com.gamestore.order.repository;

import com.gamestore.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
    List<Order> findByUserId(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
    
    Optional<Order> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt = (SELECT MAX(o2.createdAt) FROM Order o2 WHERE o2.user.id = o.user.id) ORDER BY o.createdAt DESC")
    List<Order> findLatestOrdersForAllUsers(Pageable pageable);
    
    // Encontrar la última orden general (la más reciente de todas)
    Optional<Order> findTopByOrderByCreatedAtDesc();
}