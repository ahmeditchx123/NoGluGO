package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.ProductInfoDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.noglugo.mvp.domain.ProductInfo}.
 */
public interface ProductInfoService {
    /**
     * Save a productInfo.
     *
     * @param productInfoDTO the entity to save.
     * @return the persisted entity.
     */
    ProductInfoDTO save(ProductInfoDTO productInfoDTO);

    /**
     * Updates a productInfo.
     *
     * @param productInfoDTO the entity to update.
     * @return the persisted entity.
     */
    ProductInfoDTO update(ProductInfoDTO productInfoDTO);

    /**
     * Partially updates a productInfo.
     *
     * @param productInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductInfoDTO> partialUpdate(ProductInfoDTO productInfoDTO);

    /**
     * Get all the productInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductInfoDTO> findOne(UUID id);

    /**
     * Delete the "id" productInfo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
