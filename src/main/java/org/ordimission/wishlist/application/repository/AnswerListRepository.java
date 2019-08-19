package org.ordimission.wishlist.application.repository;

import org.ordimission.wishlist.application.domain.AnswerList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AnswerList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerListRepository extends JpaRepository<AnswerList, Long> {

}
