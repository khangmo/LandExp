package com.landexp.service;

import com.landexp.domain.HouseTracking;
import com.landexp.repository.HouseTrackingRepository;
import com.landexp.repository.search.HouseTrackingSearchRepository;
import com.landexp.service.dto.HouseTrackingDTO;
import com.landexp.service.mapper.HouseTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HouseTracking.
 */
@Service
@Transactional
public class HouseTrackingService {

    private final Logger log = LoggerFactory.getLogger(HouseTrackingService.class);

    private final HouseTrackingRepository houseTrackingRepository;

    private final HouseTrackingMapper houseTrackingMapper;

    private final HouseTrackingSearchRepository houseTrackingSearchRepository;

    public HouseTrackingService(HouseTrackingRepository houseTrackingRepository, HouseTrackingMapper houseTrackingMapper, HouseTrackingSearchRepository houseTrackingSearchRepository) {
        this.houseTrackingRepository = houseTrackingRepository;
        this.houseTrackingMapper = houseTrackingMapper;
        this.houseTrackingSearchRepository = houseTrackingSearchRepository;
    }

    /**
     * Save a houseTracking.
     *
     * @param houseTrackingDTO the entity to save
     * @return the persisted entity
     */
    public HouseTrackingDTO save(HouseTrackingDTO houseTrackingDTO) {
        log.debug("Request to save HouseTracking : {}", houseTrackingDTO);
        HouseTracking houseTracking = houseTrackingMapper.toEntity(houseTrackingDTO);
        houseTracking = houseTrackingRepository.save(houseTracking);
        HouseTrackingDTO result = houseTrackingMapper.toDto(houseTracking);
        houseTrackingSearchRepository.save(houseTracking);
        return result;
    }

    /**
     * Get all the houseTrackings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseTrackingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HouseTrackings");
        return houseTrackingRepository.findAll(pageable)
            .map(houseTrackingMapper::toDto);
    }


    /**
     * Get one houseTracking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HouseTrackingDTO> findOne(Long id) {
        log.debug("Request to get HouseTracking : {}", id);
        return houseTrackingRepository.findById(id)
            .map(houseTrackingMapper::toDto);
    }

    /**
     * Delete the houseTracking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HouseTracking : {}", id);
        houseTrackingRepository.deleteById(id);
        houseTrackingSearchRepository.deleteById(id);
    }

    /**
     * Search for the houseTracking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseTrackingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HouseTrackings for query {}", query);
        return houseTrackingSearchRepository.search(queryStringQuery(query), pageable)
            .map(houseTrackingMapper::toDto);
    }
}
