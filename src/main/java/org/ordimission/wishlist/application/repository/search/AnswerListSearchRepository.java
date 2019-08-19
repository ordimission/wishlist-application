package org.ordimission.wishlist.application.repository.search;

import org.ordimission.wishlist.application.domain.AnswerList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnswerList} entity.
 */
public interface AnswerListSearchRepository extends ElasticsearchRepository<AnswerList, Long> {
}
