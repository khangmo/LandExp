package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UserSubscriptionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class UserSubscriptionSearchRepositoryMockConfiguration {

    @MockBean
    private UserSubscriptionSearchRepository mockUserSubscriptionSearchRepository;

}
