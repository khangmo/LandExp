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

import com.landexp.domain.UserFinancial;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.UserFinancialRepository;
import com.landexp.repository.search.UserFinancialSearchRepository;
import com.landexp.service.dto.UserFinancialCriteria;

import com.landexp.service.dto.UserFinancialDTO;
import com.landexp.service.mapper.UserFinancialMapper;

/**
 * Service for executing complex queries for UserFinancial entities in the database.
 * The main input is a {@link UserFinancialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserFinancialDTO} or a {@link Page} of {@link UserFinancialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserFinancialQueryService extends QueryService<UserFinancial> {

    private final Logger log = LoggerFactory.getLogger(UserFinancialQueryService.class);

    private final UserFinancialRepository userFinancialRepository;

    private final UserFinancialMapper userFinancialMapper;

    private final UserFinancialSearchRepository userFinancialSearchRepository;

    public UserFinancialQueryService(UserFinancialRepository userFinancialRepository, UserFinancialMapper userFinancialMapper, UserFinancialSearchRepository userFinancialSearchRepository) {
        this.userFinancialRepository = userFinancialRepository;
        this.userFinancialMapper = userFinancialMapper;
        this.userFinancialSearchRepository = userFinancialSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserFinancialDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserFinancialDTO> findByCriteria(UserFinancialCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserFinancial> specification = createSpecification(criteria);
        return userFinancialMapper.toDto(userFinancialRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserFinancialDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserFinancialDTO> findByCriteria(UserFinancialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserFinancial> specification = createSpecification(criteria);
        return userFinancialRepository.findAll(specification, page)
            .map(userFinancialMapper::toDto);
    }

    /**
     * Function to convert UserFinancialCriteria to a {@link Specification}
     */
    private Specification<UserFinancial> createSpecification(UserFinancialCriteria criteria) {
        Specification<UserFinancial> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserFinancial_.id));
            }
            if (criteria.getHousePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHousePrice(), UserFinancial_.housePrice));
            }
            if (criteria.getHouseAcreage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHouseAcreage(), UserFinancial_.houseAcreage));
            }
            if (criteria.getLoanRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoanRate(), UserFinancial_.loanRate));
            }
            if (criteria.getLoanFromPeople() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoanFromPeople(), UserFinancial_.loanFromPeople));
            }
            if (criteria.getCustomerMoneyHave() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerMoneyHave(), UserFinancial_.customerMoneyHave));
            }
            if (criteria.getCustomerMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerMobile(), UserFinancial_.customerMobile));
            }
            if (criteria.getCustomerEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerEmail(), UserFinancial_.customerEmail));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), UserFinancial_.user, User_.id));
            }
        }
        return specification;
    }

}
