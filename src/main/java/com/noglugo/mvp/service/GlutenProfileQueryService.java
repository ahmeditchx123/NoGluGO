package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.GlutenProfile;
import com.noglugo.mvp.repository.GlutenProfileRepository;
import com.noglugo.mvp.service.criteria.GlutenProfileCriteria;
import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import com.noglugo.mvp.service.mapper.GlutenProfileMapper;
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
 * Service for executing complex queries for {@link GlutenProfile} entities in the database.
 * The main input is a {@link GlutenProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GlutenProfileDTO} or a {@link Page} of {@link GlutenProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GlutenProfileQueryService extends QueryService<GlutenProfile> {

    private final Logger log = LoggerFactory.getLogger(GlutenProfileQueryService.class);

    private final GlutenProfileRepository glutenProfileRepository;

    private final GlutenProfileMapper glutenProfileMapper;

    public GlutenProfileQueryService(GlutenProfileRepository glutenProfileRepository, GlutenProfileMapper glutenProfileMapper) {
        this.glutenProfileRepository = glutenProfileRepository;
        this.glutenProfileMapper = glutenProfileMapper;
    }

    /**
     * Return a {@link List} of {@link GlutenProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GlutenProfileDTO> findByCriteria(GlutenProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GlutenProfile> specification = createSpecification(criteria);
        return glutenProfileMapper.toDto(glutenProfileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GlutenProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GlutenProfileDTO> findByCriteria(GlutenProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GlutenProfile> specification = createSpecification(criteria);
        return glutenProfileRepository.findAll(specification, page).map(glutenProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GlutenProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GlutenProfile> specification = createSpecification(criteria);
        return glutenProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link GlutenProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GlutenProfile> createSpecification(GlutenProfileCriteria criteria) {
        Specification<GlutenProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GlutenProfile_.id));
            }
            if (criteria.getDiseas() != null) {
                specification = specification.and(buildSpecification(criteria.getDiseas(), GlutenProfile_.diseas));
            }
            if (criteria.getOtherDiseas() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOtherDiseas(), GlutenProfile_.otherDiseas));
            }
            if (criteria.getStrictnessLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStrictnessLevel(), GlutenProfile_.strictnessLevel));
            }
            if (criteria.getDiaryFreePreferenceLvl() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDiaryFreePreferenceLvl(), GlutenProfile_.diaryFreePreferenceLvl));
            }
            if (criteria.getVeganPreferenceLvl() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getVeganPreferenceLvl(), GlutenProfile_.veganPreferenceLvl));
            }
            if (criteria.getKetoPreferenceLvl() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getKetoPreferenceLvl(), GlutenProfile_.ketoPreferenceLvl));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), GlutenProfile_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), GlutenProfile_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), GlutenProfile_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), GlutenProfile_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), GlutenProfile_.userId));
            }
        }
        return specification;
    }
}
