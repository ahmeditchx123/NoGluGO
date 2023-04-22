package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.repository.AddressRepository;
import com.noglugo.mvp.service.criteria.AddressCriteria;
import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.mapper.AddressMapper;
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
 * Service for executing complex queries for {@link Address} entities in the database.
 * The main input is a {@link AddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddressDTO} or a {@link Page} of {@link AddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressQueryService extends QueryService<Address> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    public AddressQueryService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Return a {@link List} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> findByCriteria(AddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Address> specification = createSpecification(criteria);
        return addressMapper.toDto(addressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findByCriteria(AddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.findAll(specification, page).map(addressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.count(specification);
    }

    /**
     * Function to convert {@link AddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Address> createSpecification(AddressCriteria criteria) {
        Specification<Address> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Address_.id));
            }
            if (criteria.getStreet1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet1(), Address_.street1));
            }
            if (criteria.getStreet2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet2(), Address_.street2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildSpecification(criteria.getCity(), Address_.city));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Address_.postalCode));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Address_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Address_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Address_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Address_.lastModifiedDate));
            }
            if (criteria.getOrderSAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderSAId(), root -> root.join(Address_.orderSA, JoinType.LEFT).get(Order_.id))
                    );
            }
            if (criteria.getOrderBAId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderBAId(), root -> root.join(Address_.orderBA, JoinType.LEFT).get(Order_.id))
                    );
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoreId(), root -> root.join(Address_.store, JoinType.LEFT).get(Store_.id))
                    );
            }
            if (criteria.getRestaurantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRestaurantId(),
                            root -> root.join(Address_.restaurant, JoinType.LEFT).get(Restaurant_.id)
                        )
                    );
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLocationId(), root -> root.join(Address_.location, JoinType.LEFT).get(Location_.id))
                    );
            }
        }
        return specification;
    }
}
