package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.ServiceFee;
import com.landexp.repository.ServiceFeeRepository;
import com.landexp.repository.search.ServiceFeeSearchRepository;
import com.landexp.service.ServiceFeeService;
import com.landexp.service.dto.ServiceFeeDTO;
import com.landexp.service.mapper.ServiceFeeMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.landexp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.landexp.domain.enumeration.SaleType;
/**
 * Test class for the ServiceFeeResource REST controller.
 *
 * @see ServiceFeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class ServiceFeeResourceIntTest {

    private static final SaleType DEFAULT_SALE_TYPE = SaleType.SALE_BY_MYSELF;
    private static final SaleType UPDATED_SALE_TYPE = SaleType.SALE_SUPPORT;

    private static final Float DEFAULT_FEE = 1F;
    private static final Float UPDATED_FEE = 2F;

    @Autowired
    private ServiceFeeRepository serviceFeeRepository;


    @Autowired
    private ServiceFeeMapper serviceFeeMapper;
    

    @Autowired
    private ServiceFeeService serviceFeeService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.ServiceFeeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceFeeSearchRepository mockServiceFeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceFeeMockMvc;

    private ServiceFee serviceFee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceFeeResource serviceFeeResource = new ServiceFeeResource(serviceFeeService);
        this.restServiceFeeMockMvc = MockMvcBuilders.standaloneSetup(serviceFeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceFee createEntity(EntityManager em) {
        ServiceFee serviceFee = new ServiceFee()
            .saleType(DEFAULT_SALE_TYPE)
            .fee(DEFAULT_FEE);
        return serviceFee;
    }

    @Before
    public void initTest() {
        serviceFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceFee() throws Exception {
        int databaseSizeBeforeCreate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee
        ServiceFeeDTO serviceFeeDTO = serviceFeeMapper.toDto(serviceFee);
        restServiceFeeMockMvc.perform(post("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFeeDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceFee testServiceFee = serviceFeeList.get(serviceFeeList.size() - 1);
        assertThat(testServiceFee.getSaleType()).isEqualTo(DEFAULT_SALE_TYPE);
        assertThat(testServiceFee.getFee()).isEqualTo(DEFAULT_FEE);

        // Validate the ServiceFee in Elasticsearch
        verify(mockServiceFeeSearchRepository, times(1)).save(testServiceFee);
    }

    @Test
    @Transactional
    public void createServiceFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee with an existing ID
        serviceFee.setId(1L);
        ServiceFeeDTO serviceFeeDTO = serviceFeeMapper.toDto(serviceFee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceFeeMockMvc.perform(post("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceFee in Elasticsearch
        verify(mockServiceFeeSearchRepository, times(0)).save(serviceFee);
    }

    @Test
    @Transactional
    public void getAllServiceFees() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        // Get all the serviceFeeList
        restServiceFeeMockMvc.perform(get("/api/service-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleType").value(hasItem(DEFAULT_SALE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        // Get the serviceFee
        restServiceFeeMockMvc.perform(get("/api/service-fees/{id}", serviceFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceFee.getId().intValue()))
            .andExpect(jsonPath("$.saleType").value(DEFAULT_SALE_TYPE.toString()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingServiceFee() throws Exception {
        // Get the serviceFee
        restServiceFeeMockMvc.perform(get("/api/service-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        int databaseSizeBeforeUpdate = serviceFeeRepository.findAll().size();

        // Update the serviceFee
        ServiceFee updatedServiceFee = serviceFeeRepository.findById(serviceFee.getId()).get();
        // Disconnect from session so that the updates on updatedServiceFee are not directly saved in db
        em.detach(updatedServiceFee);
        updatedServiceFee
            .saleType(UPDATED_SALE_TYPE)
            .fee(UPDATED_FEE);
        ServiceFeeDTO serviceFeeDTO = serviceFeeMapper.toDto(updatedServiceFee);

        restServiceFeeMockMvc.perform(put("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFeeDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeUpdate);
        ServiceFee testServiceFee = serviceFeeList.get(serviceFeeList.size() - 1);
        assertThat(testServiceFee.getSaleType()).isEqualTo(UPDATED_SALE_TYPE);
        assertThat(testServiceFee.getFee()).isEqualTo(UPDATED_FEE);

        // Validate the ServiceFee in Elasticsearch
        verify(mockServiceFeeSearchRepository, times(1)).save(testServiceFee);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceFee() throws Exception {
        int databaseSizeBeforeUpdate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee
        ServiceFeeDTO serviceFeeDTO = serviceFeeMapper.toDto(serviceFee);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServiceFeeMockMvc.perform(put("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceFee in Elasticsearch
        verify(mockServiceFeeSearchRepository, times(0)).save(serviceFee);
    }

    @Test
    @Transactional
    public void deleteServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        int databaseSizeBeforeDelete = serviceFeeRepository.findAll().size();

        // Get the serviceFee
        restServiceFeeMockMvc.perform(delete("/api/service-fees/{id}", serviceFee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceFee in Elasticsearch
        verify(mockServiceFeeSearchRepository, times(1)).deleteById(serviceFee.getId());
    }

    @Test
    @Transactional
    public void searchServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);
        when(mockServiceFeeSearchRepository.search(queryStringQuery("id:" + serviceFee.getId())))
            .thenReturn(Collections.singletonList(serviceFee));
        // Search the serviceFee
        restServiceFeeMockMvc.perform(get("/api/_search/service-fees?query=id:" + serviceFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleType").value(hasItem(DEFAULT_SALE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFee.class);
        ServiceFee serviceFee1 = new ServiceFee();
        serviceFee1.setId(1L);
        ServiceFee serviceFee2 = new ServiceFee();
        serviceFee2.setId(serviceFee1.getId());
        assertThat(serviceFee1).isEqualTo(serviceFee2);
        serviceFee2.setId(2L);
        assertThat(serviceFee1).isNotEqualTo(serviceFee2);
        serviceFee1.setId(null);
        assertThat(serviceFee1).isNotEqualTo(serviceFee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFeeDTO.class);
        ServiceFeeDTO serviceFeeDTO1 = new ServiceFeeDTO();
        serviceFeeDTO1.setId(1L);
        ServiceFeeDTO serviceFeeDTO2 = new ServiceFeeDTO();
        assertThat(serviceFeeDTO1).isNotEqualTo(serviceFeeDTO2);
        serviceFeeDTO2.setId(serviceFeeDTO1.getId());
        assertThat(serviceFeeDTO1).isEqualTo(serviceFeeDTO2);
        serviceFeeDTO2.setId(2L);
        assertThat(serviceFeeDTO1).isNotEqualTo(serviceFeeDTO2);
        serviceFeeDTO1.setId(null);
        assertThat(serviceFeeDTO1).isNotEqualTo(serviceFeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceFeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serviceFeeMapper.fromId(null)).isNull();
    }
}
