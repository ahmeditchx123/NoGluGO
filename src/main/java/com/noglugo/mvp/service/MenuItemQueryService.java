package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.repository.MenuItemRepository;
import com.noglugo.mvp.service.criteria.MenuItemCriteria;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.service.mapper.MenuItemMapper;
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
 * Service for executing complex queries for {@link MenuItem} entities in the database.
 * The main input is a {@link MenuItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuItemDTO} or a {@link Page} of {@link MenuItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuItemQueryService extends QueryService<MenuItem> {

    private final Logger log = LoggerFactory.getLogger(MenuItemQueryService.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    public MenuItemQueryService(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    /**
     * Return a {@link List} of {@link MenuItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuItemDTO> findByCriteria(MenuItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemMapper.toDto(menuItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenuItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findByCriteria(MenuItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification, page).map(menuItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuItem> specification = createSpecification(criteria);
        return menuItemRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MenuItem> createSpecification(MenuItemCriteria criteria) {
        Specification<MenuItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MenuItem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MenuItem_.name));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), MenuItem_.content));
            }
            if (criteria.getImgPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgPath(), MenuItem_.imgPath));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), MenuItem_.unitPrice));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), MenuItem_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MenuItem_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), MenuItem_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), MenuItem_.lastModifiedDate));
            }
            if (criteria.getMenuId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenuId(), root -> root.join(MenuItem_.menu, JoinType.LEFT).get(Menu_.id))
                    );
            }
            if (criteria.getReviewsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReviewsId(), root -> root.join(MenuItem_.reviews, JoinType.LEFT).get(Review_.id))
                    );
            }
        }
        return specification;
    }
}
