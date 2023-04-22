package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.GlutenProfile;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GlutenProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlutenProfileRepository extends JpaRepository<GlutenProfile, UUID>, JpaSpecificationExecutor<GlutenProfile> {}
