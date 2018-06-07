package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.LandProjects;
import com.landexp.repository.LandProjectsRepository;
import com.landexp.repository.search.LandProjectsSearchRepository;
import com.landexp.service.LandProjectsService;
import com.landexp.service.dto.LandProjectsDTO;
import com.landexp.service.mapper.LandProjectsMapper;
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
 * Test class for the LandProjectsResource REST controller.
 *
 * @see LandProjectsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class LandProjectsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private LandProjectsRepository landProjectsRepository;


    @Autowired
    private LandProjectsMapper landProjectsMapper;
    

    @Autowired
    private LandProjectsService landProjectsService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.LandProjectsSearchRepositoryMockConfiguration
     */
    @Autowired
    private LandProjectsSearchRepository mockLandProjectsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLandProjectsMockMvc;

    private LandProjects landProjects;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LandProjectsResource landProjectsResource = new LandProjectsResource(landProjectsService);
        this.restLandProjectsMockMvc = MockMvcBuilders.standaloneSetup(landProjectsResource)
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
    public static LandProjects createEntity(EntityManager em) {
        LandProjects landProjects = new LandProjects()
            .name(DEFAULT_NAME);
        return landProjects;
    }

    @Before
    public void initTest() {
        landProjects = createEntity(em);
    }

    @Test
    @Transactional
    public void createLandProjects() throws Exception {
        int databaseSizeBeforeCreate = landProjectsRepository.findAll().size();

        // Create the LandProjects
        LandProjectsDTO landProjectsDTO = landProjectsMapper.toDto(landProjects);
        restLandProjectsMockMvc.perform(post("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectsDTO)))
            .andExpect(status().isCreated());

        // Validate the LandProjects in the database
        List<LandProjects> landProjectsList = landProjectsRepository.findAll();
        assertThat(landProjectsList).hasSize(databaseSizeBeforeCreate + 1);
        LandProjects testLandProjects = landProjectsList.get(landProjectsList.size() - 1);
        assertThat(testLandProjects.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the LandProjects in Elasticsearch
        verify(mockLandProjectsSearchRepository, times(1)).save(testLandProjects);
    }

    @Test
    @Transactional
    public void createLandProjectsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = landProjectsRepository.findAll().size();

        // Create the LandProjects with an existing ID
        landProjects.setId(1L);
        LandProjectsDTO landProjectsDTO = landProjectsMapper.toDto(landProjects);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandProjectsMockMvc.perform(post("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProjects in the database
        List<LandProjects> landProjectsList = landProjectsRepository.findAll();
        assertThat(landProjectsList).hasSize(databaseSizeBeforeCreate);

        // Validate the LandProjects in Elasticsearch
        verify(mockLandProjectsSearchRepository, times(0)).save(landProjects);
    }

    @Test
    @Transactional
    public void getAllLandProjects() throws Exception {
        // Initialize the database
        landProjectsRepository.saveAndFlush(landProjects);

        // Get all the landProjectsList
        restLandProjectsMockMvc.perform(get("/api/land-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProjects.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getLandProjects() throws Exception {
        // Initialize the database
        landProjectsRepository.saveAndFlush(landProjects);

        // Get the landProjects
        restLandProjectsMockMvc.perform(get("/api/land-projects/{id}", landProjects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(landProjects.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLandProjects() throws Exception {
        // Get the landProjects
        restLandProjectsMockMvc.perform(get("/api/land-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLandProjects() throws Exception {
        // Initialize the database
        landProjectsRepository.saveAndFlush(landProjects);

        int databaseSizeBeforeUpdate = landProjectsRepository.findAll().size();

        // Update the landProjects
        LandProjects updatedLandProjects = landProjectsRepository.findById(landProjects.getId()).get();
        // Disconnect from session so that the updates on updatedLandProjects are not directly saved in db
        em.detach(updatedLandProjects);
        updatedLandProjects
            .name(UPDATED_NAME);
        LandProjectsDTO landProjectsDTO = landProjectsMapper.toDto(updatedLandProjects);

        restLandProjectsMockMvc.perform(put("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectsDTO)))
            .andExpect(status().isOk());

        // Validate the LandProjects in the database
        List<LandProjects> landProjectsList = landProjectsRepository.findAll();
        assertThat(landProjectsList).hasSize(databaseSizeBeforeUpdate);
        LandProjects testLandProjects = landProjectsList.get(landProjectsList.size() - 1);
        assertThat(testLandProjects.getName()).isEqualTo(UPDATED_NAME);

        // Validate the LandProjects in Elasticsearch
        verify(mockLandProjectsSearchRepository, times(1)).save(testLandProjects);
    }

    @Test
    @Transactional
    public void updateNonExistingLandProjects() throws Exception {
        int databaseSizeBeforeUpdate = landProjectsRepository.findAll().size();

        // Create the LandProjects
        LandProjectsDTO landProjectsDTO = landProjectsMapper.toDto(landProjects);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLandProjectsMockMvc.perform(put("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProjects in the database
        List<LandProjects> landProjectsList = landProjectsRepository.findAll();
        assertThat(landProjectsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LandProjects in Elasticsearch
        verify(mockLandProjectsSearchRepository, times(0)).save(landProjects);
    }

    @Test
    @Transactional
    public void deleteLandProjects() throws Exception {
        // Initialize the database
        landProjectsRepository.saveAndFlush(landProjects);

        int databaseSizeBeforeDelete = landProjectsRepository.findAll().size();

        // Get the landProjects
        restLandProjectsMockMvc.perform(delete("/api/land-projects/{id}", landProjects.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LandProjects> landProjectsList = landProjectsRepository.findAll();
        assertThat(landProjectsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LandProjects in Elasticsearch
        verify(mockLandProjectsSearchRepository, times(1)).deleteById(landProjects.getId());
    }

    @Test
    @Transactional
    public void searchLandProjects() throws Exception {
        // Initialize the database
        landProjectsRepository.saveAndFlush(landProjects);
        when(mockLandProjectsSearchRepository.search(queryStringQuery("id:" + landProjects.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(landProjects), PageRequest.of(0, 1), 1));
        // Search the landProjects
        restLandProjectsMockMvc.perform(get("/api/_search/land-projects?query=id:" + landProjects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProjects.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProjects.class);
        LandProjects landProjects1 = new LandProjects();
        landProjects1.setId(1L);
        LandProjects landProjects2 = new LandProjects();
        landProjects2.setId(landProjects1.getId());
        assertThat(landProjects1).isEqualTo(landProjects2);
        landProjects2.setId(2L);
        assertThat(landProjects1).isNotEqualTo(landProjects2);
        landProjects1.setId(null);
        assertThat(landProjects1).isNotEqualTo(landProjects2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProjectsDTO.class);
        LandProjectsDTO landProjectsDTO1 = new LandProjectsDTO();
        landProjectsDTO1.setId(1L);
        LandProjectsDTO landProjectsDTO2 = new LandProjectsDTO();
        assertThat(landProjectsDTO1).isNotEqualTo(landProjectsDTO2);
        landProjectsDTO2.setId(landProjectsDTO1.getId());
        assertThat(landProjectsDTO1).isEqualTo(landProjectsDTO2);
        landProjectsDTO2.setId(2L);
        assertThat(landProjectsDTO1).isNotEqualTo(landProjectsDTO2);
        landProjectsDTO1.setId(null);
        assertThat(landProjectsDTO1).isNotEqualTo(landProjectsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(landProjectsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(landProjectsMapper.fromId(null)).isNull();
    }
}
