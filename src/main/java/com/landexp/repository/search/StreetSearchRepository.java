package com.landexp.repository.search;

import com.landexp.domain.Street;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Street entity.
 */
public interface StreetSearchRepository extends ElasticsearchRepository<Street, Long> {
}
