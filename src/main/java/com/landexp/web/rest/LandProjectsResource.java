package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.LandProjectsService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.LandProjectsDTO;
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
 * REST controller for managing LandProjects.
 */
@RestController
@RequestMapping("/api")
public class LandProjectsResource {

    private final Logger log = LoggerFactory.getLogger(LandProjectsResource.class);

    private static final String ENTITY_NAME = "landProjects";

    private final LandProjectsService landProjectsService;

    public LandProjectsResource(LandProjectsService landProjectsService) {
        this.landProjectsService = landProjectsService;
    }

    /**
     * POST  /land-projects : Create a new landProjects.
     *
     * @param landProjectsDTO the landProjectsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new landProjectsDTO, or with status 400 (Bad Request) if the landProjects has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/land-projects")
    @Timed
    public ResponseEntity<LandProjectsDTO> createLandProjects(@RequestBody LandProjectsDTO landProjectsDTO) throws URISyntaxException {
        log.debug("REST request to save LandProjects : {}", landProjectsDTO);
        if (landProjectsDTO.getId() != null) {
            throw new BadRequestAlertException("A new landProjects cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandProjectsDTO result = landProjectsService.save(landProjectsDTO);
        return ResponseEntity.created(new URI("/api/land-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /land-projects : Updates an existing landProjects.
     *
     * @param landProjectsDTO the landProjectsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated landProjectsDTO,
     * or with status 400 (Bad Request) if the landProjectsDTO is not valid,
     * or with status 500 (Internal Server Error) if the landProjectsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/land-projects")
    @Timed
    public ResponseEntity<LandProjectsDTO> updateLandProjects(@RequestBody LandProjectsDTO landProjectsDTO) throws URISyntaxException {
        log.debug("REST request to update LandProjects : {}", landProjectsDTO);
        if (landProjectsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LandProjectsDTO result = landProjectsService.save(landProjectsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, landProjectsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /land-projects : get all the landProjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of landProjects in body
     */
    @GetMapping("/land-projects")
    @Timed
    public ResponseEntity<List<LandProjectsDTO>> getAllLandProjects(Pageable pageable) {
        log.debug("REST request to get a page of LandProjects");
        Page<LandProjectsDTO> page = landProjectsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/land-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /land-projects/:id : get the "id" landProjects.
     *
     * @param id the id of the landProjectsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the landProjectsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/land-projects/{id}")
    @Timed
    public ResponseEntity<LandProjectsDTO> getLandProjects(@PathVariable Long id) {
        log.debug("REST request to get LandProjects : {}", id);
        Optional<LandProjectsDTO> landProjectsDTO = landProjectsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landProjectsDTO);
    }

    /**
     * DELETE  /land-projects/:id : delete the "id" landProjects.
     *
     * @param id the id of the landProjectsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/land-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteLandProjects(@PathVariable Long id) {
        log.debug("REST request to delete LandProjects : {}", id);
        landProjectsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/land-projects?query=:query : search for the landProjects corresponding
     * to the query.
     *
     * @param query the query of the landProjects search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/land-projects")
    @Timed
    public ResponseEntity<List<LandProjectsDTO>> searchLandProjects(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LandProjects for query {}", query);
        Page<LandProjectsDTO> page = landProjectsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/land-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
