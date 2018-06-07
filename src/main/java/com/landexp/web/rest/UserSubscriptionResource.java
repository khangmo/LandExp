package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.UserSubscriptionService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.UserSubscriptionDTO;
import com.landexp.service.dto.UserSubscriptionCriteria;
import com.landexp.service.UserSubscriptionQueryService;
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
 * REST controller for managing UserSubscription.
 */
@RestController
@RequestMapping("/api")
public class UserSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionResource.class);

    private static final String ENTITY_NAME = "userSubscription";

    private final UserSubscriptionService userSubscriptionService;

    private final UserSubscriptionQueryService userSubscriptionQueryService;

    public UserSubscriptionResource(UserSubscriptionService userSubscriptionService, UserSubscriptionQueryService userSubscriptionQueryService) {
        this.userSubscriptionService = userSubscriptionService;
        this.userSubscriptionQueryService = userSubscriptionQueryService;
    }

    /**
     * POST  /user-subscriptions : Create a new userSubscription.
     *
     * @param userSubscriptionDTO the userSubscriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSubscriptionDTO, or with status 400 (Bad Request) if the userSubscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-subscriptions")
    @Timed
    public ResponseEntity<UserSubscriptionDTO> createUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save UserSubscription : {}", userSubscriptionDTO);
        if (userSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSubscriptionDTO result = userSubscriptionService.save(userSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/user-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-subscriptions : Updates an existing userSubscription.
     *
     * @param userSubscriptionDTO the userSubscriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userSubscriptionDTO,
     * or with status 400 (Bad Request) if the userSubscriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the userSubscriptionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-subscriptions")
    @Timed
    public ResponseEntity<UserSubscriptionDTO> updateUserSubscription(@RequestBody UserSubscriptionDTO userSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update UserSubscription : {}", userSubscriptionDTO);
        if (userSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserSubscriptionDTO result = userSubscriptionService.save(userSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-subscriptions : get all the userSubscriptions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of userSubscriptions in body
     */
    @GetMapping("/user-subscriptions")
    @Timed
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions(UserSubscriptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserSubscriptions by criteria: {}", criteria);
        Page<UserSubscriptionDTO> page = userSubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-subscriptions/:id : get the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSubscriptionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-subscriptions/{id}")
    @Timed
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(@PathVariable Long id) {
        log.debug("REST request to get UserSubscription : {}", id);
        Optional<UserSubscriptionDTO> userSubscriptionDTO = userSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSubscriptionDTO);
    }

    /**
     * DELETE  /user-subscriptions/:id : delete the "id" userSubscription.
     *
     * @param id the id of the userSubscriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-subscriptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Long id) {
        log.debug("REST request to delete UserSubscription : {}", id);
        userSubscriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-subscriptions?query=:query : search for the userSubscription corresponding
     * to the query.
     *
     * @param query the query of the userSubscription search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-subscriptions")
    @Timed
    public ResponseEntity<List<UserSubscriptionDTO>> searchUserSubscriptions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserSubscriptions for query {}", query);
        Page<UserSubscriptionDTO> page = userSubscriptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
