package org.ordimission.wishlist.application.service;

import org.ordimission.wishlist.application.domain.Wish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Wish}.
 */
public interface WishService {

    /**
     * Save a wish.
     *
     * @param wish the entity to save.
     * @return the persisted entity.
     */
    Wish save(Wish wish);

    /**
     * Get all the wishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Wish> findAll(Pageable pageable);


    /**
     * Get the "id" wish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Wish> findOne(Long id);

    /**
     * Delete the "id" wish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the wish corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Wish> search(String query, Pageable pageable);
}
