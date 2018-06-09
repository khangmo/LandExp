package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.LandProjectService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.web.rest.util.PaginationUtil;
import com.landexp.service.dto.LandProjectDTO;
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
 * REST controller for managing LandProject.
 */
@RestController
@RequestMapping("/api")
public class LandProjectResource {

    private final Logger log = LoggerFactory.getLogger(LandProjectResource.class);

    private static final String ENTITY_NAME = "landProject";

    private final LandProjectService landProjectService;

    public LandProjectResource(LandProjectService landProjectService) {
        this.landProjectService = landProjectService;
    }

    /**
     * POST  /land-projects : Create a new landProject.
     *
     * @param landProjectDTO the landProjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new landProjectDTO, or with status 400 (Bad Request) if the landProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/land-projects")
    @Timed
    public ResponseEntity<LandProjectDTO> createLandProject(@RequestBody LandProjectDTO landProjectDTO) throws URISyntaxException {
        log.debug("REST request to save LandProject : {}", landProjectDTO);
        if (landProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new landProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandProjectDTO result = landProjectService.save(landProjectDTO);
        return ResponseEntity.created(new URI("/api/land-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /land-projects : Updates an existing landProject.
     *
     * @param landProjectDTO the landProjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated landProjectDTO,
     * or with status 400 (Bad Request) if the landProjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the landProjectDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/land-projects")
    @Timed
    public ResponseEntity<LandProjectDTO> updateLandProject(@RequestBody LandProjectDTO landProjectDTO) throws URISyntaxException {
        log.debug("REST request to update LandProject : {}", landProjectDTO);
        if (landProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LandProjectDTO result = landProjectService.save(landProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, landProjectDTO.getId().toString()))
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
    public ResponseEntity<List<LandProjectDTO>> getAllLandProjects(Pageable pageable) {
        log.debug("REST request to get a page of LandProjects");
        Page<LandProjectDTO> page = landProjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/land-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /land-projects/:id : get the "id" landProject.
     *
     * @param id the id of the landProjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the landProjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/land-projects/{id}")
    @Timed
    public ResponseEntity<LandProjectDTO> getLandProject(@PathVariable Long id) {
        log.debug("REST request to get LandProject : {}", id);
        Optional<LandProjectDTO> landProjectDTO = landProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landProjectDTO);
    }

    /**
     * DELETE  /land-projects/:id : delete the "id" landProject.
     *
     * @param id the id of the landProjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/land-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteLandProject(@PathVariable Long id) {
        log.debug("REST request to delete LandProject : {}", id);
        landProjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/land-projects?query=:query : search for the landProject corresponding
     * to the query.
     *
     * @param query the query of the landProject search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/land-projects")
    @Timed
    public ResponseEntity<List<LandProjectDTO>> searchLandProjects(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LandProjects for query {}", query);
        Page<LandProjectDTO> page = landProjectService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/land-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
