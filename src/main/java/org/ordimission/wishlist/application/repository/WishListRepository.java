package org.ordimission.wishlist.application.repository;

import org.ordimission.wishlist.application.domain.WishList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WishList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

}
