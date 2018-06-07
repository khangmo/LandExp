package com.landexp.repository.search;

import com.landexp.domain.UserFinancial;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserFinancial entity.
 */
public interface UserFinancialSearchRepository extends ElasticsearchRepository<UserFinancial, Long> {
}
