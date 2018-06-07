package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.SearchTrackingService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.SearchTrackingDTO;
import com.landexp.service.dto.SearchTrackingCriteria;
import com.landexp.service.SearchTrackingQueryService;
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
 * REST controller for managing SearchTracking.
 */
@RestController
@RequestMapping("/api")
public class SearchTrackingResource {

    private final Logger log = LoggerFactory.getLogger(SearchTrackingResource.class);

    private static final String ENTITY_NAME = "searchTracking";

    private final SearchTrackingService searchTrackingService;

    private final SearchTrackingQueryService searchTrackingQueryService;

    public SearchTrackingResource(SearchTrackingService searchTrackingService, SearchTrackingQueryService searchTrackingQueryService) {
        this.searchTrackingService = searchTrackingService;
        this.searchTrackingQueryService = searchTrackingQueryService;
    }

    /**
     * POST  /search-trackings : Create a new searchTracking.
     *
     * @param searchTrackingDTO the searchTrackingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchTrackingDTO, or with status 400 (Bad Request) if the searchTracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-trackings")
    @Timed
    public ResponseEntity<SearchTrackingDTO> createSearchTracking(@RequestBody SearchTrackingDTO searchTrackingDTO) throws URISyntaxException {
        log.debug("REST request to save SearchTracking : {}", searchTrackingDTO);
        if (searchTrackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchTracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchTrackingDTO result = searchTrackingService.save(searchTrackingDTO);
        return ResponseEntity.created(new URI("/api/search-trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /search-trackings : Updates an existing searchTracking.
     *
     * @param searchTrackingDTO the searchTrackingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated searchTrackingDTO,
     * or with status 400 (Bad Request) if the searchTrackingDTO is not valid,
     * or with status 500 (Internal Server Error) if the searchTrackingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/search-trackings")
    @Timed
    public ResponseEntity<SearchTrackingDTO> updateSearchTracking(@RequestBody SearchTrackingDTO searchTrackingDTO) throws URISyntaxException {
        log.debug("REST request to update SearchTracking : {}", searchTrackingDTO);
        if (searchTrackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SearchTrackingDTO result = searchTrackingService.save(searchTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searchTrackingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /search-trackings : get all the searchTrackings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of searchTrackings in body
     */
    @GetMapping("/search-trackings")
    @Timed
    public ResponseEntity<List<SearchTrackingDTO>> getAllSearchTrackings(SearchTrackingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SearchTrackings by criteria: {}", criteria);
        Page<SearchTrackingDTO> page = searchTrackingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /search-trackings/:id : get the "id" searchTracking.
     *
     * @param id the id of the searchTrackingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searchTrackingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/search-trackings/{id}")
    @Timed
    public ResponseEntity<SearchTrackingDTO> getSearchTracking(@PathVariable Long id) {
        log.debug("REST request to get SearchTracking : {}", id);
        Optional<SearchTrackingDTO> searchTrackingDTO = searchTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchTrackingDTO);
    }

    /**
     * DELETE  /search-trackings/:id : delete the "id" searchTracking.
     *
     * @param id the id of the searchTrackingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/search-trackings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSearchTracking(@PathVariable Long id) {
        log.debug("REST request to delete SearchTracking : {}", id);
        searchTrackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/search-trackings?query=:query : search for the searchTracking corresponding
     * to the query.
     *
     * @param query the query of the searchTracking search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/search-trackings")
    @Timed
    public ResponseEntity<List<SearchTrackingDTO>> searchSearchTrackings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SearchTrackings for query {}", query);
        Page<SearchTrackingDTO> page = searchTrackingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/search-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
