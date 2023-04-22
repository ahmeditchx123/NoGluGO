package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.repository.StoreRepository;
import com.noglugo.mvp.service.criteria.StoreCriteria;
import com.noglugo.mvp.service.dto.StoreDTO;
import com.noglugo.mvp.service.mapper.StoreMapper;
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
 * Service for executing complex queries for {@link Store} entities in the database.
 * The main input is a {@link StoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StoreDTO} or a {@link Page} of {@link StoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StoreQueryService extends QueryService<Store> {

    private final Logger log = LoggerFactory.getLogger(StoreQueryService.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    public StoreQueryService(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    /**
     * Return a {@link List} of {@link StoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StoreDTO> findByCriteria(StoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Store> specification = createSpecification(criteria);
        return storeMapper.toDto(storeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StoreDTO> findByCriteria(StoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Store> specification = createSpecification(criteria);
        return storeRepository.findAll(specification, page).map(storeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Store> specification = createSpecification(criteria);
        return storeRepository.count(specification);
    }

    /**
     * Function to convert {@link StoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Store> createSpecification(StoreCriteria criteria) {
        Specification<Store> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Store_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Store_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Store_.description));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Store_.telephone));
            }
            if (criteria.getImgPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgPath(), Store_.imgPath));
            }
            if (criteria.getIsDedicatedGlutenFree() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDedicatedGlutenFree(), Store_.isDedicatedGlutenFree));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Store_.website));
            }
            if (criteria.getHasDeliveryMode() != null) {
                specification = specification.and(buildSpecification(criteria.getHasDeliveryMode(), Store_.hasDeliveryMode));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Store_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Store_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Store_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Store_.lastModifiedDate));
            }
            if (criteria.getStoreAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStoreAddressId(),
                            root -> root.join(Store_.storeAddress, JoinType.LEFT).get(Address_.id)
                        )
                    );
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductsId(), root -> root.join(Store_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
