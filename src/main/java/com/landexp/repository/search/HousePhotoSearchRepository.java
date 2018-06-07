package com.landexp.repository.search;

import com.landexp.domain.HousePhoto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HousePhoto entity.
 */
public interface HousePhotoSearchRepository extends ElasticsearchRepository<HousePhoto, Long> {
}
