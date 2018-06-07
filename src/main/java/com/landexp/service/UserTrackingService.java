package com.landexp.service;

import com.landexp.domain.UserTracking;
import com.landexp.repository.UserTrackingRepository;
import com.landexp.repository.search.UserTrackingSearchRepository;
import com.landexp.service.dto.UserTrackingDTO;
import com.landexp.service.mapper.UserTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserTracking.
 */
@Service
@Transactional
public class UserTrackingService {

    private final Logger log = LoggerFactory.getLogger(UserTrackingService.class);

    private final UserTrackingRepository userTrackingRepository;

    private final UserTrackingMapper userTrackingMapper;

    private final UserTrackingSearchRepository userTrackingSearchRepository;

    public UserTrackingService(UserTrackingRepository userTrackingRepository, UserTrackingMapper userTrackingMapper, UserTrackingSearchRepository userTrackingSearchRepository) {
        this.userTrackingRepository = userTrackingRepository;
        this.userTrackingMapper = userTrackingMapper;
        this.userTrackingSearchRepository = userTrackingSearchRepository;
    }

    /**
     * Save a userTracking.
     *
     * @param userTrackingDTO the entity to save
     * @return the persisted entity
     */
    public UserTrackingDTO save(UserTrackingDTO userTrackingDTO) {
        log.debug("Request to save UserTracking : {}", userTrackingDTO);
        UserTracking userTracking = userTrackingMapper.toEntity(userTrackingDTO);
        userTracking = userTrackingRepository.save(userTracking);
        UserTrackingDTO result = userTrackingMapper.toDto(userTracking);
        userTrackingSearchRepository.save(userTracking);
        return result;
    }

    /**
     * Get all the userTrackings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserTrackingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserTrackings");
        return userTrackingRepository.findAll(pageable)
            .map(userTrackingMapper::toDto);
    }


    /**
     * Get one userTracking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserTrackingDTO> findOne(Long id) {
        log.debug("Request to get UserTracking : {}", id);
        return userTrackingRepository.findById(id)
            .map(userTrackingMapper::toDto);
    }

    /**
     * Delete the userTracking by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserTracking : {}", id);
        userTrackingRepository.deleteById(id);
        userTrackingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userTracking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserTrackingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserTrackings for query {}", query);
        return userTrackingSearchRepository.search(queryStringQuery(query), pageable)
            .map(userTrackingMapper::toDto);
    }
}
