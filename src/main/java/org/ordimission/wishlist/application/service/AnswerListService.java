package org.ordimission.wishlist.application.service;

import org.ordimission.wishlist.application.domain.AnswerList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnswerList}.
 */
public interface AnswerListService {

    /**
     * Save a answerList.
     *
     * @param answerList the entity to save.
     * @return the persisted entity.
     */
    AnswerList save(AnswerList answerList);

    /**
     * Get all the answerLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerList> findAll(Pageable pageable);


    /**
     * Get the "id" answerList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnswerList> findOne(Long id);

    /**
     * Delete the "id" answerList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the answerList corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnswerList> search(String query, Pageable pageable);
}
