package org.ordimission.wishlist.application.repository.search;

import org.ordimission.wishlist.application.domain.Wish;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Wish} entity.
 */
public interface WishSearchRepository extends ElasticsearchRepository<Wish, Long> {
}
