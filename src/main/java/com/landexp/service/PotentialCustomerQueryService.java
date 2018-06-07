package com.landexp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.landexp.domain.PotentialCustomer;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.PotentialCustomerRepository;
import com.landexp.repository.search.PotentialCustomerSearchRepository;
import com.landexp.service.dto.PotentialCustomerCriteria;

import com.landexp.service.dto.PotentialCustomerDTO;
import com.landexp.service.mapper.PotentialCustomerMapper;

/**
 * Service for executing complex queries for PotentialCustomer entities in the database.
 * The main input is a {@link PotentialCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PotentialCustomerDTO} or a {@link Page} of {@link PotentialCustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PotentialCustomerQueryService extends QueryService<PotentialCustomer> {

    private final Logger log = LoggerFactory.getLogger(PotentialCustomerQueryService.class);

    private final PotentialCustomerRepository potentialCustomerRepository;

    private final PotentialCustomerMapper potentialCustomerMapper;

    private final PotentialCustomerSearchRepository potentialCustomerSearchRepository;

    public PotentialCustomerQueryService(PotentialCustomerRepository potentialCustomerRepository, PotentialCustomerMapper potentialCustomerMapper, PotentialCustomerSearchRepository potentialCustomerSearchRepository) {
        this.potentialCustomerRepository = potentialCustomerRepository;
        this.potentialCustomerMapper = potentialCustomerMapper;
        this.potentialCustomerSearchRepository = potentialCustomerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PotentialCustomerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PotentialCustomerDTO> findByCriteria(PotentialCustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PotentialCustomer> specification = createSpecification(criteria);
        return potentialCustomerMapper.toDto(potentialCustomerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PotentialCustomerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PotentialCustomerDTO> findByCriteria(PotentialCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PotentialCustomer> specification = createSpecification(criteria);
        return potentialCustomerRepository.findAll(specification, page)
            .map(potentialCustomerMapper::toDto);
    }

    /**
     * Function to convert PotentialCustomerCriteria to a {@link Specification}
     */
    private Specification<PotentialCustomer> createSpecification(PotentialCustomerCriteria criteria) {
        Specification<PotentialCustomer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PotentialCustomer_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), PotentialCustomer_.level));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PotentialCustomer_.description));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), PotentialCustomer_.createAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), PotentialCustomer_.updateAt));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCustomerId(), PotentialCustomer_.customer, User_.id));
            }
            if (criteria.getCreateById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreateById(), PotentialCustomer_.createBy, User_.id));
            }
            if (criteria.getUpdateById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUpdateById(), PotentialCustomer_.updateBy, User_.id));
            }
        }
        return specification;
    }

}
