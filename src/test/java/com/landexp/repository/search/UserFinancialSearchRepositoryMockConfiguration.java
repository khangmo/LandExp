package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UserFinancialSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class UserFinancialSearchRepositoryMockConfiguration {

    @MockBean
    private UserFinancialSearchRepository mockUserFinancialSearchRepository;

}
