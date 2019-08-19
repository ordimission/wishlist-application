package org.ordimission.wishlist.application.service.impl;

import org.ordimission.wishlist.application.service.AnswerService;
import org.ordimission.wishlist.application.domain.Answer;
import org.ordimission.wishlist.application.repository.AnswerRepository;
import org.ordimission.wishlist.application.repository.search.AnswerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Answer}.
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    private final AnswerSearchRepository answerSearchRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerSearchRepository answerSearchRepository) {
        this.answerRepository = answerRepository;
        this.answerSearchRepository = answerSearchRepository;
    }

    /**
     * Save a answer.
     *
     * @param answer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Answer save(Answer answer) {
        log.debug("Request to save Answer : {}", answer);
        Answer result = answerRepository.save(answer);
        answerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the answers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Answer> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return answerRepository.findAll(pageable);
    }


    /**
     * Get one answer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Answer> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id);
    }

    /**
     * Delete the answer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
        answerSearchRepository.deleteById(id);
    }

    /**
     * Search for the answer corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Answer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Answers for query {}", query);
        return answerSearchRepository.search(queryStringQuery(query), pageable);    }
}
