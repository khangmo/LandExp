package com.landexp.service;

import com.landexp.domain.SearchTracking;
import com.landexp.repository.SearchTrackingRepository;
import com.landexp.repository.search.SearchTrackingSearchRepository;
import com.landexp.service.dto.SearchTrackingDTO;
import com.landexp.service.mapper.SearchTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SearchTracking.
 */
@Service
@Transactional
public class SearchTrackingService {

    private final Logger log = LoggerFactory.getLogger(SearchTrackingService.class);

    private final SearchTrackingRepository searchTrackingRepository;

    private final SearchTrackingMapper searchTrackingMapper;

    private final SearchTrackingSearchRepository searchTrackingSearchRepository;

    public SearchTrackingService(SearchTrackingRepository searchTrackingRepository, SearchTrackingMapper searchTrackingMapper, SearchTrackingSearchRepository searchTrackingSearchRepository) {
        this.searchTrackingRepository = searchTrackingRepository;
        this.searchTrackingMapper = searchTrackingMapper;
        this.searchTrackingSearchRepository = searchTrackingSearchRepository;
    }

    /**
     * Save a searchTracking.
     *
     * @param searchTrackingDTO the entity to save
     * @return the persisted entity
     */
    public SearchTrackingDTO save(SearchTrackingDTO searchTrackingDTO) {
        log.debug("Request to save SearchTracking : {}", searchTrackingDTO);
        SearchTracking searchTracking = searchTrackingMapper.toEntity(searchTrackingDTO);
        searchTracking = searchTrackingRepository.save(searchTracking);
        SearchTrackingDTO result = searchTrackingMapper.toDto(searchTracking);
        searchTrackingSearchRepository.save(searchTracking);
        return result;
    }

    /**
     * Get all the searchTrackings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SearchTrackingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SearchTrackings");
        return searchTrackingRepository.findAll(pageable)
            .map(searchTrackingMapper::toDto);
    }


    /**
     * Get one searchTracking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SearchTrackingDTO> findOne(Long id) {
        log.debug("Request to get SearchTracking : {}", id);
        return searchTrackingRepository.findById(id)
            .map(searchTrackingMapper::toDto);
    }

    /**
     * Delete the searchTracking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchTracking : {}", id);
        searchTrackingRepository.deleteById(id);
        searchTrackingSearchRepository.deleteById(id);
    }

    /**
     * Search for the searchTracking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SearchTrackingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SearchTrackings for query {}", query);
        return searchTrackingSearchRepository.search(queryStringQuery(query), pageable)
            .map(searchTrackingMapper::toDto);
    }
}
