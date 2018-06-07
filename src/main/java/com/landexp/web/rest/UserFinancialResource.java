package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.UserFinancialService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.UserFinancialDTO;
import com.landexp.service.dto.UserFinancialCriteria;
import com.landexp.service.UserFinancialQueryService;
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
 * REST controller for managing UserFinancial.
 */
@RestController
@RequestMapping("/api")
public class UserFinancialResource {

    private final Logger log = LoggerFactory.getLogger(UserFinancialResource.class);

    private static final String ENTITY_NAME = "userFinancial";

    private final UserFinancialService userFinancialService;

    private final UserFinancialQueryService userFinancialQueryService;

    public UserFinancialResource(UserFinancialService userFinancialService, UserFinancialQueryService userFinancialQueryService) {
        this.userFinancialService = userFinancialService;
        this.userFinancialQueryService = userFinancialQueryService;
    }

    /**
     * POST  /user-financials : Create a new userFinancial.
     *
     * @param userFinancialDTO the userFinancialDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userFinancialDTO, or with status 400 (Bad Request) if the userFinancial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-financials")
    @Timed
    public ResponseEntity<UserFinancialDTO> createUserFinancial(@RequestBody UserFinancialDTO userFinancialDTO) throws URISyntaxException {
        log.debug("REST request to save UserFinancial : {}", userFinancialDTO);
        if (userFinancialDTO.getId() != null) {
            throw new BadRequestAlertException("A new userFinancial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFinancialDTO result = userFinancialService.save(userFinancialDTO);
        return ResponseEntity.created(new URI("/api/user-financials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-financials : Updates an existing userFinancial.
     *
     * @param userFinancialDTO the userFinancialDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userFinancialDTO,
     * or with status 400 (Bad Request) if the userFinancialDTO is not valid,
     * or with status 500 (Internal Server Error) if the userFinancialDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-financials")
    @Timed
    public ResponseEntity<UserFinancialDTO> updateUserFinancial(@RequestBody UserFinancialDTO userFinancialDTO) throws URISyntaxException {
        log.debug("REST request to update UserFinancial : {}", userFinancialDTO);
        if (userFinancialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserFinancialDTO result = userFinancialService.save(userFinancialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userFinancialDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-financials : get all the userFinancials.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of userFinancials in body
     */
    @GetMapping("/user-financials")
    @Timed
    public ResponseEntity<List<UserFinancialDTO>> getAllUserFinancials(UserFinancialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserFinancials by criteria: {}", criteria);
        Page<UserFinancialDTO> page = userFinancialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-financials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-financials/:id : get the "id" userFinancial.
     *
     * @param id the id of the userFinancialDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userFinancialDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-financials/{id}")
    @Timed
    public ResponseEntity<UserFinancialDTO> getUserFinancial(@PathVariable Long id) {
        log.debug("REST request to get UserFinancial : {}", id);
        Optional<UserFinancialDTO> userFinancialDTO = userFinancialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userFinancialDTO);
    }

    /**
     * DELETE  /user-financials/:id : delete the "id" userFinancial.
     *
     * @param id the id of the userFinancialDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-financials/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserFinancial(@PathVariable Long id) {
        log.debug("REST request to delete UserFinancial : {}", id);
        userFinancialService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-financials?query=:query : search for the userFinancial corresponding
     * to the query.
     *
     * @param query the query of the userFinancial search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-financials")
    @Timed
    public ResponseEntity<List<UserFinancialDTO>> searchUserFinancials(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserFinancials for query {}", query);
        Page<UserFinancialDTO> page = userFinancialService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-financials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
