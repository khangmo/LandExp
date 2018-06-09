package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.LandProjectPhoto;
import com.landexp.repository.LandProjectPhotoRepository;
import com.landexp.repository.search.LandProjectPhotoSearchRepository;
import com.landexp.service.LandProjectPhotoService;
import com.landexp.service.dto.LandProjectPhotoDTO;
import com.landexp.service.mapper.LandProjectPhotoMapper;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the LandProjectPhotoResource REST controller.
 *
 * @see LandProjectPhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class LandProjectPhotoResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private LandProjectPhotoRepository landProjectPhotoRepository;


    @Autowired
    private LandProjectPhotoMapper landProjectPhotoMapper;
    

    @Autowired
    private LandProjectPhotoService landProjectPhotoService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.LandProjectPhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private LandProjectPhotoSearchRepository mockLandProjectPhotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLandProjectPhotoMockMvc;

    private LandProjectPhoto landProjectPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LandProjectPhotoResource landProjectPhotoResource = new LandProjectPhotoResource(landProjectPhotoService);
        this.restLandProjectPhotoMockMvc = MockMvcBuilders.standaloneSetup(landProjectPhotoResource)
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
    public static LandProjectPhoto createEntity(EntityManager em) {
        LandProjectPhoto landProjectPhoto = new LandProjectPhoto()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .createAt(DEFAULT_CREATE_AT);
        return landProjectPhoto;
    }

    @Before
    public void initTest() {
        landProjectPhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createLandProjectPhoto() throws Exception {
        int databaseSizeBeforeCreate = landProjectPhotoRepository.findAll().size();

        // Create the LandProjectPhoto
        LandProjectPhotoDTO landProjectPhotoDTO = landProjectPhotoMapper.toDto(landProjectPhoto);
        restLandProjectPhotoMockMvc.perform(post("/api/land-project-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectPhotoDTO)))
            .andExpect(status().isCreated());

        // Validate the LandProjectPhoto in the database
        List<LandProjectPhoto> landProjectPhotoList = landProjectPhotoRepository.findAll();
        assertThat(landProjectPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        LandProjectPhoto testLandProjectPhoto = landProjectPhotoList.get(landProjectPhotoList.size() - 1);
        assertThat(testLandProjectPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLandProjectPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testLandProjectPhoto.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);

        // Validate the LandProjectPhoto in Elasticsearch
        verify(mockLandProjectPhotoSearchRepository, times(1)).save(testLandProjectPhoto);
    }

    @Test
    @Transactional
    public void createLandProjectPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = landProjectPhotoRepository.findAll().size();

        // Create the LandProjectPhoto with an existing ID
        landProjectPhoto.setId(1L);
        LandProjectPhotoDTO landProjectPhotoDTO = landProjectPhotoMapper.toDto(landProjectPhoto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandProjectPhotoMockMvc.perform(post("/api/land-project-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProjectPhoto in the database
        List<LandProjectPhoto> landProjectPhotoList = landProjectPhotoRepository.findAll();
        assertThat(landProjectPhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the LandProjectPhoto in Elasticsearch
        verify(mockLandProjectPhotoSearchRepository, times(0)).save(landProjectPhoto);
    }

    @Test
    @Transactional
    public void getAllLandProjectPhotos() throws Exception {
        // Initialize the database
        landProjectPhotoRepository.saveAndFlush(landProjectPhoto);

        // Get all the landProjectPhotoList
        restLandProjectPhotoMockMvc.perform(get("/api/land-project-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProjectPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getLandProjectPhoto() throws Exception {
        // Initialize the database
        landProjectPhotoRepository.saveAndFlush(landProjectPhoto);

        // Get the landProjectPhoto
        restLandProjectPhotoMockMvc.perform(get("/api/land-project-photos/{id}", landProjectPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(landProjectPhoto.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLandProjectPhoto() throws Exception {
        // Get the landProjectPhoto
        restLandProjectPhotoMockMvc.perform(get("/api/land-project-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLandProjectPhoto() throws Exception {
        // Initialize the database
        landProjectPhotoRepository.saveAndFlush(landProjectPhoto);

        int databaseSizeBeforeUpdate = landProjectPhotoRepository.findAll().size();

        // Update the landProjectPhoto
        LandProjectPhoto updatedLandProjectPhoto = landProjectPhotoRepository.findById(landProjectPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedLandProjectPhoto are not directly saved in db
        em.detach(updatedLandProjectPhoto);
        updatedLandProjectPhoto
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .createAt(UPDATED_CREATE_AT);
        LandProjectPhotoDTO landProjectPhotoDTO = landProjectPhotoMapper.toDto(updatedLandProjectPhoto);

        restLandProjectPhotoMockMvc.perform(put("/api/land-project-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectPhotoDTO)))
            .andExpect(status().isOk());

        // Validate the LandProjectPhoto in the database
        List<LandProjectPhoto> landProjectPhotoList = landProjectPhotoRepository.findAll();
        assertThat(landProjectPhotoList).hasSize(databaseSizeBeforeUpdate);
        LandProjectPhoto testLandProjectPhoto = landProjectPhotoList.get(landProjectPhotoList.size() - 1);
        assertThat(testLandProjectPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLandProjectPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testLandProjectPhoto.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);

        // Validate the LandProjectPhoto in Elasticsearch
        verify(mockLandProjectPhotoSearchRepository, times(1)).save(testLandProjectPhoto);
    }

    @Test
    @Transactional
    public void updateNonExistingLandProjectPhoto() throws Exception {
        int databaseSizeBeforeUpdate = landProjectPhotoRepository.findAll().size();

        // Create the LandProjectPhoto
        LandProjectPhotoDTO landProjectPhotoDTO = landProjectPhotoMapper.toDto(landProjectPhoto);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLandProjectPhotoMockMvc.perform(put("/api/land-project-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landProjectPhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LandProjectPhoto in the database
        List<LandProjectPhoto> landProjectPhotoList = landProjectPhotoRepository.findAll();
        assertThat(landProjectPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LandProjectPhoto in Elasticsearch
        verify(mockLandProjectPhotoSearchRepository, times(0)).save(landProjectPhoto);
    }

    @Test
    @Transactional
    public void deleteLandProjectPhoto() throws Exception {
        // Initialize the database
        landProjectPhotoRepository.saveAndFlush(landProjectPhoto);

        int databaseSizeBeforeDelete = landProjectPhotoRepository.findAll().size();

        // Get the landProjectPhoto
        restLandProjectPhotoMockMvc.perform(delete("/api/land-project-photos/{id}", landProjectPhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LandProjectPhoto> landProjectPhotoList = landProjectPhotoRepository.findAll();
        assertThat(landProjectPhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LandProjectPhoto in Elasticsearch
        verify(mockLandProjectPhotoSearchRepository, times(1)).deleteById(landProjectPhoto.getId());
    }

    @Test
    @Transactional
    public void searchLandProjectPhoto() throws Exception {
        // Initialize the database
        landProjectPhotoRepository.saveAndFlush(landProjectPhoto);
        when(mockLandProjectPhotoSearchRepository.search(queryStringQuery("id:" + landProjectPhoto.getId())))
            .thenReturn(Collections.singletonList(landProjectPhoto));
        // Search the landProjectPhoto
        restLandProjectPhotoMockMvc.perform(get("/api/_search/land-project-photos?query=id:" + landProjectPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landProjectPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProjectPhoto.class);
        LandProjectPhoto landProjectPhoto1 = new LandProjectPhoto();
        landProjectPhoto1.setId(1L);
        LandProjectPhoto landProjectPhoto2 = new LandProjectPhoto();
        landProjectPhoto2.setId(landProjectPhoto1.getId());
        assertThat(landProjectPhoto1).isEqualTo(landProjectPhoto2);
        landProjectPhoto2.setId(2L);
        assertThat(landProjectPhoto1).isNotEqualTo(landProjectPhoto2);
        landProjectPhoto1.setId(null);
        assertThat(landProjectPhoto1).isNotEqualTo(landProjectPhoto2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandProjectPhotoDTO.class);
        LandProjectPhotoDTO landProjectPhotoDTO1 = new LandProjectPhotoDTO();
        landProjectPhotoDTO1.setId(1L);
        LandProjectPhotoDTO landProjectPhotoDTO2 = new LandProjectPhotoDTO();
        assertThat(landProjectPhotoDTO1).isNotEqualTo(landProjectPhotoDTO2);
        landProjectPhotoDTO2.setId(landProjectPhotoDTO1.getId());
        assertThat(landProjectPhotoDTO1).isEqualTo(landProjectPhotoDTO2);
        landProjectPhotoDTO2.setId(2L);
        assertThat(landProjectPhotoDTO1).isNotEqualTo(landProjectPhotoDTO2);
        landProjectPhotoDTO1.setId(null);
        assertThat(landProjectPhotoDTO1).isNotEqualTo(landProjectPhotoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(landProjectPhotoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(landProjectPhotoMapper.fromId(null)).isNull();
    }
}
