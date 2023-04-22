package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByUserIdEquals(Long userId);
}
