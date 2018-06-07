package com.landexp.repository.search;

import com.landexp.domain.Banner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Banner entity.
 */
public interface BannerSearchRepository extends ElasticsearchRepository<Banner, Long> {
}
