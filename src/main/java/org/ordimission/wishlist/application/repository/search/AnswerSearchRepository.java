package org.ordimission.wishlist.application.repository.search;

import org.ordimission.wishlist.application.domain.Answer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Answer} entity.
 */
public interface AnswerSearchRepository extends ElasticsearchRepository<Answer, Long> {
}
