package org.ordimission.wishlist.application.web.rest;

import org.ordimission.wishlist.application.domain.AnswerList;
import org.ordimission.wishlist.application.service.AnswerListService;
import org.ordimission.wishlist.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.ordimission.wishlist.application.domain.AnswerList}.
 */
@RestController
@RequestMapping("/api")
public class AnswerListResource {

    private final Logger log = LoggerFactory.getLogger(AnswerListResource.class);

    private static final String ENTITY_NAME = "answerList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerListService answerListService;

    public AnswerListResource(AnswerListService answerListService) {
        this.answerListService = answerListService;
    }

    /**
     * {@code POST  /answer-lists} : Create a new answerList.
     *
     * @param answerList the answerList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerList, or with status {@code 400 (Bad Request)} if the answerList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-lists")
    public ResponseEntity<AnswerList> createAnswerList(@RequestBody AnswerList answerList) throws URISyntaxException {
        log.debug("REST request to save AnswerList : {}", answerList);
        if (answerList.getId() != null) {
            throw new BadRequestAlertException("A new answerList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerList result = answerListService.save(answerList);
        return ResponseEntity.created(new URI("/api/answer-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /answer-lists} : Updates an existing answerList.
     *
     * @param answerList the answerList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerList,
     * or with status {@code 400 (Bad Request)} if the answerList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answer-lists")
    public ResponseEntity<AnswerList> updateAnswerList(@RequestBody AnswerList answerList) throws URISyntaxException {
        log.debug("REST request to update AnswerList : {}", answerList);
        if (answerList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnswerList result = answerListService.save(answerList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerList.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /answer-lists} : get all the answerLists.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answerLists in body.
     */
    @GetMapping("/answer-lists")
    public ResponseEntity<List<AnswerList>> getAllAnswerLists(Pageable pageable) {
        log.debug("REST request to get a page of AnswerLists");
        Page<AnswerList> page = answerListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /answer-lists/:id} : get the "id" answerList.
     *
     * @param id the id of the answerList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/answer-lists/{id}")
    public ResponseEntity<AnswerList> getAnswerList(@PathVariable Long id) {
        log.debug("REST request to get AnswerList : {}", id);
        Optional<AnswerList> answerList = answerListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerList);
    }

    /**
     * {@code DELETE  /answer-lists/:id} : delete the "id" answerList.
     *
     * @param id the id of the answerList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/answer-lists/{id}")
    public ResponseEntity<Void> deleteAnswerList(@PathVariable Long id) {
        log.debug("REST request to delete AnswerList : {}", id);
        answerListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/answer-lists?query=:query} : search for the answerList corresponding
     * to the query.
     *
     * @param query the query of the answerList search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/answer-lists")
    public ResponseEntity<List<AnswerList>> searchAnswerLists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnswerLists for query {}", query);
        Page<AnswerList> page = answerListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
