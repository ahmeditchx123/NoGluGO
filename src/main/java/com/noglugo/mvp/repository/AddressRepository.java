package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Address;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
    Optional<Address> findByUserIdEquals(Long userId);
}
