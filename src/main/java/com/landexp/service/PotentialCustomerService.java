package com.landexp.service;

import com.landexp.domain.PotentialCustomer;
import com.landexp.repository.PotentialCustomerRepository;
import com.landexp.repository.search.PotentialCustomerSearchRepository;
import com.landexp.service.dto.PotentialCustomerDTO;
import com.landexp.service.mapper.PotentialCustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PotentialCustomer.
 */
@Service
@Transactional
public class PotentialCustomerService {

    private final Logger log = LoggerFactory.getLogger(PotentialCustomerService.class);

    private final PotentialCustomerRepository potentialCustomerRepository;

    private final PotentialCustomerMapper potentialCustomerMapper;

    private final PotentialCustomerSearchRepository potentialCustomerSearchRepository;

    public PotentialCustomerService(PotentialCustomerRepository potentialCustomerRepository, PotentialCustomerMapper potentialCustomerMapper, PotentialCustomerSearchRepository potentialCustomerSearchRepository) {
        this.potentialCustomerRepository = potentialCustomerRepository;
        this.potentialCustomerMapper = potentialCustomerMapper;
        this.potentialCustomerSearchRepository = potentialCustomerSearchRepository;
    }

    /**
     * Save a potentialCustomer.
     *
     * @param potentialCustomerDTO the entity to save
     * @return the persisted entity
     */
    public PotentialCustomerDTO save(PotentialCustomerDTO potentialCustomerDTO) {
        log.debug("Request to save PotentialCustomer : {}", potentialCustomerDTO);
        PotentialCustomer potentialCustomer = potentialCustomerMapper.toEntity(potentialCustomerDTO);
        potentialCustomer = potentialCustomerRepository.save(potentialCustomer);
        PotentialCustomerDTO result = potentialCustomerMapper.toDto(potentialCustomer);
        potentialCustomerSearchRepository.save(potentialCustomer);
        return result;
    }

    /**
     * Get all the potentialCustomers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PotentialCustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PotentialCustomers");
        return potentialCustomerRepository.findAll(pageable)
            .map(potentialCustomerMapper::toDto);
    }


    /**
     * Get one potentialCustomer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PotentialCustomerDTO> findOne(Long id) {
        log.debug("Request to get PotentialCustomer : {}", id);
        return potentialCustomerRepository.findById(id)
            .map(potentialCustomerMapper::toDto);
    }

    /**
     * Delete the potentialCustomer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PotentialCustomer : {}", id);
        potentialCustomerRepository.deleteById(id);
        potentialCustomerSearchRepository.deleteById(id);
    }

    /**
     * Search for the potentialCustomer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PotentialCustomerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PotentialCustomers for query {}", query);
        return potentialCustomerSearchRepository.search(queryStringQuery(query), pageable)
            .map(potentialCustomerMapper::toDto);
    }
}
