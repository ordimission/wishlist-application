package org.ordimission.wishlist.application.service.impl;

import org.ordimission.wishlist.application.service.AnswerListService;
import org.ordimission.wishlist.application.domain.AnswerList;
import org.ordimission.wishlist.application.repository.AnswerListRepository;
import org.ordimission.wishlist.application.repository.search.AnswerListSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnswerList}.
 */
@Service
@Transactional
public class AnswerListServiceImpl implements AnswerListService {

    private final Logger log = LoggerFactory.getLogger(AnswerListServiceImpl.class);

    private final AnswerListRepository answerListRepository;

    private final AnswerListSearchRepository answerListSearchRepository;

    public AnswerListServiceImpl(AnswerListRepository answerListRepository, AnswerListSearchRepository answerListSearchRepository) {
        this.answerListRepository = answerListRepository;
        this.answerListSearchRepository = answerListSearchRepository;
    }

    /**
     * Save a answerList.
     *
     * @param answerList the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnswerList save(AnswerList answerList) {
        log.debug("Request to save AnswerList : {}", answerList);
        AnswerList result = answerListRepository.save(answerList);
        answerListSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the answerLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnswerList> findAll(Pageable pageable) {
        log.debug("Request to get all AnswerLists");
        return answerListRepository.findAll(pageable);
    }


    /**
     * Get one answerList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnswerList> findOne(Long id) {
        log.debug("Request to get AnswerList : {}", id);
        return answerListRepository.findById(id);
    }

    /**
     * Delete the answerList by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnswerList : {}", id);
        answerListRepository.deleteById(id);
        answerListSearchRepository.deleteById(id);
    }

    /**
     * Search for the answerList corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnswerList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnswerLists for query {}", query);
        return answerListSearchRepository.search(queryStringQuery(query), pageable);    }
}
