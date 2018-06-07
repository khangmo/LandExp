package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.PotentialCustomer;
import com.landexp.domain.User;
import com.landexp.domain.User;
import com.landexp.domain.User;
import com.landexp.repository.PotentialCustomerRepository;
import com.landexp.repository.search.PotentialCustomerSearchRepository;
import com.landexp.service.PotentialCustomerService;
import com.landexp.service.dto.PotentialCustomerDTO;
import com.landexp.service.mapper.PotentialCustomerMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.PotentialCustomerCriteria;
import com.landexp.service.PotentialCustomerQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.landexp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.landexp.domain.enumeration.CustomerLevel;
/**
 * Test class for the PotentialCustomerResource REST controller.
 *
 * @see PotentialCustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class PotentialCustomerResourceIntTest {

    private static final CustomerLevel DEFAULT_LEVEL = CustomerLevel.NORMAL;
    private static final CustomerLevel UPDATED_LEVEL = CustomerLevel.SILVER;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PotentialCustomerRepository potentialCustomerRepository;


    @Autowired
    private PotentialCustomerMapper potentialCustomerMapper;
    

    @Autowired
    private PotentialCustomerService potentialCustomerService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.PotentialCustomerSearchRepositoryMockConfiguration
     */
    @Autowired
    private PotentialCustomerSearchRepository mockPotentialCustomerSearchRepository;

    @Autowired
    private PotentialCustomerQueryService potentialCustomerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPotentialCustomerMockMvc;

    private PotentialCustomer potentialCustomer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PotentialCustomerResource potentialCustomerResource = new PotentialCustomerResource(potentialCustomerService, potentialCustomerQueryService);
        this.restPotentialCustomerMockMvc = MockMvcBuilders.standaloneSetup(potentialCustomerResource)
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
    public static PotentialCustomer createEntity(EntityManager em) {
        PotentialCustomer potentialCustomer = new PotentialCustomer()
            .level(DEFAULT_LEVEL)
            .description(DEFAULT_DESCRIPTION)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return potentialCustomer;
    }

    @Before
    public void initTest() {
        potentialCustomer = createEntity(em);
    }

    @Test
    @Transactional
    public void createPotentialCustomer() throws Exception {
        int databaseSizeBeforeCreate = potentialCustomerRepository.findAll().size();

        // Create the PotentialCustomer
        PotentialCustomerDTO potentialCustomerDTO = potentialCustomerMapper.toDto(potentialCustomer);
        restPotentialCustomerMockMvc.perform(post("/api/potential-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(potentialCustomerDTO)))
            .andExpect(status().isCreated());

        // Validate the PotentialCustomer in the database
        List<PotentialCustomer> potentialCustomerList = potentialCustomerRepository.findAll();
        assertThat(potentialCustomerList).hasSize(databaseSizeBeforeCreate + 1);
        PotentialCustomer testPotentialCustomer = potentialCustomerList.get(potentialCustomerList.size() - 1);
        assertThat(testPotentialCustomer.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testPotentialCustomer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPotentialCustomer.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPotentialCustomer.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the PotentialCustomer in Elasticsearch
        verify(mockPotentialCustomerSearchRepository, times(1)).save(testPotentialCustomer);
    }

    @Test
    @Transactional
    public void createPotentialCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = potentialCustomerRepository.findAll().size();

        // Create the PotentialCustomer with an existing ID
        potentialCustomer.setId(1L);
        PotentialCustomerDTO potentialCustomerDTO = potentialCustomerMapper.toDto(potentialCustomer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotentialCustomerMockMvc.perform(post("/api/potential-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(potentialCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PotentialCustomer in the database
        List<PotentialCustomer> potentialCustomerList = potentialCustomerRepository.findAll();
        assertThat(potentialCustomerList).hasSize(databaseSizeBeforeCreate);

        // Validate the PotentialCustomer in Elasticsearch
        verify(mockPotentialCustomerSearchRepository, times(0)).save(potentialCustomer);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomers() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList
        restPotentialCustomerMockMvc.perform(get("/api/potential-customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potentialCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getPotentialCustomer() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get the potentialCustomer
        restPotentialCustomerMockMvc.perform(get("/api/potential-customers/{id}", potentialCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(potentialCustomer.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where level equals to DEFAULT_LEVEL
        defaultPotentialCustomerShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the potentialCustomerList where level equals to UPDATED_LEVEL
        defaultPotentialCustomerShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultPotentialCustomerShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the potentialCustomerList where level equals to UPDATED_LEVEL
        defaultPotentialCustomerShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where level is not null
        defaultPotentialCustomerShouldBeFound("level.specified=true");

        // Get all the potentialCustomerList where level is null
        defaultPotentialCustomerShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where description equals to DEFAULT_DESCRIPTION
        defaultPotentialCustomerShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the potentialCustomerList where description equals to UPDATED_DESCRIPTION
        defaultPotentialCustomerShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPotentialCustomerShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the potentialCustomerList where description equals to UPDATED_DESCRIPTION
        defaultPotentialCustomerShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where description is not null
        defaultPotentialCustomerShouldBeFound("description.specified=true");

        // Get all the potentialCustomerList where description is null
        defaultPotentialCustomerShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where createAt equals to DEFAULT_CREATE_AT
        defaultPotentialCustomerShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the potentialCustomerList where createAt equals to UPDATED_CREATE_AT
        defaultPotentialCustomerShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultPotentialCustomerShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the potentialCustomerList where createAt equals to UPDATED_CREATE_AT
        defaultPotentialCustomerShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where createAt is not null
        defaultPotentialCustomerShouldBeFound("createAt.specified=true");

        // Get all the potentialCustomerList where createAt is null
        defaultPotentialCustomerShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultPotentialCustomerShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the potentialCustomerList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultPotentialCustomerShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultPotentialCustomerShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the potentialCustomerList where createAt less than or equals to UPDATED_CREATE_AT
        defaultPotentialCustomerShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where updateAt equals to DEFAULT_UPDATE_AT
        defaultPotentialCustomerShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the potentialCustomerList where updateAt equals to UPDATED_UPDATE_AT
        defaultPotentialCustomerShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultPotentialCustomerShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the potentialCustomerList where updateAt equals to UPDATED_UPDATE_AT
        defaultPotentialCustomerShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where updateAt is not null
        defaultPotentialCustomerShouldBeFound("updateAt.specified=true");

        // Get all the potentialCustomerList where updateAt is null
        defaultPotentialCustomerShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where updateAt greater than or equals to DEFAULT_UPDATE_AT
        defaultPotentialCustomerShouldBeFound("updateAt.greaterOrEqualThan=" + DEFAULT_UPDATE_AT);

        // Get all the potentialCustomerList where updateAt greater than or equals to UPDATED_UPDATE_AT
        defaultPotentialCustomerShouldNotBeFound("updateAt.greaterOrEqualThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        // Get all the potentialCustomerList where updateAt less than or equals to DEFAULT_UPDATE_AT
        defaultPotentialCustomerShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the potentialCustomerList where updateAt less than or equals to UPDATED_UPDATE_AT
        defaultPotentialCustomerShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }


    @Test
    @Transactional
    public void getAllPotentialCustomersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        User customer = UserResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        potentialCustomer.setCustomer(customer);
        potentialCustomerRepository.saveAndFlush(potentialCustomer);
        Long customerId = customer.getId();

        // Get all the potentialCustomerList where customer equals to customerId
        defaultPotentialCustomerShouldBeFound("customerId.equals=" + customerId);

        // Get all the potentialCustomerList where customer equals to customerId + 1
        defaultPotentialCustomerShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllPotentialCustomersByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User createBy = UserResourceIntTest.createEntity(em);
        em.persist(createBy);
        em.flush();
        potentialCustomer.setCreateBy(createBy);
        potentialCustomerRepository.saveAndFlush(potentialCustomer);
        Long createById = createBy.getId();

        // Get all the potentialCustomerList where createBy equals to createById
        defaultPotentialCustomerShouldBeFound("createById.equals=" + createById);

        // Get all the potentialCustomerList where createBy equals to createById + 1
        defaultPotentialCustomerShouldNotBeFound("createById.equals=" + (createById + 1));
    }


    @Test
    @Transactional
    public void getAllPotentialCustomersByUpdateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User updateBy = UserResourceIntTest.createEntity(em);
        em.persist(updateBy);
        em.flush();
        potentialCustomer.setUpdateBy(updateBy);
        potentialCustomerRepository.saveAndFlush(potentialCustomer);
        Long updateById = updateBy.getId();

        // Get all the potentialCustomerList where updateBy equals to updateById
        defaultPotentialCustomerShouldBeFound("updateById.equals=" + updateById);

        // Get all the potentialCustomerList where updateBy equals to updateById + 1
        defaultPotentialCustomerShouldNotBeFound("updateById.equals=" + (updateById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPotentialCustomerShouldBeFound(String filter) throws Exception {
        restPotentialCustomerMockMvc.perform(get("/api/potential-customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potentialCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPotentialCustomerShouldNotBeFound(String filter) throws Exception {
        restPotentialCustomerMockMvc.perform(get("/api/potential-customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingPotentialCustomer() throws Exception {
        // Get the potentialCustomer
        restPotentialCustomerMockMvc.perform(get("/api/potential-customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePotentialCustomer() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        int databaseSizeBeforeUpdate = potentialCustomerRepository.findAll().size();

        // Update the potentialCustomer
        PotentialCustomer updatedPotentialCustomer = potentialCustomerRepository.findById(potentialCustomer.getId()).get();
        // Disconnect from session so that the updates on updatedPotentialCustomer are not directly saved in db
        em.detach(updatedPotentialCustomer);
        updatedPotentialCustomer
            .level(UPDATED_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        PotentialCustomerDTO potentialCustomerDTO = potentialCustomerMapper.toDto(updatedPotentialCustomer);

        restPotentialCustomerMockMvc.perform(put("/api/potential-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(potentialCustomerDTO)))
            .andExpect(status().isOk());

        // Validate the PotentialCustomer in the database
        List<PotentialCustomer> potentialCustomerList = potentialCustomerRepository.findAll();
        assertThat(potentialCustomerList).hasSize(databaseSizeBeforeUpdate);
        PotentialCustomer testPotentialCustomer = potentialCustomerList.get(potentialCustomerList.size() - 1);
        assertThat(testPotentialCustomer.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testPotentialCustomer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPotentialCustomer.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPotentialCustomer.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the PotentialCustomer in Elasticsearch
        verify(mockPotentialCustomerSearchRepository, times(1)).save(testPotentialCustomer);
    }

    @Test
    @Transactional
    public void updateNonExistingPotentialCustomer() throws Exception {
        int databaseSizeBeforeUpdate = potentialCustomerRepository.findAll().size();

        // Create the PotentialCustomer
        PotentialCustomerDTO potentialCustomerDTO = potentialCustomerMapper.toDto(potentialCustomer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPotentialCustomerMockMvc.perform(put("/api/potential-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(potentialCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PotentialCustomer in the database
        List<PotentialCustomer> potentialCustomerList = potentialCustomerRepository.findAll();
        assertThat(potentialCustomerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PotentialCustomer in Elasticsearch
        verify(mockPotentialCustomerSearchRepository, times(0)).save(potentialCustomer);
    }

    @Test
    @Transactional
    public void deletePotentialCustomer() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);

        int databaseSizeBeforeDelete = potentialCustomerRepository.findAll().size();

        // Get the potentialCustomer
        restPotentialCustomerMockMvc.perform(delete("/api/potential-customers/{id}", potentialCustomer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PotentialCustomer> potentialCustomerList = potentialCustomerRepository.findAll();
        assertThat(potentialCustomerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PotentialCustomer in Elasticsearch
        verify(mockPotentialCustomerSearchRepository, times(1)).deleteById(potentialCustomer.getId());
    }

    @Test
    @Transactional
    public void searchPotentialCustomer() throws Exception {
        // Initialize the database
        potentialCustomerRepository.saveAndFlush(potentialCustomer);
        when(mockPotentialCustomerSearchRepository.search(queryStringQuery("id:" + potentialCustomer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(potentialCustomer), PageRequest.of(0, 1), 1));
        // Search the potentialCustomer
        restPotentialCustomerMockMvc.perform(get("/api/_search/potential-customers?query=id:" + potentialCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(potentialCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotentialCustomer.class);
        PotentialCustomer potentialCustomer1 = new PotentialCustomer();
        potentialCustomer1.setId(1L);
        PotentialCustomer potentialCustomer2 = new PotentialCustomer();
        potentialCustomer2.setId(potentialCustomer1.getId());
        assertThat(potentialCustomer1).isEqualTo(potentialCustomer2);
        potentialCustomer2.setId(2L);
        assertThat(potentialCustomer1).isNotEqualTo(potentialCustomer2);
        potentialCustomer1.setId(null);
        assertThat(potentialCustomer1).isNotEqualTo(potentialCustomer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotentialCustomerDTO.class);
        PotentialCustomerDTO potentialCustomerDTO1 = new PotentialCustomerDTO();
        potentialCustomerDTO1.setId(1L);
        PotentialCustomerDTO potentialCustomerDTO2 = new PotentialCustomerDTO();
        assertThat(potentialCustomerDTO1).isNotEqualTo(potentialCustomerDTO2);
        potentialCustomerDTO2.setId(potentialCustomerDTO1.getId());
        assertThat(potentialCustomerDTO1).isEqualTo(potentialCustomerDTO2);
        potentialCustomerDTO2.setId(2L);
        assertThat(potentialCustomerDTO1).isNotEqualTo(potentialCustomerDTO2);
        potentialCustomerDTO1.setId(null);
        assertThat(potentialCustomerDTO1).isNotEqualTo(potentialCustomerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(potentialCustomerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(potentialCustomerMapper.fromId(null)).isNull();
    }
}
