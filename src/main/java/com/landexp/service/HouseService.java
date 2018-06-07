package com.landexp.service;

import com.landexp.domain.House;
import com.landexp.repository.HouseRepository;
import com.landexp.repository.search.HouseSearchRepository;
import com.landexp.service.dto.HouseDTO;
import com.landexp.service.mapper.HouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing House.
 */
@Service
@Transactional
public class HouseService {

    private final Logger log = LoggerFactory.getLogger(HouseService.class);

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper;

    private final HouseSearchRepository houseSearchRepository;

    public HouseService(HouseRepository houseRepository, HouseMapper houseMapper, HouseSearchRepository houseSearchRepository) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
        this.houseSearchRepository = houseSearchRepository;
    }

    /**
     * Save a house.
     *
     * @param houseDTO the entity to save
     * @return the persisted entity
     */
    public HouseDTO save(HouseDTO houseDTO) {
        log.debug("Request to save House : {}", houseDTO);
        House house = houseMapper.toEntity(houseDTO);
        house = houseRepository.save(house);
        HouseDTO result = houseMapper.toDto(house);
        houseSearchRepository.save(house);
        return result;
    }

    /**
     * Get all the houses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Houses");
        return houseRepository.findAll(pageable)
            .map(houseMapper::toDto);
    }


    /**
     * Get one house by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HouseDTO> findOne(Long id) {
        log.debug("Request to get House : {}", id);
        return houseRepository.findById(id)
            .map(houseMapper::toDto);
    }

    /**
     * Delete the house by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete House : {}", id);
        houseRepository.deleteById(id);
        houseSearchRepository.deleteById(id);
    }

    /**
     * Search for the house corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Houses for query {}", query);
        return houseSearchRepository.search(queryStringQuery(query), pageable)
            .map(houseMapper::toDto);
    }
}
