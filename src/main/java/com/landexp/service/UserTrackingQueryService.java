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

import com.landexp.domain.UserTracking;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.UserTrackingRepository;
import com.landexp.repository.search.UserTrackingSearchRepository;
import com.landexp.service.dto.UserTrackingCriteria;

import com.landexp.service.dto.UserTrackingDTO;
import com.landexp.service.mapper.UserTrackingMapper;

/**
 * Service for executing complex queries for UserTracking entities in the database.
 * The main input is a {@link UserTrackingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserTrackingDTO} or a {@link Page} of {@link UserTrackingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserTrackingQueryService extends QueryService<UserTracking> {

    private final Logger log = LoggerFactory.getLogger(UserTrackingQueryService.class);

    private final UserTrackingRepository userTrackingRepository;

    private final UserTrackingMapper userTrackingMapper;

    private final UserTrackingSearchRepository userTrackingSearchRepository;

    public UserTrackingQueryService(UserTrackingRepository userTrackingRepository, UserTrackingMapper userTrackingMapper, UserTrackingSearchRepository userTrackingSearchRepository) {
        this.userTrackingRepository = userTrackingRepository;
        this.userTrackingMapper = userTrackingMapper;
        this.userTrackingSearchRepository = userTrackingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link UserTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserTrackingDTO> findByCriteria(UserTrackingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserTracking> specification = createSpecification(criteria);
        return userTrackingMapper.toDto(userTrackingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserTrackingDTO> findByCriteria(UserTrackingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserTracking> specification = createSpecification(criteria);
        return userTrackingRepository.findAll(specification, page)
            .map(userTrackingMapper::toDto);
    }

    /**
     * Function to convert UserTrackingCriteria to a {@link Specification}
     */
    private Specification<UserTracking> createSpecification(UserTrackingCriteria criteria) {
        Specification<UserTracking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), UserTracking_.id));
            }
            if (criteria.getActivityType() != null) {
                specification = specification.and(buildSpecification(criteria.getActivityType(), UserTracking_.activityType));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceId(), UserTracking_.sourceId));
            }
            if (criteria.getSourceLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceLink(), UserTracking_.sourceLink));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), UserTracking_.description));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), UserTracking_.createAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), UserTracking_.user, User_.id));
            }
        }
        return specification;
    }

}
