package com.landexp.repository.search;

import com.landexp.domain.LandProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LandProject entity.
 */
public interface LandProjectSearchRepository extends ElasticsearchRepository<LandProject, Long> {
}
