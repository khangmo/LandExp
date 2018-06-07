package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.UserTrackingService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.UserTrackingDTO;
import com.landexp.service.dto.UserTrackingCriteria;
import com.landexp.service.UserTrackingQueryService;
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
 * REST controller for managing UserTracking.
 */
@RestController
@RequestMapping("/api")
public class UserTrackingResource {

    private final Logger log = LoggerFactory.getLogger(UserTrackingResource.class);

    private static final String ENTITY_NAME = "userTracking";

    private final UserTrackingService userTrackingService;

    private final UserTrackingQueryService userTrackingQueryService;

    public UserTrackingResource(UserTrackingService userTrackingService, UserTrackingQueryService userTrackingQueryService) {
        this.userTrackingService = userTrackingService;
        this.userTrackingQueryService = userTrackingQueryService;
    }

    /**
     * POST  /user-trackings : Create a new userTracking.
     *
     * @param userTrackingDTO the userTrackingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userTrackingDTO, or with status 400 (Bad Request) if the userTracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-trackings")
    @Timed
    public ResponseEntity<UserTrackingDTO> createUserTracking(@RequestBody UserTrackingDTO userTrackingDTO) throws URISyntaxException {
        log.debug("REST request to save UserTracking : {}", userTrackingDTO);
        if (userTrackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new userTracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserTrackingDTO result = userTrackingService.save(userTrackingDTO);
        return ResponseEntity.created(new URI("/api/user-trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-trackings : Updates an existing userTracking.
     *
     * @param userTrackingDTO the userTrackingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userTrackingDTO,
     * or with status 400 (Bad Request) if the userTrackingDTO is not valid,
     * or with status 500 (Internal Server Error) if the userTrackingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-trackings")
    @Timed
    public ResponseEntity<UserTrackingDTO> updateUserTracking(@RequestBody UserTrackingDTO userTrackingDTO) throws URISyntaxException {
        log.debug("REST request to update UserTracking : {}", userTrackingDTO);
        if (userTrackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserTrackingDTO result = userTrackingService.save(userTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userTrackingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-trackings : get all the userTrackings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of userTrackings in body
     */
    @GetMapping("/user-trackings")
    @Timed
    public ResponseEntity<List<UserTrackingDTO>> getAllUserTrackings(UserTrackingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserTrackings by criteria: {}", criteria);
        Page<UserTrackingDTO> page = userTrackingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-trackings/:id : get the "id" userTracking.
     *
     * @param id the id of the userTrackingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userTrackingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-trackings/{id}")
    @Timed
    public ResponseEntity<UserTrackingDTO> getUserTracking(@PathVariable Long id) {
        log.debug("REST request to get UserTracking : {}", id);
        Optional<UserTrackingDTO> userTrackingDTO = userTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userTrackingDTO);
    }

    /**
     * DELETE  /user-trackings/:id : delete the "id" userTracking.
     *
     * @param id the id of the userTrackingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-trackings/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserTracking(@PathVariable Long id) {
        log.debug("REST request to delete UserTracking : {}", id);
        userTrackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-trackings?query=:query : search for the userTracking corresponding
     * to the query.
     *
     * @param query the query of the userTracking search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-trackings")
    @Timed
    public ResponseEntity<List<UserTrackingDTO>> searchUserTrackings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserTrackings for query {}", query);
        Page<UserTrackingDTO> page = userTrackingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
