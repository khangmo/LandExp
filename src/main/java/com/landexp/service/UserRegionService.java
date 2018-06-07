package com.landexp.service;

import com.landexp.domain.UserRegion;
import com.landexp.repository.UserRegionRepository;
import com.landexp.repository.search.UserRegionSearchRepository;
import com.landexp.service.dto.UserRegionDTO;
import com.landexp.service.mapper.UserRegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRegion.
 */
@Service
@Transactional
public class UserRegionService {

    private final Logger log = LoggerFactory.getLogger(UserRegionService.class);

    private final UserRegionRepository userRegionRepository;

    private final UserRegionMapper userRegionMapper;

    private final UserRegionSearchRepository userRegionSearchRepository;

    public UserRegionService(UserRegionRepository userRegionRepository, UserRegionMapper userRegionMapper, UserRegionSearchRepository userRegionSearchRepository) {
        this.userRegionRepository = userRegionRepository;
        this.userRegionMapper = userRegionMapper;
        this.userRegionSearchRepository = userRegionSearchRepository;
    }

    /**
     * Save a userRegion.
     *
     * @param userRegionDTO the entity to save
     * @return the persisted entity
     */
    public UserRegionDTO save(UserRegionDTO userRegionDTO) {
        log.debug("Request to save UserRegion : {}", userRegionDTO);
        UserRegion userRegion = userRegionMapper.toEntity(userRegionDTO);
        userRegion = userRegionRepository.save(userRegion);
        UserRegionDTO result = userRegionMapper.toDto(userRegion);
        userRegionSearchRepository.save(userRegion);
        return result;
    }

    /**
     * Get all the userRegions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserRegionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRegions");
        return userRegionRepository.findAll(pageable)
            .map(userRegionMapper::toDto);
    }

    /**
     * Get all the UserRegion with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<UserRegionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userRegionRepository.findAllWithEagerRelationships(pageable).map(userRegionMapper::toDto);
    }
    

    /**
     * Get one userRegion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserRegionDTO> findOne(Long id) {
        log.debug("Request to get UserRegion : {}", id);
        return userRegionRepository.findOneWithEagerRelationships(id)
            .map(userRegionMapper::toDto);
    }

    /**
     * Delete the userRegion by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserRegion : {}", id);
        userRegionRepository.deleteById(id);
        userRegionSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRegion corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserRegionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRegions for query {}", query);
        return userRegionSearchRepository.search(queryStringQuery(query), pageable)
            .map(userRegionMapper::toDto);
    }
}
