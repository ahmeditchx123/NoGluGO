package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUserIdEquals(Long userId);
}
