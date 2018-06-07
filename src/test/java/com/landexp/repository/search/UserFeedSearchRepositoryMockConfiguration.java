package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UserFeedSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class UserFeedSearchRepositoryMockConfiguration {

    @MockBean
    private UserFeedSearchRepository mockUserFeedSearchRepository;

}
