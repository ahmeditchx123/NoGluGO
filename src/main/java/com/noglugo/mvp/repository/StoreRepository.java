package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Store;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Store entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, JpaSpecificationExecutor<Store> {
    Optional<Store> findByUserIdEquals(Long userId);
}
