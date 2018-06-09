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

import com.landexp.domain.SearchTracking;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.SearchTrackingRepository;
import com.landexp.repository.search.SearchTrackingSearchRepository;
import com.landexp.service.dto.SearchTrackingCriteria;

import com.landexp.service.dto.SearchTrackingDTO;
import com.landexp.service.mapper.SearchTrackingMapper;

/**
 * Service for executing complex queries for SearchTracking entities in the database.
 * The main input is a {@link SearchTrackingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SearchTrackingDTO} or a {@link Page} of {@link SearchTrackingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SearchTrackingQueryService extends QueryService<SearchTracking> {

    private final Logger log = LoggerFactory.getLogger(SearchTrackingQueryService.class);

    private final SearchTrackingRepository searchTrackingRepository;

    private final SearchTrackingMapper searchTrackingMapper;

    private final SearchTrackingSearchRepository searchTrackingSearchRepository;

    public SearchTrackingQueryService(SearchTrackingRepository searchTrackingRepository, SearchTrackingMapper searchTrackingMapper, SearchTrackingSearchRepository searchTrackingSearchRepository) {
        this.searchTrackingRepository = searchTrackingRepository;
        this.searchTrackingMapper = searchTrackingMapper;
        this.searchTrackingSearchRepository = searchTrackingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SearchTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SearchTrackingDTO> findByCriteria(SearchTrackingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SearchTracking> specification = createSpecification(criteria);
        return searchTrackingMapper.toDto(searchTrackingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SearchTrackingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchTrackingDTO> findByCriteria(SearchTrackingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SearchTracking> specification = createSpecification(criteria);
        return searchTrackingRepository.findAll(specification, page)
            .map(searchTrackingMapper::toDto);
    }

    /**
     * Function to convert SearchTrackingCriteria to a {@link Specification}
     */
    private Specification<SearchTracking> createSpecification(SearchTrackingCriteria criteria) {
        Specification<SearchTracking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SearchTracking_.id));
            }
            if (criteria.getActionType() != null) {
                specification = specification.and(buildSpecification(criteria.getActionType(), SearchTracking_.actionType));
            }
            if (criteria.getKeyword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyword(), SearchTracking_.keyword));
            }
            if (criteria.getCostFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostFrom(), SearchTracking_.costFrom));
            }
            if (criteria.getCostTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostTo(), SearchTracking_.costTo));
            }
            if (criteria.getAcreageFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcreageFrom(), SearchTracking_.acreageFrom));
            }
            if (criteria.getAcreageTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcreageTo(), SearchTracking_.acreageTo));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildSpecification(criteria.getDirection(), SearchTracking_.direction));
            }
            if (criteria.getFloor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFloor(), SearchTracking_.floor));
            }
            if (criteria.getBathRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBathRoom(), SearchTracking_.bathRoom));
            }
            if (criteria.getBedRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBedRoom(), SearchTracking_.bedRoom));
            }
            if (criteria.getParking() != null) {
                specification = specification.and(buildSpecification(criteria.getParking(), SearchTracking_.parking));
            }
            if (criteria.getLandType() != null) {
                specification = specification.and(buildSpecification(criteria.getLandType(), SearchTracking_.landType));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), SearchTracking_.createAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), SearchTracking_.user, User_.id));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCityId(), SearchTracking_.city, City_.id));
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDistrictId(), SearchTracking_.district, District_.id));
            }
            if (criteria.getStreetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getStreetId(), SearchTracking_.street, Street_.id));
            }
        }
        return specification;
    }

}
