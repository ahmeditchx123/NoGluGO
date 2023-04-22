package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.repository.GlutenProfileRepository;
import com.noglugo.mvp.service.GlutenProfileQueryService;
import com.noglugo.mvp.service.GlutenProfileService;
import com.noglugo.mvp.service.criteria.GlutenProfileCriteria;
import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import com.noglugo.mvp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.noglugo.mvp.domain.GlutenProfile}.
 */
@RestController
@RequestMapping("/api")
public class GlutenProfileResource {

    private final Logger log = LoggerFactory.getLogger(GlutenProfileResource.class);

    private static final String ENTITY_NAME = "glutenProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlutenProfileService glutenProfileService;

    private final GlutenProfileRepository glutenProfileRepository;

    private final GlutenProfileQueryService glutenProfileQueryService;

    public GlutenProfileResource(
        GlutenProfileService glutenProfileService,
        GlutenProfileRepository glutenProfileRepository,
        GlutenProfileQueryService glutenProfileQueryService
    ) {
        this.glutenProfileService = glutenProfileService;
        this.glutenProfileRepository = glutenProfileRepository;
        this.glutenProfileQueryService = glutenProfileQueryService;
    }

    /**
     * {@code POST  /gluten-profiles} : Create a new glutenProfile.
     *
     * @param glutenProfileDTO the glutenProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new glutenProfileDTO, or with status {@code 400 (Bad Request)} if the glutenProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gluten-profiles")
    public ResponseEntity<GlutenProfileDTO> createGlutenProfile(@RequestBody GlutenProfileDTO glutenProfileDTO) throws URISyntaxException {
        log.debug("REST request to save GlutenProfile : {}", glutenProfileDTO);
        if (glutenProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new glutenProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlutenProfileDTO result = glutenProfileService.save(glutenProfileDTO);
        return ResponseEntity
            .created(new URI("/api/gluten-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gluten-profiles/:id} : Updates an existing glutenProfile.
     *
     * @param id the id of the glutenProfileDTO to save.
     * @param glutenProfileDTO the glutenProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated glutenProfileDTO,
     * or with status {@code 400 (Bad Request)} if the glutenProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the glutenProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gluten-profiles/{id}")
    public ResponseEntity<GlutenProfileDTO> updateGlutenProfile(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody GlutenProfileDTO glutenProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GlutenProfile : {}, {}", id, glutenProfileDTO);
        if (glutenProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, glutenProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!glutenProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GlutenProfileDTO result = glutenProfileService.update(glutenProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, glutenProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gluten-profiles/:id} : Partial updates given fields of an existing glutenProfile, field will ignore if it is null
     *
     * @param id the id of the glutenProfileDTO to save.
     * @param glutenProfileDTO the glutenProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated glutenProfileDTO,
     * or with status {@code 400 (Bad Request)} if the glutenProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the glutenProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the glutenProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gluten-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GlutenProfileDTO> partialUpdateGlutenProfile(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody GlutenProfileDTO glutenProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlutenProfile partially : {}, {}", id, glutenProfileDTO);
        if (glutenProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, glutenProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!glutenProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlutenProfileDTO> result = glutenProfileService.partialUpdate(glutenProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, glutenProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gluten-profiles} : get all the glutenProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of glutenProfiles in body.
     */
    @GetMapping("/gluten-profiles")
    public ResponseEntity<List<GlutenProfileDTO>> getAllGlutenProfiles(
        GlutenProfileCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GlutenProfiles by criteria: {}", criteria);
        Page<GlutenProfileDTO> page = glutenProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gluten-profiles/count} : count all the glutenProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gluten-profiles/count")
    public ResponseEntity<Long> countGlutenProfiles(GlutenProfileCriteria criteria) {
        log.debug("REST request to count GlutenProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(glutenProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gluten-profiles/:id} : get the "id" glutenProfile.
     *
     * @param id the id of the glutenProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the glutenProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gluten-profiles/{id}")
    public ResponseEntity<GlutenProfileDTO> getGlutenProfile(@PathVariable UUID id) {
        log.debug("REST request to get GlutenProfile : {}", id);
        Optional<GlutenProfileDTO> glutenProfileDTO = glutenProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(glutenProfileDTO);
    }

    /**
     * {@code DELETE  /gluten-profiles/:id} : delete the "id" glutenProfile.
     *
     * @param id the id of the glutenProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gluten-profiles/{id}")
    public ResponseEntity<Void> deleteGlutenProfile(@PathVariable UUID id) {
        log.debug("REST request to delete GlutenProfile : {}", id);
        glutenProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
