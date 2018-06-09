package com.landexp.service;

import com.landexp.domain.LandProject;
import com.landexp.repository.LandProjectRepository;
import com.landexp.repository.search.LandProjectSearchRepository;
import com.landexp.service.dto.LandProjectDTO;
import com.landexp.service.mapper.LandProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LandProject.
 */
@Service
@Transactional
public class LandProjectService {

    private final Logger log = LoggerFactory.getLogger(LandProjectService.class);

    private final LandProjectRepository landProjectRepository;

    private final LandProjectMapper landProjectMapper;

    private final LandProjectSearchRepository landProjectSearchRepository;

    public LandProjectService(LandProjectRepository landProjectRepository, LandProjectMapper landProjectMapper, LandProjectSearchRepository landProjectSearchRepository) {
        this.landProjectRepository = landProjectRepository;
        this.landProjectMapper = landProjectMapper;
        this.landProjectSearchRepository = landProjectSearchRepository;
    }

    /**
     * Save a landProject.
     *
     * @param landProjectDTO the entity to save
     * @return the persisted entity
     */
    public LandProjectDTO save(LandProjectDTO landProjectDTO) {
        log.debug("Request to save LandProject : {}", landProjectDTO);
        LandProject landProject = landProjectMapper.toEntity(landProjectDTO);
        landProject = landProjectRepository.save(landProject);
        LandProjectDTO result = landProjectMapper.toDto(landProject);
        landProjectSearchRepository.save(landProject);
        return result;
    }

    /**
     * Get all the landProjects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LandProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LandProjects");
        return landProjectRepository.findAll(pageable)
            .map(landProjectMapper::toDto);
    }


    /**
     * Get one landProject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LandProjectDTO> findOne(Long id) {
        log.debug("Request to get LandProject : {}", id);
        return landProjectRepository.findById(id)
            .map(landProjectMapper::toDto);
    }

    /**
     * Delete the landProject by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LandProject : {}", id);
        landProjectRepository.deleteById(id);
        landProjectSearchRepository.deleteById(id);
    }

    /**
     * Search for the landProject corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LandProjectDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LandProjects for query {}", query);
        return landProjectSearchRepository.search(queryStringQuery(query), pageable)
            .map(landProjectMapper::toDto);
    }
}
