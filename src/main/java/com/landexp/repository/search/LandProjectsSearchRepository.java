package com.landexp.repository.search;

import com.landexp.domain.LandProjects;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LandProjects entity.
 */
public interface LandProjectsSearchRepository extends ElasticsearchRepository<LandProjects, Long> {
}
