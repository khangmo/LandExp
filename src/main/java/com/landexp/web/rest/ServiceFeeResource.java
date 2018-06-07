package com.landexp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.landexp.service.ServiceFeeService;
import com.landexp.web.rest.errors.BadRequestAlertException;
import com.landexp.web.rest.util.HeaderUtil;
import com.landexp.service.dto.ServiceFeeDTO;
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
 * REST controller for managing ServiceFee.
 */
@RestController
@RequestMapping("/api")
public class ServiceFeeResource {

    private final Logger log = LoggerFactory.getLogger(ServiceFeeResource.class);

    private static final String ENTITY_NAME = "serviceFee";

    private final ServiceFeeService serviceFeeService;

    public ServiceFeeResource(ServiceFeeService serviceFeeService) {
        this.serviceFeeService = serviceFeeService;
    }

    /**
     * POST  /service-fees : Create a new serviceFee.
     *
     * @param serviceFeeDTO the serviceFeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceFeeDTO, or with status 400 (Bad Request) if the serviceFee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-fees")
    @Timed
    public ResponseEntity<ServiceFeeDTO> createServiceFee(@RequestBody ServiceFeeDTO serviceFeeDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceFee : {}", serviceFeeDTO);
        if (serviceFeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceFeeDTO result = serviceFeeService.save(serviceFeeDTO);
        return ResponseEntity.created(new URI("/api/service-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-fees : Updates an existing serviceFee.
     *
     * @param serviceFeeDTO the serviceFeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceFeeDTO,
     * or with status 400 (Bad Request) if the serviceFeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceFeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-fees")
    @Timed
    public ResponseEntity<ServiceFeeDTO> updateServiceFee(@RequestBody ServiceFeeDTO serviceFeeDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceFee : {}", serviceFeeDTO);
        if (serviceFeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceFeeDTO result = serviceFeeService.save(serviceFeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceFeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-fees : get all the serviceFees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceFees in body
     */
    @GetMapping("/service-fees")
    @Timed
    public List<ServiceFeeDTO> getAllServiceFees() {
        log.debug("REST request to get all ServiceFees");
        return serviceFeeService.findAll();
    }

    /**
     * GET  /service-fees/:id : get the "id" serviceFee.
     *
     * @param id the id of the serviceFeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceFeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-fees/{id}")
    @Timed
    public ResponseEntity<ServiceFeeDTO> getServiceFee(@PathVariable Long id) {
        log.debug("REST request to get ServiceFee : {}", id);
        Optional<ServiceFeeDTO> serviceFeeDTO = serviceFeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceFeeDTO);
    }

    /**
     * DELETE  /service-fees/:id : delete the "id" serviceFee.
     *
     * @param id the id of the serviceFeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-fees/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceFee(@PathVariable Long id) {
        log.debug("REST request to delete ServiceFee : {}", id);
        serviceFeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-fees?query=:query : search for the serviceFee corresponding
     * to the query.
     *
     * @param query the query of the serviceFee search
     * @return the result of the search
     */
    @GetMapping("/_search/service-fees")
    @Timed
    public List<ServiceFeeDTO> searchServiceFees(@RequestParam String query) {
        log.debug("REST request to search ServiceFees for query {}", query);
        return serviceFeeService.search(query);
    }

}
