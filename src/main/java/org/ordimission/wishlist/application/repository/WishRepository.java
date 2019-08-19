package org.ordimission.wishlist.application.repository;

import org.ordimission.wishlist.application.domain.Wish;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Wish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

}
