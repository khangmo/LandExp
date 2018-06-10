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

import com.landexp.domain.House;
import com.landexp.domain.*; // for static metamodels
import com.landexp.repository.HouseRepository;
import com.landexp.repository.search.HouseSearchRepository;
import com.landexp.service.dto.HouseCriteria;

import com.landexp.service.dto.HouseDTO;
import com.landexp.service.mapper.HouseMapper;

/**
 * Service for executing complex queries for House entities in the database.
 * The main input is a {@link HouseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HouseDTO} or a {@link Page} of {@link HouseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HouseQueryService extends QueryService<House> {

    private final Logger log = LoggerFactory.getLogger(HouseQueryService.class);

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper;

    private final HouseSearchRepository houseSearchRepository;

    public HouseQueryService(HouseRepository houseRepository, HouseMapper houseMapper, HouseSearchRepository houseSearchRepository) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
        this.houseSearchRepository = houseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HouseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HouseDTO> findByCriteria(HouseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<House> specification = createSpecification(criteria);
        return houseMapper.toDto(houseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HouseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> findByCriteria(HouseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<House> specification = createSpecification(criteria);
        return houseRepository.findAll(specification, page)
            .map(houseMapper::toDto);
    }

    /**
     * Function to convert HouseCriteria to a {@link Specification}
     */
    private Specification<House> createSpecification(HouseCriteria criteria) {
        Specification<House> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), House_.id));
            }
            if (criteria.getAvatar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvatar(), House_.avatar));
            }
            if (criteria.getActionType() != null) {
                specification = specification.and(buildSpecification(criteria.getActionType(), House_.actionType));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), House_.address));
            }
            if (criteria.getMoney() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMoney(), House_.money));
            }
            if (criteria.getMoneyType() != null) {
                specification = specification.and(buildSpecification(criteria.getMoneyType(), House_.moneyType));
            }
            if (criteria.getAcreage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAcreage(), House_.acreage));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), House_.discount));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildSpecification(criteria.getDirection(), House_.direction));
            }
            if (criteria.getDirectionBalcony() != null) {
                specification = specification.and(buildSpecification(criteria.getDirectionBalcony(), House_.directionBalcony));
            }
            if (criteria.getFloor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFloor(), House_.floor));
            }
            if (criteria.getNumberOfFloor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfFloor(), House_.numberOfFloor));
            }
            if (criteria.getBathRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBathRoom(), House_.bathRoom));
            }
            if (criteria.getBedRoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBedRoom(), House_.bedRoom));
            }
            if (criteria.getParking() != null) {
                specification = specification.and(buildSpecification(criteria.getParking(), House_.parking));
            }
            if (criteria.getFurniture() != null) {
                specification = specification.and(buildSpecification(criteria.getFurniture(), House_.furniture));
            }
            if (criteria.getLandType() != null) {
                specification = specification.and(buildSpecification(criteria.getLandType(), House_.landType));
            }
            if (criteria.getSaleType() != null) {
                specification = specification.and(buildSpecification(criteria.getSaleType(), House_.saleType));
            }
            if (criteria.getFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFee(), House_.fee));
            }
            if (criteria.getFeeMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeMax(), House_.feeMax));
            }
            if (criteria.getPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getPresent(), House_.present));
            }
            if (criteria.getHits() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHits(), House_.hits));
            }
            if (criteria.getStatusType() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusType(), House_.statusType));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), House_.createAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), House_.updateAt));
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDistrictId(), House_.district, District_.id));
            }
            if (criteria.getPhotosId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPhotosId(), House_.photos, HousePhoto_.id));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCityId(), House_.city, City_.id));
            }
            if (criteria.getStreetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getStreetId(), House_.street, Street_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), House_.project, LandProject_.id));
            }
            if (criteria.getCreateById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreateById(), House_.createBy, User_.id));
            }
            if (criteria.getUpdateById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUpdateById(), House_.updateBy, User_.id));
            }
        }
        return specification;
    }

}
