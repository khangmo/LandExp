package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.LandProject;
import com.landexp.repository.LandProjectRepository;
import com.landexp.repository.search.LandProjectSearchRepository;
import com.landexp.service.LandProjectService;
import com.landexp.service.dto.LandProjectDTO;
import com.landexp.service.mapper.LandProjectMapper;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the LandProjectResource REST controller.
 *
 * @see LandProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class LandProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private LandProjectRepository landProjectRepository;


    @Autowired
    private LandProjectMapper landProjectMapper;
    

    @Autowired
    private LandProjectService landProjectService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.LandProjectSearchRepositoryMockConfiguration
     */
    @Autowired
    private LandProjectSearchRepository mockLandProjectSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLandProjectMockMvc;

    private LandProject landProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LandProjectResource landProjectResource = new LandProjectResource(landProjectService);
        this.restLandProjectMockMvc = MockMvcBuilders.standaloneSetup(landProjectResource)
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
    public static LandProject createEntity(EntityManager em) {
        LandProject landProject = new LandProject()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return landProject;
    }

    @Before
    public void initTest() {
        landProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createLandProject() throws Exception {
        int databaseSizeBeforeCreate = landProjectRepository.findAll().size();

        // Create the LandProject
        LandProjectDTO landProjectDTO = landProjectMapper.toDto(landProject);
        restLandProjectMockMvc.perform(post("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the LandProject in the database
        List<LandProject> landProjectList = landProjectRepository.findAll();
        assertThat(landProjectList).hasSize(databaseSizeBeforeCreate + 1);
        LandProject testLandProject = landProjectList.get(landProjectList.size() - 1);
        assertThat(testLandProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLandProject.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLandProject.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the LandProject in Elasticsearch
        verify(mockLandProjectSearchRepository, times(1)).save(testLandProject);
    }

    @Test
    @Transactional
    public void createLandProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = landProjectRepository.findAll().size();

        // Create the LandProject with an existing ID
        landProject.setId(1L);
        LandProjectDTO landProjectDTO = landProjectMapper.toDto(landProject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandProjectMockMvc.perform(post("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProject in the database
        List<LandProject> landProjectList = landProjectRepository.findAll();
        assertThat(landProjectList).hasSize(databaseSizeBeforeCreate);

        // Validate the LandProject in Elasticsearch
        verify(mockLandProjectSearchRepository, times(0)).save(landProject);
    }

    @Test
    @Transactional
    public void getAllLandProjects() throws Exception {
        // Initialize the database
        landProjectRepository.saveAndFlush(landProject);

        // Get all the landProjectList
        restLandProjectMockMvc.perform(get("/api/land-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    

    @Test
    @Transactional
    public void getLandProject() throws Exception {
        // Initialize the database
        landProjectRepository.saveAndFlush(landProject);

        // Get the landProject
        restLandProjectMockMvc.perform(get("/api/land-projects/{id}", landProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(landProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }
    @Test
    @Transactional
    public void getNonExistingLandProject() throws Exception {
        // Get the landProject
        restLandProjectMockMvc.perform(get("/api/land-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLandProject() throws Exception {
        // Initialize the database
        landProjectRepository.saveAndFlush(landProject);

        int databaseSizeBeforeUpdate = landProjectRepository.findAll().size();

        // Update the landProject
        LandProject updatedLandProject = landProjectRepository.findById(landProject.getId()).get();
        // Disconnect from session so that the updates on updatedLandProject are not directly saved in db
        em.detach(updatedLandProject);
        updatedLandProject
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        LandProjectDTO landProjectDTO = landProjectMapper.toDto(updatedLandProject);

        restLandProjectMockMvc.perform(put("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectDTO)))
            .andExpect(status().isOk());

        // Validate the LandProject in the database
        List<LandProject> landProjectList = landProjectRepository.findAll();
        assertThat(landProjectList).hasSize(databaseSizeBeforeUpdate);
        LandProject testLandProject = landProjectList.get(landProjectList.size() - 1);
        assertThat(testLandProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLandProject.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLandProject.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the LandProject in Elasticsearch
        verify(mockLandProjectSearchRepository, times(1)).save(testLandProject);
    }

    @Test
    @Transactional
    public void updateNonExistingLandProject() throws Exception {
        int databaseSizeBeforeUpdate = landProjectRepository.findAll().size();

        // Create the LandProject
        LandProjectDTO landProjectDTO = landProjectMapper.toDto(landProject);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLandProjectMockMvc.perform(put("/api/land-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProject in the database
        List<LandProject> landProjectList = landProjectRepository.findAll();
        assertThat(landProjectList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LandProject in Elasticsearch
        verify(mockLandProjectSearchRepository, times(0)).save(landProject);
    }

    @Test
    @Transactional
    public void deleteLandProject() throws Exception {
        // Initialize the database
        landProjectRepository.saveAndFlush(landProject);

        int databaseSizeBeforeDelete = landProjectRepository.findAll().size();

        // Get the landProject
        restLandProjectMockMvc.perform(delete("/api/land-projects/{id}", landProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LandProject> landProjectList = landProjectRepository.findAll();
        assertThat(landProjectList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LandProject in Elasticsearch
        verify(mockLandProjectSearchRepository, times(1)).deleteById(landProject.getId());
    }

    @Test
    @Transactional
    public void searchLandProject() throws Exception {
        // Initialize the database
        landProjectRepository.saveAndFlush(landProject);
        when(mockLandProjectSearchRepository.search(queryStringQuery("id:" + landProject.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(landProject), PageRequest.of(0, 1), 1));
        // Search the landProject
        restLandProjectMockMvc.perform(get("/api/_search/land-projects?query=id:" + landProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProject.class);
        LandProject landProject1 = new LandProject();
        landProject1.setId(1L);
        LandProject landProject2 = new LandProject();
        landProject2.setId(landProject1.getId());
        assertThat(landProject1).isEqualTo(landProject2);
        landProject2.setId(2L);
        assertThat(landProject1).isNotEqualTo(landProject2);
        landProject1.setId(null);
        assertThat(landProject1).isNotEqualTo(landProject2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProjectDTO.class);
        LandProjectDTO landProjectDTO1 = new LandProjectDTO();
        landProjectDTO1.setId(1L);
        LandProjectDTO landProjectDTO2 = new LandProjectDTO();
        assertThat(landProjectDTO1).isNotEqualTo(landProjectDTO2);
        landProjectDTO2.setId(landProjectDTO1.getId());
        assertThat(landProjectDTO1).isEqualTo(landProjectDTO2);
        landProjectDTO2.setId(2L);
        assertThat(landProjectDTO1).isNotEqualTo(landProjectDTO2);
        landProjectDTO1.setId(null);
        assertThat(landProjectDTO1).isNotEqualTo(landProjectDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(landProjectMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(landProjectMapper.fromId(null)).isNull();
    }
}
