package com.kalasalingam.canteen.repository;

import com.kalasalingam.canteen.entity.Order;
import com.kalasalingam.canteen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findByStatusInOrderByCreatedAtDesc(List<String> statuses);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :start")
    Long countOrdersSince(@Param("start") LocalDateTime start);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.createdAt >= :start")
    Double sumRevenueSince(@Param("start") LocalDateTime start);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :start ORDER BY o.createdAt DESC")
    List<Order> findOrdersSince(@Param("start") LocalDateTime start);
}
