package com.landexp.service;

import com.landexp.domain.HousePhoto;
import com.landexp.repository.HousePhotoRepository;
import com.landexp.repository.search.HousePhotoSearchRepository;
import com.landexp.service.dto.HousePhotoDTO;
import com.landexp.service.mapper.HousePhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HousePhoto.
 */
@Service
@Transactional
public class HousePhotoService {

    private final Logger log = LoggerFactory.getLogger(HousePhotoService.class);

    private final HousePhotoRepository housePhotoRepository;

    private final HousePhotoMapper housePhotoMapper;

    private final HousePhotoSearchRepository housePhotoSearchRepository;

    public HousePhotoService(HousePhotoRepository housePhotoRepository, HousePhotoMapper housePhotoMapper, HousePhotoSearchRepository housePhotoSearchRepository) {
        this.housePhotoRepository = housePhotoRepository;
        this.housePhotoMapper = housePhotoMapper;
        this.housePhotoSearchRepository = housePhotoSearchRepository;
    }

    /**
     * Save a housePhoto.
     *
     * @param housePhotoDTO the entity to save
     * @return the persisted entity
     */
    public HousePhotoDTO save(HousePhotoDTO housePhotoDTO) {
        log.debug("Request to save HousePhoto : {}", housePhotoDTO);
        HousePhoto housePhoto = housePhotoMapper.toEntity(housePhotoDTO);
        housePhoto = housePhotoRepository.save(housePhoto);
        HousePhotoDTO result = housePhotoMapper.toDto(housePhoto);
        housePhotoSearchRepository.save(housePhoto);
        return result;
    }

    /**
     * Get all the housePhotos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HousePhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HousePhotos");
        return housePhotoRepository.findAll(pageable)
            .map(housePhotoMapper::toDto);
    }


    /**
     * Get one housePhoto by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HousePhotoDTO> findOne(Long id) {
        log.debug("Request to get HousePhoto : {}", id);
        return housePhotoRepository.findById(id)
            .map(housePhotoMapper::toDto);
    }

    /**
     * Delete the housePhoto by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HousePhoto : {}", id);
        housePhotoRepository.deleteById(id);
        housePhotoSearchRepository.deleteById(id);
    }

    /**
     * Search for the housePhoto corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HousePhotoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HousePhotos for query {}", query);
        return housePhotoSearchRepository.search(queryStringQuery(query), pageable)
            .map(housePhotoMapper::toDto);
    }
}
