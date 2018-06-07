package com.landexp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of HouseSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HouseSearchRepositoryMockConfiguration {

    @MockBean
    private HouseSearchRepository mockHouseSearchRepository;

}
