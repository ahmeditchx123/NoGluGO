package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.RestaurantDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.noglugo.mvp.domain.Restaurant}.
 */
public interface RestaurantService {
    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    RestaurantDTO save(RestaurantDTO restaurantDTO);

    /**
     * Updates a restaurant.
     *
     * @param restaurantDTO the entity to update.
     * @return the persisted entity.
     */
    RestaurantDTO update(RestaurantDTO restaurantDTO);

    /**
     * Partially updates a restaurant.
     *
     * @param restaurantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO);

    /**
     * Get all the restaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantDTO> findAll(Pageable pageable);
    /**
     * Get all the RestaurantDTO where RestaurantAddress is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<RestaurantDTO> findAllWhereRestaurantAddressIsNull();
    /**
     * Get all the RestaurantDTO where RestaurantMenu is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<RestaurantDTO> findAllWhereRestaurantMenuIsNull();

    /**
     * Get the "id" restaurant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantDTO> findOne(UUID id);

    /**
     * Delete the "id" restaurant.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
