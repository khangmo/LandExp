package com.landexp.repository.search;

import com.landexp.domain.LandProjectPhoto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LandProjectPhoto entity.
 */
public interface LandProjectPhotoSearchRepository extends ElasticsearchRepository<LandProjectPhoto, Long> {
}
