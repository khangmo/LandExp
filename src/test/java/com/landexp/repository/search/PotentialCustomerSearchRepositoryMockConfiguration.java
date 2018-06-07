package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PotentialCustomerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PotentialCustomerSearchRepositoryMockConfiguration {

    @MockBean
    private PotentialCustomerSearchRepository mockPotentialCustomerSearchRepository;

}
