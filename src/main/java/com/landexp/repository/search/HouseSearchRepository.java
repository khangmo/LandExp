package com.landexp.repository.search;

import com.landexp.domain.House;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the House entity.
 */
public interface HouseSearchRepository extends ElasticsearchRepository<House, Long> {
}
