package com.landexp.repository.search;

import com.landexp.domain.UserFeed;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserFeed entity.
 */
public interface UserFeedSearchRepository extends ElasticsearchRepository<UserFeed, Long> {
}
