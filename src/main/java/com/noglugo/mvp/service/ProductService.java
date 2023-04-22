package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.ProductDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.noglugo.mvp.domain.Product}.
 */
public interface ProductService {
    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    ProductDTO save(ProductDTO productDTO);

    /**
     * Updates a product.
     *
     * @param productDTO the entity to update.
     * @return the persisted entity.
     */
    ProductDTO update(ProductDTO productDTO);

    /**
     * Partially updates a product.
     *
     * @param productDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductDTO> partialUpdate(ProductDTO productDTO);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDTO> findAll(Pageable pageable);
    /**
     * Get all the ProductDTO where Information is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ProductDTO> findAllWhereInformationIsNull();

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDTO> findOne(UUID id);

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
