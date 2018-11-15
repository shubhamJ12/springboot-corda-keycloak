package com.corda.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.corda.backend.domain.Corporate;
import com.corda.backend.model.ClientCorporate;
import com.corda.backend.repository.CorporateRepository;
import com.corda.backend.web.rest.errors.BadRequestAlertException;
import com.corda.backend.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Corporate.
 */
@RestController
@RequestMapping("/api")
public class CorporateResource {

    private final Logger log = LoggerFactory.getLogger(CorporateResource.class);

    private static final String ENTITY_NAME = "corporate";

    private final CorporateRepository corporateRepository;

    public CorporateResource(CorporateRepository corporateRepository) {
        this.corporateRepository = corporateRepository;
    }


    @Autowired
    com.corda.backend.api.Corporate corporatess;

    /**
     * POST  /corporates : Create a new corporate.
     *
     * @param corporate the corporate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corporate, or with status 400 (Bad Request) if the corporate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/corporates")
    @Timed
    public ResponseEntity<Corporate> createCorporate(@RequestBody Corporate corporate) throws URISyntaxException {


            log.debug("REST request to save Corporate : {}", corporate);


            if (corporate != null && (corporate.getCorporate_name() != null || corporate.getCorporate_id() != null)) {
                ClientCorporate clientCorporate = new ClientCorporate(corporate.getCorporate_id(), corporate.getCorporate_name());
                if (corporate.getCorporate_id() != null) {
                    clientCorporate.setCorporate_id(corporate.getCorporate_id());
                }
                if (corporate.getCorporate_name() != null) {
                    clientCorporate.setCorporate_name(corporate.getCorporate_name());
                }
                corporatess.createCordaTranscation(clientCorporate);


                System.out.println("REST request to save Corporate : }}");
                if (corporate.getId() != null) {
                    throw new BadRequestAlertException("A new corporate cannot already have an ID", ENTITY_NAME, "idexists");
                }
                log.debug(" save Corporate to db : {}", corporate);
                Corporate result = corporateRepository.save(corporate);
                log.debug(" save Corporate to db succesfully!! : {}", corporate);

                return ResponseEntity.created(new URI("/api/corporates/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                    .body(result);
            } else {
                throw new BadRequestAlertException("Invalid paramter", ENTITY_NAME, "idnull");
            }


    }

    /**
     * PUT  /corporates : Updates an existing corporate.
     *
     * @param corporate the corporate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corporate,
     * or with status 400 (Bad Request) if the corporate is not valid,
     * or with status 500 (Internal Server Error) if the corporate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/corporates")
    @Timed
    public ResponseEntity<Corporate> updateCorporate(@RequestBody Corporate corporate) throws URISyntaxException {

        log.debug("REST request to update Corporate : {}", corporate);
        if (corporate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Corporate result = corporateRepository.save(corporate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, corporate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /corporates : get all the corporates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of corporates in body
     */


    @GetMapping("/corporates")
    @Timed
    public List<Corporate> getAllCorporates() {
        log.debug("REST request to get all Corporates");
/*
        System.out.println("REST request to save Corporate : {");
        Corporate corporate = new Corporate();
        corporate.setCorporate_id("99883");
        corporate.setCorporate_name("SHUBHAM");
        ClientCorporate clientCorporate = new ClientCorporate("22", "ss");
        if (corporate.getCorporate_id() != null) {
            clientCorporate.setCorporate_id(corporate.getCorporate_id());
        }
        if (corporate.getCorporate_name() != null) {
            clientCorporate.setCorporate_name(corporate.getCorporate_name());
        }
        try {
            corporatess.createCordaTranscation(clientCorporate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Corporate result = corporateRepository.save(corporate);*/
        return corporateRepository.findAll();
    }

    /**
     * GET  /corporates/:id : get the "id" corporate.
     *
     * @param id the id of the corporate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corporate, or with status 404 (Not Found)
     */
/*
    @GetMapping("/corporates/{id}")
    @Timed
    public ResponseEntity<Corporate> getCorporate(@PathVariable Long id) {
        log.debug("REST request to get Corporate : {}", id);
        Optional<Corporate> corporate = corporateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(corporate);
    }
*/

    /**
     * DELETE  /corporates/:id : delete the "id" corporate.
     *
     * @param id the id of the corporate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
  /*  @DeleteMapping("/corporates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorporate(@PathVariable Long id) {
        log.debug("REST request to delete Corporate : {}", id);

        corporateRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }*/
}
