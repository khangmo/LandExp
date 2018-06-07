package com.landexp.repository.search;

import com.landexp.domain.ServiceFee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ServiceFee entity.
 */
public interface ServiceFeeSearchRepository extends ElasticsearchRepository<ServiceFee, Long> {
}
