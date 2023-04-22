package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.MenuItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID>, JpaSpecificationExecutor<MenuItem> {}
