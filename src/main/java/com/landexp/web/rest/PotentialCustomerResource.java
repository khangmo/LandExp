package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.PotentialCustomerService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.PotentialCustomerDTO;
import com.landexp.service.dto.PotentialCustomerCriteria;
import com.landexp.service.PotentialCustomerQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PotentialCustomer.
 */
@RestController
@RequestMapping("/api")
public class PotentialCustomerResource {

    private final Logger log = LoggerFactory.getLogger(PotentialCustomerResource.class);

    private static final String ENTITY_NAME = "potentialCustomer";

    private final PotentialCustomerService potentialCustomerService;

    private final PotentialCustomerQueryService potentialCustomerQueryService;

    public PotentialCustomerResource(PotentialCustomerService potentialCustomerService, PotentialCustomerQueryService potentialCustomerQueryService) {
        this.potentialCustomerService = potentialCustomerService;
        this.potentialCustomerQueryService = potentialCustomerQueryService;
    }

    /**
     * POST  /potential-customers : Create a new potentialCustomer.
     *
     * @param potentialCustomerDTO the potentialCustomerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new potentialCustomerDTO, or with status 400 (Bad Request) if the potentialCustomer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/potential-customers")
    @Timed
    public ResponseEntity<PotentialCustomerDTO> createPotentialCustomer(@RequestBody PotentialCustomerDTO potentialCustomerDTO) throws URISyntaxException {
        log.debug("REST request to save PotentialCustomer : {}", potentialCustomerDTO);
        if (potentialCustomerDTO.getId() != null) {
            throw new BadRequestAlertException("A new potentialCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PotentialCustomerDTO result = potentialCustomerService.save(potentialCustomerDTO);
        return ResponseEntity.created(new URI("/api/potential-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /potential-customers : Updates an existing potentialCustomer.
     *
     * @param potentialCustomerDTO the potentialCustomerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated potentialCustomerDTO,
     * or with status 400 (Bad Request) if the potentialCustomerDTO is not valid,
     * or with status 500 (Internal Server Error) if the potentialCustomerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/potential-customers")
    @Timed
    public ResponseEntity<PotentialCustomerDTO> updatePotentialCustomer(@RequestBody PotentialCustomerDTO potentialCustomerDTO) throws URISyntaxException {
        log.debug("REST request to update PotentialCustomer : {}", potentialCustomerDTO);
        if (potentialCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PotentialCustomerDTO result = potentialCustomerService.save(potentialCustomerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, potentialCustomerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /potential-customers : get all the potentialCustomers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of potentialCustomers in body
     */
    @GetMapping("/potential-customers")
    @Timed
    public ResponseEntity<List<PotentialCustomerDTO>> getAllPotentialCustomers(PotentialCustomerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PotentialCustomers by criteria: {}", criteria);
        Page<PotentialCustomerDTO> page = potentialCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/potential-customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /potential-customers/:id : get the "id" potentialCustomer.
     *
     * @param id the id of the potentialCustomerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the potentialCustomerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/potential-customers/{id}")
    @Timed
    public ResponseEntity<PotentialCustomerDTO> getPotentialCustomer(@PathVariable Long id) {
        log.debug("REST request to get PotentialCustomer : {}", id);
        Optional<PotentialCustomerDTO> potentialCustomerDTO = potentialCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(potentialCustomerDTO);
    }

    /**
     * DELETE  /potential-customers/:id : delete the "id" potentialCustomer.
     *
     * @param id the id of the potentialCustomerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/potential-customers/{id}")
    @Timed
    public ResponseEntity<Void> deletePotentialCustomer(@PathVariable Long id) {
        log.debug("REST request to delete PotentialCustomer : {}", id);
        potentialCustomerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/potential-customers?query=:query : search for the potentialCustomer corresponding
     * to the query.
     *
     * @param query the query of the potentialCustomer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/potential-customers")
    @Timed
    public ResponseEntity<List<PotentialCustomerDTO>> searchPotentialCustomers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PotentialCustomers for query {}", query);
        Page<PotentialCustomerDTO> page = potentialCustomerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/potential-customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
