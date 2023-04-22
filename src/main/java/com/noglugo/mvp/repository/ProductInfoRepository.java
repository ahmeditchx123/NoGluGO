package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.ProductInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, UUID>, JpaSpecificationExecutor<ProductInfo> {}
