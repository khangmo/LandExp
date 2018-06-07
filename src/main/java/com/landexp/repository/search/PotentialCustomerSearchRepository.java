package com.landexp.repository.search;

import com.landexp.domain.PotentialCustomer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PotentialCustomer entity.
 */
public interface PotentialCustomerSearchRepository extends ElasticsearchRepository<PotentialCustomer, Long> {
}
