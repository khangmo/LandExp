package com.landexp.repository.search;

import com.landexp.domain.SearchTracking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SearchTracking entity.
 */
public interface SearchTrackingSearchRepository extends ElasticsearchRepository<SearchTracking, Long> {
}
