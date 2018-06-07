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

import com.landexp.domain.HouseTracking;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.HouseTrackingRepository;
import com.landexp.repository.search.HouseTrackingSearchRepository;
import com.landexp.service.dto.HouseTrackingCriteria;

import com.landexp.service.dto.HouseTrackingDTO;
import com.landexp.service.mapper.HouseTrackingMapper;

/**
 * Service for executing complex queries for HouseTracking entities in the database.
 * The main input is a {@link HouseTrackingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HouseTrackingDTO} or a {@link Page} of {@link HouseTrackingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HouseTrackingQueryService extends QueryService<HouseTracking> {

    private final Logger log = LoggerFactory.getLogger(HouseTrackingQueryService.class);

    private final HouseTrackingRepository houseTrackingRepository;

    private final HouseTrackingMapper houseTrackingMapper;

    private final HouseTrackingSearchRepository houseTrackingSearchRepository;

    public HouseTrackingQueryService(HouseTrackingRepository houseTrackingRepository, HouseTrackingMapper houseTrackingMapper, HouseTrackingSearchRepository houseTrackingSearchRepository) {
        this.houseTrackingRepository = houseTrackingRepository;
        this.houseTrackingMapper = houseTrackingMapper;
        this.houseTrackingSearchRepository = houseTrackingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HouseTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HouseTrackingDTO> findByCriteria(HouseTrackingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HouseTracking> specification = createSpecification(criteria);
        return houseTrackingMapper.toDto(houseTrackingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HouseTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseTrackingDTO> findByCriteria(HouseTrackingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HouseTracking> specification = createSpecification(criteria);
        return houseTrackingRepository.findAll(specification, page)
            .map(houseTrackingMapper::toDto);
    }

    /**
     * Function to convert HouseTrackingCriteria to a {@link Specification}
     */
    private Specification<HouseTracking> createSpecification(HouseTrackingCriteria criteria) {
        Specification<HouseTracking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HouseTracking_.id));
            }
            if (criteria.getActivityType() != null) {
                specification = specification.and(buildSpecification(criteria.getActivityType(), HouseTracking_.activityType));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceId(), HouseTracking_.sourceId));
            }
            if (criteria.getSourceLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceLink(), HouseTracking_.sourceLink));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HouseTracking_.description));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), HouseTracking_.createAt));
            }
            if (criteria.getHouseId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getHouseId(), HouseTracking_.house, House_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), HouseTracking_.user, User_.id));
            }
        }
        return specification;
    }

}
