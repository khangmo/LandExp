package com.landexp.service;

import com.landexp.domain.UserFinancial;
import com.landexp.repository.UserFinancialRepository;
import com.landexp.repository.search.UserFinancialSearchRepository;
import com.landexp.service.dto.UserFinancialDTO;
import com.landexp.service.mapper.UserFinancialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserFinancial.
 */
@Service
@Transactional
public class UserFinancialService {

    private final Logger log = LoggerFactory.getLogger(UserFinancialService.class);

    private final UserFinancialRepository userFinancialRepository;

    private final UserFinancialMapper userFinancialMapper;

    private final UserFinancialSearchRepository userFinancialSearchRepository;

    public UserFinancialService(UserFinancialRepository userFinancialRepository, UserFinancialMapper userFinancialMapper, UserFinancialSearchRepository userFinancialSearchRepository) {
        this.userFinancialRepository = userFinancialRepository;
        this.userFinancialMapper = userFinancialMapper;
        this.userFinancialSearchRepository = userFinancialSearchRepository;
    }

    /**
     * Save a userFinancial.
     *
     * @param userFinancialDTO the entity to save
     * @return the persisted entity
     */
    public UserFinancialDTO save(UserFinancialDTO userFinancialDTO) {
        log.debug("Request to save UserFinancial : {}", userFinancialDTO);
        UserFinancial userFinancial = userFinancialMapper.toEntity(userFinancialDTO);
        userFinancial = userFinancialRepository.save(userFinancial);
        UserFinancialDTO result = userFinancialMapper.toDto(userFinancial);
        userFinancialSearchRepository.save(userFinancial);
        return result;
    }

    /**
     * Get all the userFinancials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserFinancialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserFinancials");
        return userFinancialRepository.findAll(pageable)
            .map(userFinancialMapper::toDto);
    }


    /**
     * Get one userFinancial by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserFinancialDTO> findOne(Long id) {
        log.debug("Request to get UserFinancial : {}", id);
        return userFinancialRepository.findById(id)
            .map(userFinancialMapper::toDto);
    }

    /**
     * Delete the userFinancial by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserFinancial : {}", id);
        userFinancialRepository.deleteById(id);
        userFinancialSearchRepository.deleteById(id);
    }

    /**
     * Search for the userFinancial corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserFinancialDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserFinancials for query {}", query);
        return userFinancialSearchRepository.search(queryStringQuery(query), pageable)
            .map(userFinancialMapper::toDto);
    }
}
