package org.ordimission.wishlist.application.service.impl;

import org.ordimission.wishlist.application.service.WishListService;
import org.ordimission.wishlist.application.domain.WishList;
import org.ordimission.wishlist.application.repository.WishListRepository;
import org.ordimission.wishlist.application.repository.search.WishListSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link WishList}.
 */
@Service
@Transactional
public class WishListServiceImpl implements WishListService {

    private final Logger log = LoggerFactory.getLogger(WishListServiceImpl.class);

    private final WishListRepository wishListRepository;

    private final WishListSearchRepository wishListSearchRepository;

    public WishListServiceImpl(WishListRepository wishListRepository, WishListSearchRepository wishListSearchRepository) {
        this.wishListRepository = wishListRepository;
        this.wishListSearchRepository = wishListSearchRepository;
    }

    /**
     * Save a wishList.
     *
     * @param wishList the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WishList save(WishList wishList) {
        log.debug("Request to save WishList : {}", wishList);
        WishList result = wishListRepository.save(wishList);
        wishListSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the wishLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WishList> findAll(Pageable pageable) {
        log.debug("Request to get all WishLists");
        return wishListRepository.findAll(pageable);
    }


    /**
     * Get one wishList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WishList> findOne(Long id) {
        log.debug("Request to get WishList : {}", id);
        return wishListRepository.findById(id);
    }

    /**
     * Delete the wishList by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WishList : {}", id);
        wishListRepository.deleteById(id);
        wishListSearchRepository.deleteById(id);
    }

    /**
     * Search for the wishList corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WishList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WishLists for query {}", query);
        return wishListSearchRepository.search(queryStringQuery(query), pageable);    }
}
