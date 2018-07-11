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

import com.landexp.domain.Ward;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.WardRepository;
import com.landexp.service.dto.WardCriteria;

import com.landexp.service.dto.WardDTO;
import com.landexp.service.mapper.WardMapper;

/**
 * Service for executing complex queries for Ward entities in the database.
 * The main input is a {@link WardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WardDTO} or a {@link Page} of {@link WardDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WardQueryService extends QueryService<Ward> {

    private final Logger log = LoggerFactory.getLogger(WardQueryService.class);

    private final WardRepository wardRepository;

    private final WardMapper wardMapper;

    public WardQueryService(WardRepository wardRepository, WardMapper wardMapper) {
        this.wardRepository = wardRepository;
        this.wardMapper = wardMapper;
    }

    /**
     * Return a {@link List} of {@link WardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WardDTO> findByCriteria(WardCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ward> specification = createSpecification(criteria);
        return wardMapper.toDto(wardRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WardDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WardDTO> findByCriteria(WardCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ward> specification = createSpecification(criteria);
        return wardRepository.findAll(specification, page)
            .map(wardMapper::toDto);
    }

    /**
     * Function to convert WardCriteria to a {@link Specification}
     */
    private Specification<Ward> createSpecification(WardCriteria criteria) {
        Specification<Ward> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ward_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ward_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Ward_.enabled));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), Ward_.createAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), Ward_.updateAt));
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDistrictId(), Ward_.district, District_.id));
            }
        }
        return specification;
    }

}
