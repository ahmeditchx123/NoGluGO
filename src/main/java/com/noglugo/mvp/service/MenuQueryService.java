package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.repository.MenuRepository;
import com.noglugo.mvp.service.criteria.MenuCriteria;
import com.noglugo.mvp.service.dto.MenuDTO;
import com.noglugo.mvp.service.mapper.MenuMapper;
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
 * Service for executing complex queries for {@link Menu} entities in the database.
 * The main input is a {@link MenuCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuDTO} or a {@link Page} of {@link MenuDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuQueryService extends QueryService<Menu> {

    private final Logger log = LoggerFactory.getLogger(MenuQueryService.class);

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    public MenuQueryService(MenuRepository menuRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    /**
     * Return a {@link List} of {@link MenuDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuDTO> findByCriteria(MenuCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuMapper.toDto(menuRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenuDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuDTO> findByCriteria(MenuCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuRepository.findAll(specification, page).map(menuMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Menu> specification = createSpecification(criteria);
        return menuRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Menu> createSpecification(MenuCriteria criteria) {
        Specification<Menu> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Menu_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Menu_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Menu_.description));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Menu_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Menu_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Menu_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Menu_.lastModifiedDate));
            }
            if (criteria.getRestaurantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRestaurantId(),
                            root -> root.join(Menu_.restaurant, JoinType.LEFT).get(Restaurant_.id)
                        )
                    );
            }
            if (criteria.getMenuItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenuItemsId(), root -> root.join(Menu_.menuItems, JoinType.LEFT).get(MenuItem_.id))
                    );
            }
        }
        return specification;
    }
}
