package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.ProductInfo;
import com.noglugo.mvp.repository.ProductInfoRepository;
import com.noglugo.mvp.service.criteria.ProductInfoCriteria;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
import com.noglugo.mvp.service.mapper.ProductInfoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductInfo} entities in the database.
 * The main input is a {@link ProductInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductInfoDTO} or a {@link Page} of {@link ProductInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductInfoQueryService extends QueryService<ProductInfo> {

    private final Logger log = LoggerFactory.getLogger(ProductInfoQueryService.class);

    private final ProductInfoRepository productInfoRepository;

    private final ProductInfoMapper productInfoMapper;

    public ProductInfoQueryService(ProductInfoRepository productInfoRepository, ProductInfoMapper productInfoMapper) {
        this.productInfoRepository = productInfoRepository;
        this.productInfoMapper = productInfoMapper;
    }

    /**
     * Return a {@link List} of {@link ProductInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> findByCriteria(ProductInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductInfo> specification = createSpecification(criteria);
        return productInfoMapper.toDto(productInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductInfoDTO> findByCriteria(ProductInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductInfo> specification = createSpecification(criteria);
        return productInfoRepository.findAll(specification, page).map(productInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductInfo> specification = createSpecification(criteria);
        return productInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductInfo> createSpecification(ProductInfoCriteria criteria) {
        Specification<ProductInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductInfo_.id));
            }
            if (criteria.getQtyInStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQtyInStock(), ProductInfo_.qtyInStock));
            }
            if (criteria.getIsGlutenFree() != null) {
                specification = specification.and(buildSpecification(criteria.getIsGlutenFree(), ProductInfo_.isGlutenFree));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ProductInfo_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ProductInfo_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ProductInfo_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ProductInfo_.lastModifiedDate));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(ProductInfo_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
