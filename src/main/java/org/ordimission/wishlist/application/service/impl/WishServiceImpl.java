package org.ordimission.wishlist.application.service.impl;

import org.ordimission.wishlist.application.service.WishService;
import org.ordimission.wishlist.application.domain.Wish;
import org.ordimission.wishlist.application.repository.WishRepository;
import org.ordimission.wishlist.application.repository.search.WishSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Wish}.
 */
@Service
@Transactional
public class WishServiceImpl implements WishService {

    private final Logger log = LoggerFactory.getLogger(WishServiceImpl.class);

    private final WishRepository wishRepository;

    private final WishSearchRepository wishSearchRepository;

    public WishServiceImpl(WishRepository wishRepository, WishSearchRepository wishSearchRepository) {
        this.wishRepository = wishRepository;
        this.wishSearchRepository = wishSearchRepository;
    }

    /**
     * Save a wish.
     *
     * @param wish the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Wish save(Wish wish) {
        log.debug("Request to save Wish : {}", wish);
        Wish result = wishRepository.save(wish);
        wishSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the wishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Wish> findAll(Pageable pageable) {
        log.debug("Request to get all Wishes");
        return wishRepository.findAll(pageable);
    }


    /**
     * Get one wish by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Wish> findOne(Long id) {
        log.debug("Request to get Wish : {}", id);
        return wishRepository.findById(id);
    }

    /**
     * Delete the wish by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wish : {}", id);
        wishRepository.deleteById(id);
        wishSearchRepository.deleteById(id);
    }

    /**
     * Search for the wish corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Wish> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Wishes for query {}", query);
        return wishSearchRepository.search(queryStringQuery(query), pageable);    }
}
