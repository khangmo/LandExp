package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.LandProjectPhotoService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.service.dto.LandProjectPhotoDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LandProjectPhoto.
 */
@RestController
@RequestMapping("/api")
public class LandProjectPhotoResource {

    private final Logger log = LoggerFactory.getLogger(LandProjectPhotoResource.class);

    private static final String ENTITY_NAME = "landProjectPhoto";

    private final LandProjectPhotoService landProjectPhotoService;

    public LandProjectPhotoResource(LandProjectPhotoService landProjectPhotoService) {
        this.landProjectPhotoService = landProjectPhotoService;
    }

    /**
     * POST  /land-project-photos : Create a new landProjectPhoto.
     *
     * @param landProjectPhotoDTO the landProjectPhotoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new landProjectPhotoDTO, or with status 400 (Bad Request) if the landProjectPhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/land-project-photos")
    @Timed
    public ResponseEntity<LandProjectPhotoDTO> createLandProjectPhoto(@RequestBody LandProjectPhotoDTO landProjectPhotoDTO) throws URISyntaxException {
        log.debug("REST request to save LandProjectPhoto : {}", landProjectPhotoDTO);
        if (landProjectPhotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new landProjectPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandProjectPhotoDTO result = landProjectPhotoService.save(landProjectPhotoDTO);
        return ResponseEntity.created(new URI("/api/land-project-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /land-project-photos : Updates an existing landProjectPhoto.
     *
     * @param landProjectPhotoDTO the landProjectPhotoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated landProjectPhotoDTO,
     * or with status 400 (Bad Request) if the landProjectPhotoDTO is not valid,
     * or with status 500 (Internal Server Error) if the landProjectPhotoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/land-project-photos")
    @Timed
    public ResponseEntity<LandProjectPhotoDTO> updateLandProjectPhoto(@RequestBody LandProjectPhotoDTO landProjectPhotoDTO) throws URISyntaxException {
        log.debug("REST request to update LandProjectPhoto : {}", landProjectPhotoDTO);
        if (landProjectPhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LandProjectPhotoDTO result = landProjectPhotoService.save(landProjectPhotoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, landProjectPhotoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /land-project-photos : get all the landProjectPhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of landProjectPhotos in body
     */
    @GetMapping("/land-project-photos")
    @Timed
    public List<LandProjectPhotoDTO> getAllLandProjectPhotos() {
        log.debug("REST request to get all LandProjectPhotos");
        return landProjectPhotoService.findAll();
    }

    /**
     * GET  /land-project-photos/:id : get the "id" landProjectPhoto.
     *
     * @param id the id of the landProjectPhotoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the landProjectPhotoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/land-project-photos/{id}")
    @Timed
    public ResponseEntity<LandProjectPhotoDTO> getLandProjectPhoto(@PathVariable Long id) {
        log.debug("REST request to get LandProjectPhoto : {}", id);
        Optional<LandProjectPhotoDTO> landProjectPhotoDTO = landProjectPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landProjectPhotoDTO);
    }

    /**
     * DELETE  /land-project-photos/:id : delete the "id" landProjectPhoto.
     *
     * @param id the id of the landProjectPhotoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/land-project-photos/{id}")
    @Timed
    public ResponseEntity<Void> deleteLandProjectPhoto(@PathVariable Long id) {
        log.debug("REST request to delete LandProjectPhoto : {}", id);
        landProjectPhotoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/land-project-photos?query=:query : search for the landProjectPhoto corresponding
     * to the query.
     *
     * @param query the query of the landProjectPhoto search
     * @return the result of the search
     */
    @GetMapping("/_search/land-project-photos")
    @Timed
    public List<LandProjectPhotoDTO> searchLandProjectPhotos(@RequestParam String query) {
        log.debug("REST request to search LandProjectPhotos for query {}", query);
        return landProjectPhotoService.search(query);
    }

}
