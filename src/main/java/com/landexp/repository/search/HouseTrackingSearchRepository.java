package com.landexp.repository.search;

import com.landexp.domain.HouseTracking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HouseTracking entity.
 */
public interface HouseTrackingSearchRepository extends ElasticsearchRepository<HouseTracking, Long> {
}
