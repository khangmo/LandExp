package com.landexp.service;

import com.landexp.domain.Street;
import com.landexp.repository.StreetRepository;
import com.landexp.repository.search.StreetSearchRepository;
import com.landexp.service.dto.StreetDTO;
import com.landexp.service.mapper.StreetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Street.
 */
@Service
@Transactional
public class StreetService {

    private final Logger log = LoggerFactory.getLogger(StreetService.class);

    private final StreetRepository streetRepository;

    private final StreetMapper streetMapper;

    private final StreetSearchRepository streetSearchRepository;

    public StreetService(StreetRepository streetRepository, StreetMapper streetMapper, StreetSearchRepository streetSearchRepository) {
        this.streetRepository = streetRepository;
        this.streetMapper = streetMapper;
        this.streetSearchRepository = streetSearchRepository;
    }

    /**
     * Save a street.
     *
     * @param streetDTO the entity to save
     * @return the persisted entity
     */
    public StreetDTO save(StreetDTO streetDTO) {
        log.debug("Request to save Street : {}", streetDTO);
        Street street = streetMapper.toEntity(streetDTO);
        street = streetRepository.save(street);
        StreetDTO result = streetMapper.toDto(street);
        streetSearchRepository.save(street);
        return result;
    }

    /**
     * Get all the streets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StreetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Streets");
        return streetRepository.findAll(pageable)
            .map(streetMapper::toDto);
    }


    /**
     * Get one street by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StreetDTO> findOne(Long id) {
        log.debug("Request to get Street : {}", id);
        return streetRepository.findById(id)
            .map(streetMapper::toDto);
    }

    /**
     * Delete the street by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Street : {}", id);
        streetRepository.deleteById(id);
        streetSearchRepository.deleteById(id);
    }

    /**
     * Search for the street corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StreetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Streets for query {}", query);
        return streetSearchRepository.search(queryStringQuery(query), pageable)
            .map(streetMapper::toDto);
    }
}
