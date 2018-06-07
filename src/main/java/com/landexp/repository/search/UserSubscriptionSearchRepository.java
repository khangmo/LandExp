package com.landexp.repository.search;

import com.landexp.domain.UserSubscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserSubscription entity.
 */
public interface UserSubscriptionSearchRepository extends ElasticsearchRepository<UserSubscription, Long> {
}
