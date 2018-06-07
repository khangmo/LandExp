package com.landexp.repository.search;

import com.landexp.domain.UserTracking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserTracking entity.
 */
public interface UserTrackingSearchRepository extends ElasticsearchRepository<UserTracking, Long> {
}
