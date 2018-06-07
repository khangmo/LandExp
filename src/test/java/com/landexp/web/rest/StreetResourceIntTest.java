package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.Street;
import com.landexp.repository.StreetRepository;
import com.landexp.repository.search.StreetSearchRepository;
import com.landexp.service.StreetService;
import com.landexp.service.dto.StreetDTO;
import com.landexp.service.mapper.StreetMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;

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

/**
 * Test class for the StreetResource REST controller.
 *
 * @see StreetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class StreetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private StreetRepository streetRepository;


    @Autowired
    private StreetMapper streetMapper;
    

    @Autowired
    private StreetService streetService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.StreetSearchRepositoryMockConfiguration
     */
    @Autowired
    private StreetSearchRepository mockStreetSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStreetMockMvc;

    private Street street;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StreetResource streetResource = new StreetResource(streetService);
        this.restStreetMockMvc = MockMvcBuilders.standaloneSetup(streetResource)
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
    public static Street createEntity(EntityManager em) {
        Street street = new Street()
            .name(DEFAULT_NAME)
            .postalCode(DEFAULT_POSTAL_CODE)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .enabled(DEFAULT_ENABLED)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return street;
    }

    @Before
    public void initTest() {
        street = createEntity(em);
    }

    @Test
    @Transactional
    public void createStreet() throws Exception {
        int databaseSizeBeforeCreate = streetRepository.findAll().size();

        // Create the Street
        StreetDTO streetDTO = streetMapper.toDto(street);
        restStreetMockMvc.perform(post("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streetDTO)))
            .andExpect(status().isCreated());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeCreate + 1);
        Street testStreet = streetList.get(streetList.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStreet.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testStreet.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testStreet.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testStreet.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testStreet.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Street in Elasticsearch
        verify(mockStreetSearchRepository, times(1)).save(testStreet);
    }

    @Test
    @Transactional
    public void createStreetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = streetRepository.findAll().size();

        // Create the Street with an existing ID
        street.setId(1L);
        StreetDTO streetDTO = streetMapper.toDto(street);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStreetMockMvc.perform(post("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeCreate);

        // Validate the Street in Elasticsearch
        verify(mockStreetSearchRepository, times(0)).save(street);
    }

    @Test
    @Transactional
    public void getAllStreets() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get all the streetList
        restStreetMockMvc.perform(get("/api/streets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(street.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", street.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(street.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingStreet() throws Exception {
        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        int databaseSizeBeforeUpdate = streetRepository.findAll().size();

        // Update the street
        Street updatedStreet = streetRepository.findById(street.getId()).get();
        // Disconnect from session so that the updates on updatedStreet are not directly saved in db
        em.detach(updatedStreet);
        updatedStreet
            .name(UPDATED_NAME)
            .postalCode(UPDATED_POSTAL_CODE)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .enabled(UPDATED_ENABLED)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        StreetDTO streetDTO = streetMapper.toDto(updatedStreet);

        restStreetMockMvc.perform(put("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streetDTO)))
            .andExpect(status().isOk());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeUpdate);
        Street testStreet = streetList.get(streetList.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStreet.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testStreet.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testStreet.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testStreet.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testStreet.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Street in Elasticsearch
        verify(mockStreetSearchRepository, times(1)).save(testStreet);
    }

    @Test
    @Transactional
    public void updateNonExistingStreet() throws Exception {
        int databaseSizeBeforeUpdate = streetRepository.findAll().size();

        // Create the Street
        StreetDTO streetDTO = streetMapper.toDto(street);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStreetMockMvc.perform(put("/api/streets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Street in the database
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Street in Elasticsearch
        verify(mockStreetSearchRepository, times(0)).save(street);
    }

    @Test
    @Transactional
    public void deleteStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        int databaseSizeBeforeDelete = streetRepository.findAll().size();

        // Get the street
        restStreetMockMvc.perform(delete("/api/streets/{id}", street.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Street> streetList = streetRepository.findAll();
        assertThat(streetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Street in Elasticsearch
        verify(mockStreetSearchRepository, times(1)).deleteById(street.getId());
    }

    @Test
    @Transactional
    public void searchStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);
        when(mockStreetSearchRepository.search(queryStringQuery("id:" + street.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(street), PageRequest.of(0, 1), 1));
        // Search the street
        restStreetMockMvc.perform(get("/api/_search/streets?query=id:" + street.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(street.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Street.class);
        Street street1 = new Street();
        street1.setId(1L);
        Street street2 = new Street();
        street2.setId(street1.getId());
        assertThat(street1).isEqualTo(street2);
        street2.setId(2L);
        assertThat(street1).isNotEqualTo(street2);
        street1.setId(null);
        assertThat(street1).isNotEqualTo(street2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StreetDTO.class);
        StreetDTO streetDTO1 = new StreetDTO();
        streetDTO1.setId(1L);
        StreetDTO streetDTO2 = new StreetDTO();
        assertThat(streetDTO1).isNotEqualTo(streetDTO2);
        streetDTO2.setId(streetDTO1.getId());
        assertThat(streetDTO1).isEqualTo(streetDTO2);
        streetDTO2.setId(2L);
        assertThat(streetDTO1).isNotEqualTo(streetDTO2);
        streetDTO1.setId(null);
        assertThat(streetDTO1).isNotEqualTo(streetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(streetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(streetMapper.fromId(null)).isNull();
    }
}
