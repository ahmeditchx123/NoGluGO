package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.noglugo.mvp.domain.GlutenProfile}.
 */
public interface GlutenProfileService {
    /**
     * Save a glutenProfile.
     *
     * @param glutenProfileDTO the entity to save.
     * @return the persisted entity.
     */
    GlutenProfileDTO save(GlutenProfileDTO glutenProfileDTO);

    /**
     * Updates a glutenProfile.
     *
     * @param glutenProfileDTO the entity to update.
     * @return the persisted entity.
     */
    GlutenProfileDTO update(GlutenProfileDTO glutenProfileDTO);

    /**
     * Partially updates a glutenProfile.
     *
     * @param glutenProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GlutenProfileDTO> partialUpdate(GlutenProfileDTO glutenProfileDTO);

    /**
     * Get all the glutenProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GlutenProfileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" glutenProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GlutenProfileDTO> findOne(UUID id);

    /**
     * Delete the "id" glutenProfile.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
