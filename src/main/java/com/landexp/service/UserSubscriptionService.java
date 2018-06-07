package com.landexp.service;

import com.landexp.domain.UserSubscription;
import com.landexp.repository.UserSubscriptionRepository;
import com.landexp.repository.search.UserSubscriptionSearchRepository;
import com.landexp.service.dto.UserSubscriptionDTO;
import com.landexp.service.mapper.UserSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserSubscription.
 */
@Service
@Transactional
public class UserSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionService.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    private final UserSubscriptionSearchRepository userSubscriptionSearchRepository;

    public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository, UserSubscriptionMapper userSubscriptionMapper, UserSubscriptionSearchRepository userSubscriptionSearchRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
        this.userSubscriptionSearchRepository = userSubscriptionSearchRepository;
    }

    /**
     * Save a userSubscription.
     *
     * @param userSubscriptionDTO the entity to save
     * @return the persisted entity
     */
    public UserSubscriptionDTO save(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to save UserSubscription : {}", userSubscriptionDTO);
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription = userSubscriptionRepository.save(userSubscription);
        UserSubscriptionDTO result = userSubscriptionMapper.toDto(userSubscription);
        userSubscriptionSearchRepository.save(userSubscription);
        return result;
    }

    /**
     * Get all the userSubscriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSubscriptions");
        return userSubscriptionRepository.findAll(pageable)
            .map(userSubscriptionMapper::toDto);
    }


    /**
     * Get one userSubscription by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get UserSubscription : {}", id);
        return userSubscriptionRepository.findById(id)
            .map(userSubscriptionMapper::toDto);
    }

    /**
     * Delete the userSubscription by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserSubscription : {}", id);
        userSubscriptionRepository.deleteById(id);
        userSubscriptionSearchRepository.deleteById(id);
    }

    /**
     * Search for the userSubscription corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserSubscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserSubscriptions for query {}", query);
        return userSubscriptionSearchRepository.search(queryStringQuery(query), pageable)
            .map(userSubscriptionMapper::toDto);
    }
}
