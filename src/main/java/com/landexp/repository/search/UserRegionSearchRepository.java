package com.landexp.repository.search;

import com.landexp.domain.UserRegion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRegion entity.
 */
public interface UserRegionSearchRepository extends ElasticsearchRepository<UserRegion, Long> {
}
