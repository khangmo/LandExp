package com.landexp.service;

import com.landexp.domain.LandProjects;
import com.landexp.repository.LandProjectsRepository;
import com.landexp.repository.search.LandProjectsSearchRepository;
import com.landexp.service.dto.LandProjectsDTO;
import com.landexp.service.mapper.LandProjectsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LandProjects.
 */
@Service
@Transactional
public class LandProjectsService {

    private final Logger log = LoggerFactory.getLogger(LandProjectsService.class);

    private final LandProjectsRepository landProjectsRepository;

    private final LandProjectsMapper landProjectsMapper;

    private final LandProjectsSearchRepository landProjectsSearchRepository;

    public LandProjectsService(LandProjectsRepository landProjectsRepository, LandProjectsMapper landProjectsMapper, LandProjectsSearchRepository landProjectsSearchRepository) {
        this.landProjectsRepository = landProjectsRepository;
        this.landProjectsMapper = landProjectsMapper;
        this.landProjectsSearchRepository = landProjectsSearchRepository;
    }

    /**
     * Save a landProjects.
     *
     * @param landProjectsDTO the entity to save
     * @return the persisted entity
     */
    public LandProjectsDTO save(LandProjectsDTO landProjectsDTO) {
        log.debug("Request to save LandProjects : {}", landProjectsDTO);
        LandProjects landProjects = landProjectsMapper.toEntity(landProjectsDTO);
        landProjects = landProjectsRepository.save(landProjects);
        LandProjectsDTO result = landProjectsMapper.toDto(landProjects);
        landProjectsSearchRepository.save(landProjects);
        return result;
    }

    /**
     * Get all the landProjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LandProjectsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LandProjects");
        return landProjectsRepository.findAll(pageable)
            .map(landProjectsMapper::toDto);
    }


    /**
     * Get one landProjects by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LandProjectsDTO> findOne(Long id) {
        log.debug("Request to get LandProjects : {}", id);
        return landProjectsRepository.findById(id)
            .map(landProjectsMapper::toDto);
    }

    /**
     * Delete the landProjects by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LandProjects : {}", id);
        landProjectsRepository.deleteById(id);
        landProjectsSearchRepository.deleteById(id);
    }

    /**
     * Search for the landProjects corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LandProjectsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LandProjects for query {}", query);
        return landProjectsSearchRepository.search(queryStringQuery(query), pageable)
            .map(landProjectsMapper::toDto);
    }
}
