package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.UserRegionService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.UserRegionDTO;
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
 * REST controller for managing UserRegion.
 */
@RestController
@RequestMapping("/api")
public class UserRegionResource {

    private final Logger log = LoggerFactory.getLogger(UserRegionResource.class);

    private static final String ENTITY_NAME = "userRegion";

    private final UserRegionService userRegionService;

    public UserRegionResource(UserRegionService userRegionService) {
        this.userRegionService = userRegionService;
    }

    /**
     * POST  /user-regions : Create a new userRegion.
     *
     * @param userRegionDTO the userRegionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRegionDTO, or with status 400 (Bad Request) if the userRegion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-regions")
    @Timed
    public ResponseEntity<UserRegionDTO> createUserRegion(@RequestBody UserRegionDTO userRegionDTO) throws URISyntaxException {
        log.debug("REST request to save UserRegion : {}", userRegionDTO);
        if (userRegionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRegionDTO result = userRegionService.save(userRegionDTO);
        return ResponseEntity.created(new URI("/api/user-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-regions : Updates an existing userRegion.
     *
     * @param userRegionDTO the userRegionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRegionDTO,
     * or with status 400 (Bad Request) if the userRegionDTO is not valid,
     * or with status 500 (Internal Server Error) if the userRegionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-regions")
    @Timed
    public ResponseEntity<UserRegionDTO> updateUserRegion(@RequestBody UserRegionDTO userRegionDTO) throws URISyntaxException {
        log.debug("REST request to update UserRegion : {}", userRegionDTO);
        if (userRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserRegionDTO result = userRegionService.save(userRegionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRegionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-regions : get all the userRegions.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of userRegions in body
     */
    @GetMapping("/user-regions")
    @Timed
    public ResponseEntity<List<UserRegionDTO>> getAllUserRegions(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of UserRegions");
        Page<UserRegionDTO> page;
        if (eagerload) {
            page = userRegionService.findAllWithEagerRelationships(pageable);
        } else {
            page = userRegionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/user-regions?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-regions/:id : get the "id" userRegion.
     *
     * @param id the id of the userRegionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRegionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-regions/{id}")
    @Timed
    public ResponseEntity<UserRegionDTO> getUserRegion(@PathVariable Long id) {
        log.debug("REST request to get UserRegion : {}", id);
        Optional<UserRegionDTO> userRegionDTO = userRegionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRegionDTO);
    }

    /**
     * DELETE  /user-regions/:id : delete the "id" userRegion.
     *
     * @param id the id of the userRegionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-regions/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserRegion(@PathVariable Long id) {
        log.debug("REST request to delete UserRegion : {}", id);
        userRegionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-regions?query=:query : search for the userRegion corresponding
     * to the query.
     *
     * @param query the query of the userRegion search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-regions")
    @Timed
    public ResponseEntity<List<UserRegionDTO>> searchUserRegions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserRegions for query {}", query);
        Page<UserRegionDTO> page = userRegionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
