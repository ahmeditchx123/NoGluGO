package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Menu;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID>, JpaSpecificationExecutor<Menu> {}
