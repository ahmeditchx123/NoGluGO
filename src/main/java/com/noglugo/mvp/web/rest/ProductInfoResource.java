package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.repository.ProductInfoRepository;
import com.noglugo.mvp.service.ProductInfoQueryService;
import com.noglugo.mvp.service.ProductInfoService;
import com.noglugo.mvp.service.criteria.ProductInfoCriteria;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
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
 * REST controller for managing {@link com.noglugo.mvp.domain.ProductInfo}.
 */
@RestController
@RequestMapping("/api")
public class ProductInfoResource {

    private final Logger log = LoggerFactory.getLogger(ProductInfoResource.class);

    private static final String ENTITY_NAME = "productInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductInfoService productInfoService;

    private final ProductInfoRepository productInfoRepository;

    private final ProductInfoQueryService productInfoQueryService;

    public ProductInfoResource(
        ProductInfoService productInfoService,
        ProductInfoRepository productInfoRepository,
        ProductInfoQueryService productInfoQueryService
    ) {
        this.productInfoService = productInfoService;
        this.productInfoRepository = productInfoRepository;
        this.productInfoQueryService = productInfoQueryService;
    }

    /**
     * {@code POST  /product-infos} : Create a new productInfo.
     *
     * @param productInfoDTO the productInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productInfoDTO, or with status {@code 400 (Bad Request)} if the productInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-infos")
    public ResponseEntity<ProductInfoDTO> createProductInfo(@Valid @RequestBody ProductInfoDTO productInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ProductInfo : {}", productInfoDTO);
        if (productInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new productInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductInfoDTO result = productInfoService.save(productInfoDTO);
        return ResponseEntity
            .created(new URI("/api/product-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-infos/:id} : Updates an existing productInfo.
     *
     * @param id the id of the productInfoDTO to save.
     * @param productInfoDTO the productInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productInfoDTO,
     * or with status {@code 400 (Bad Request)} if the productInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-infos/{id}")
    public ResponseEntity<ProductInfoDTO> updateProductInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ProductInfoDTO productInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductInfo : {}, {}", id, productInfoDTO);
        if (productInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductInfoDTO result = productInfoService.update(productInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-infos/:id} : Partial updates given fields of an existing productInfo, field will ignore if it is null
     *
     * @param id the id of the productInfoDTO to save.
     * @param productInfoDTO the productInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productInfoDTO,
     * or with status {@code 400 (Bad Request)} if the productInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductInfoDTO> partialUpdateProductInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ProductInfoDTO productInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductInfo partially : {}, {}", id, productInfoDTO);
        if (productInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductInfoDTO> result = productInfoService.partialUpdate(productInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-infos} : get all the productInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productInfos in body.
     */
    @GetMapping("/product-infos")
    public ResponseEntity<List<ProductInfoDTO>> getAllProductInfos(
        ProductInfoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductInfos by criteria: {}", criteria);
        Page<ProductInfoDTO> page = productInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-infos/count} : count all the productInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-infos/count")
    public ResponseEntity<Long> countProductInfos(ProductInfoCriteria criteria) {
        log.debug("REST request to count ProductInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(productInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-infos/:id} : get the "id" productInfo.
     *
     * @param id the id of the productInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-infos/{id}")
    public ResponseEntity<ProductInfoDTO> getProductInfo(@PathVariable UUID id) {
        log.debug("REST request to get ProductInfo : {}", id);
        Optional<ProductInfoDTO> productInfoDTO = productInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productInfoDTO);
    }

    /**
     * {@code DELETE  /product-infos/:id} : delete the "id" productInfo.
     *
     * @param id the id of the productInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-infos/{id}")
    public ResponseEntity<Void> deleteProductInfo(@PathVariable UUID id) {
        log.debug("REST request to delete ProductInfo : {}", id);
        productInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
