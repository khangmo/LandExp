package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.HouseTrackingService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.HouseTrackingDTO;
import com.landexp.service.dto.HouseTrackingCriteria;
import com.landexp.service.HouseTrackingQueryService;
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
 * REST controller for managing HouseTracking.
 */
@RestController
@RequestMapping("/api")
public class HouseTrackingResource {

    private final Logger log = LoggerFactory.getLogger(HouseTrackingResource.class);

    private static final String ENTITY_NAME = "houseTracking";

    private final HouseTrackingService houseTrackingService;

    private final HouseTrackingQueryService houseTrackingQueryService;

    public HouseTrackingResource(HouseTrackingService houseTrackingService, HouseTrackingQueryService houseTrackingQueryService) {
        this.houseTrackingService = houseTrackingService;
        this.houseTrackingQueryService = houseTrackingQueryService;
    }

    /**
     * POST  /house-trackings : Create a new houseTracking.
     *
     * @param houseTrackingDTO the houseTrackingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new houseTrackingDTO, or with status 400 (Bad Request) if the houseTracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/house-trackings")
    @Timed
    public ResponseEntity<HouseTrackingDTO> createHouseTracking(@RequestBody HouseTrackingDTO houseTrackingDTO) throws URISyntaxException {
        log.debug("REST request to save HouseTracking : {}", houseTrackingDTO);
        if (houseTrackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new houseTracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseTrackingDTO result = houseTrackingService.save(houseTrackingDTO);
        return ResponseEntity.created(new URI("/api/house-trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /house-trackings : Updates an existing houseTracking.
     *
     * @param houseTrackingDTO the houseTrackingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated houseTrackingDTO,
     * or with status 400 (Bad Request) if the houseTrackingDTO is not valid,
     * or with status 500 (Internal Server Error) if the houseTrackingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/house-trackings")
    @Timed
    public ResponseEntity<HouseTrackingDTO> updateHouseTracking(@RequestBody HouseTrackingDTO houseTrackingDTO) throws URISyntaxException {
        log.debug("REST request to update HouseTracking : {}", houseTrackingDTO);
        if (houseTrackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HouseTrackingDTO result = houseTrackingService.save(houseTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, houseTrackingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /house-trackings : get all the houseTrackings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of houseTrackings in body
     */
    @GetMapping("/house-trackings")
    @Timed
    public ResponseEntity<List<HouseTrackingDTO>> getAllHouseTrackings(HouseTrackingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HouseTrackings by criteria: {}", criteria);
        Page<HouseTrackingDTO> page = houseTrackingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/house-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /house-trackings/:id : get the "id" houseTracking.
     *
     * @param id the id of the houseTrackingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the houseTrackingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/house-trackings/{id}")
    @Timed
    public ResponseEntity<HouseTrackingDTO> getHouseTracking(@PathVariable Long id) {
        log.debug("REST request to get HouseTracking : {}", id);
        Optional<HouseTrackingDTO> houseTrackingDTO = houseTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(houseTrackingDTO);
    }

    /**
     * DELETE  /house-trackings/:id : delete the "id" houseTracking.
     *
     * @param id the id of the houseTrackingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/house-trackings/{id}")
    @Timed
    public ResponseEntity<Void> deleteHouseTracking(@PathVariable Long id) {
        log.debug("REST request to delete HouseTracking : {}", id);
        houseTrackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/house-trackings?query=:query : search for the houseTracking corresponding
     * to the query.
     *
     * @param query the query of the houseTracking search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/house-trackings")
    @Timed
    public ResponseEntity<List<HouseTrackingDTO>> searchHouseTrackings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HouseTrackings for query {}", query);
        Page<HouseTrackingDTO> page = houseTrackingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/house-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
