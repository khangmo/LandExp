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

import com.landexp.domain.UserSubscription;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.UserSubscriptionRepository;
import com.landexp.repository.search.UserSubscriptionSearchRepository;
import com.landexp.service.dto.UserSubscriptionCriteria;

import com.landexp.service.dto.UserSubscriptionDTO;
import com.landexp.service.mapper.UserSubscriptionMapper;

/**
 * Service for executing complex queries for UserSubscription entities in the database.
 * The main input is a {@link UserSubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserSubscriptionDTO} or a {@link Page} of {@link UserSubscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserSubscriptionQueryService extends QueryService<UserSubscription> {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionQueryService.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    private final UserSubscriptionSearchRepository userSubscriptionSearchRepository;

    public UserSubscriptionQueryService(UserSubscriptionRepository userSubscriptionRepository, UserSubscriptionMapper userSubscriptionMapper, UserSubscriptionSearchRepository userSubscriptionSearchRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
        this.userSubscriptionSearchRepository = userSubscriptionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserSubscriptionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserSubscriptionDTO> findByCriteria(UserSubscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserSubscription> specification = createSpecification(criteria);
        return userSubscriptionMapper.toDto(userSubscriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserSubscriptionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSubscriptionDTO> findByCriteria(UserSubscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserSubscription> specification = createSpecification(criteria);
        return userSubscriptionRepository.findAll(specification, page)
            .map(userSubscriptionMapper::toDto);
    }

    /**
     * Function to convert UserSubscriptionCriteria to a {@link Specification}
     */
    private Specification<UserSubscription> createSpecification(UserSubscriptionCriteria criteria) {
        Specification<UserSubscription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserSubscription_.id));
            }
            if (criteria.getActionType() != null) {
                specification = specification.and(buildSpecification(criteria.getActionType(), UserSubscription_.actionType));
            }
            if (criteria.getKeyword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyword(), UserSubscription_.keyword));
            }
            if (criteria.getCostFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostFrom(), UserSubscription_.costFrom));
            }
            if (criteria.getCostTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostTo(), UserSubscription_.costTo));
            }
            if (criteria.getAcreageFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcreageFrom(), UserSubscription_.acreageFrom));
            }
            if (criteria.getAcreageTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcreageTo(), UserSubscription_.acreageTo));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildSpecification(criteria.getDirection(), UserSubscription_.direction));
            }
            if (criteria.getFloor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFloor(), UserSubscription_.floor));
            }
            if (criteria.getBathRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBathRoom(), UserSubscription_.bathRoom));
            }
            if (criteria.getBedRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBedRoom(), UserSubscription_.bedRoom));
            }
            if (criteria.getParking() != null) {
                specification = specification.and(buildSpecification(criteria.getParking(), UserSubscription_.parking));
            }
            if (criteria.getLandType() != null) {
                specification = specification.and(buildSpecification(criteria.getLandType(), UserSubscription_.landType));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), UserSubscription_.enabled));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), UserSubscription_.createAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), UserSubscription_.updateAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), UserSubscription_.user, User_.id));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCityId(), UserSubscription_.city, City_.id));
            }
            if (criteria.getStreetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getStreetId(), UserSubscription_.street, Street_.id));
            }
        }
        return specification;
    }

}
