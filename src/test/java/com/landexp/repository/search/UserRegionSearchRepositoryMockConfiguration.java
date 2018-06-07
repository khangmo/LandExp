package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UserRegionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class UserRegionSearchRepositoryMockConfiguration {

    @MockBean
    private UserRegionSearchRepository mockUserRegionSearchRepository;

}
