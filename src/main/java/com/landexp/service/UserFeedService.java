package com.landexp.service;

import com.landexp.domain.UserFeed;
import com.landexp.repository.UserFeedRepository;
import com.landexp.repository.search.UserFeedSearchRepository;
import com.landexp.service.dto.UserFeedDTO;
import com.landexp.service.mapper.UserFeedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserFeed.
 */
@Service
@Transactional
public class UserFeedService {

    private final Logger log = LoggerFactory.getLogger(UserFeedService.class);

    private final UserFeedRepository userFeedRepository;

    private final UserFeedMapper userFeedMapper;

    private final UserFeedSearchRepository userFeedSearchRepository;

    public UserFeedService(UserFeedRepository userFeedRepository, UserFeedMapper userFeedMapper, UserFeedSearchRepository userFeedSearchRepository) {
        this.userFeedRepository = userFeedRepository;
        this.userFeedMapper = userFeedMapper;
        this.userFeedSearchRepository = userFeedSearchRepository;
    }

    /**
     * Save a userFeed.
     *
     * @param userFeedDTO the entity to save
     * @return the persisted entity
     */
    public UserFeedDTO save(UserFeedDTO userFeedDTO) {
        log.debug("Request to save UserFeed : {}", userFeedDTO);
        UserFeed userFeed = userFeedMapper.toEntity(userFeedDTO);
        userFeed = userFeedRepository.save(userFeed);
        UserFeedDTO result = userFeedMapper.toDto(userFeed);
        userFeedSearchRepository.save(userFeed);
        return result;
    }

    /**
     * Get all the userFeeds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserFeedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserFeeds");
        return userFeedRepository.findAll(pageable)
            .map(userFeedMapper::toDto);
    }


    /**
     * Get one userFeed by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserFeedDTO> findOne(Long id) {
        log.debug("Request to get UserFeed : {}", id);
        return userFeedRepository.findById(id)
            .map(userFeedMapper::toDto);
    }

    /**
     * Delete the userFeed by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserFeed : {}", id);
        userFeedRepository.deleteById(id);
        userFeedSearchRepository.deleteById(id);
    }

    /**
     * Search for the userFeed corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserFeedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserFeeds for query {}", query);
        return userFeedSearchRepository.search(queryStringQuery(query), pageable)
            .map(userFeedMapper::toDto);
    }
}
