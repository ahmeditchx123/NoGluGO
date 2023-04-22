package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.repository.MenuItemRepository;
import com.noglugo.mvp.service.MenuItemQueryService;
import com.noglugo.mvp.service.MenuItemService;
import com.noglugo.mvp.service.criteria.MenuItemCriteria;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.noglugo.mvp.domain.MenuItem}.
 */
@RestController
@RequestMapping("/api")
public class MenuItemResource {

    private final Logger log = LoggerFactory.getLogger(MenuItemResource.class);

    private static final String ENTITY_NAME = "menuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuItemService menuItemService;

    private final MenuItemRepository menuItemRepository;

    private final MenuItemQueryService menuItemQueryService;

    public MenuItemResource(
        MenuItemService menuItemService,
        MenuItemRepository menuItemRepository,
        MenuItemQueryService menuItemQueryService
    ) {
        this.menuItemService = menuItemService;
        this.menuItemRepository = menuItemRepository;
        this.menuItemQueryService = menuItemQueryService;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItem.
     *
     * @param menuItemDTO the menuItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItemDTO, or with status {@code 400 (Bad Request)} if the menuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItemDTO);
        if (menuItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItemDTO result = menuItemService.save(menuItemDTO);
        return ResponseEntity
            .created(new URI("/api/menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-items/:id} : Updates an existing menuItem.
     *
     * @param id the id of the menuItemDTO to save.
     * @param menuItemDTO the menuItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItemDTO,
     * or with status {@code 400 (Bad Request)} if the menuItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-items/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MenuItemDTO menuItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MenuItem : {}, {}", id, menuItemDTO);
        if (menuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItemDTO result = menuItemService.update(menuItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menu-items/:id} : Partial updates given fields of an existing menuItem, field will ignore if it is null
     *
     * @param id the id of the menuItemDTO to save.
     * @param menuItemDTO the menuItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItemDTO,
     * or with status {@code 400 (Bad Request)} if the menuItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the menuItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/menu-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuItemDTO> partialUpdateMenuItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MenuItemDTO menuItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuItem partially : {}, {}", id, menuItemDTO);
        if (menuItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuItemDTO> result = menuItemService.partialUpdate(menuItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems(
        MenuItemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MenuItems by criteria: {}", criteria);
        Page<MenuItemDTO> page = menuItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /menu-items/count} : count all the menuItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/menu-items/count")
    public ResponseEntity<Long> countMenuItems(MenuItemCriteria criteria) {
        log.debug("REST request to count MenuItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItem.
     *
     * @param id the id of the menuItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable UUID id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItemDTO> menuItemDTO = menuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuItemDTO);
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItem.
     *
     * @param id the id of the menuItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
