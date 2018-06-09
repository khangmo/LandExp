package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.HousePhoto;
import com.landexp.repository.HousePhotoRepository;
import com.landexp.repository.search.HousePhotoSearchRepository;
import com.landexp.service.HousePhotoService;
import com.landexp.service.dto.HousePhotoDTO;
import com.landexp.service.mapper.HousePhotoMapper;
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
 * Test class for the HousePhotoResource REST controller.
 *
 * @see HousePhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class HousePhotoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HousePhotoRepository housePhotoRepository;


    @Autowired
    private HousePhotoMapper housePhotoMapper;
    

    @Autowired
    private HousePhotoService housePhotoService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.HousePhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private HousePhotoSearchRepository mockHousePhotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHousePhotoMockMvc;

    private HousePhoto housePhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HousePhotoResource housePhotoResource = new HousePhotoResource(housePhotoService);
        this.restHousePhotoMockMvc = MockMvcBuilders.standaloneSetup(housePhotoResource)
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
    public static HousePhoto createEntity(EntityManager em) {
        HousePhoto housePhoto = new HousePhoto()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .createAt(DEFAULT_CREATE_AT);
        return housePhoto;
    }

    @Before
    public void initTest() {
        housePhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createHousePhoto() throws Exception {
        int databaseSizeBeforeCreate = housePhotoRepository.findAll().size();

        // Create the HousePhoto
        HousePhotoDTO housePhotoDTO = housePhotoMapper.toDto(housePhoto);
        restHousePhotoMockMvc.perform(post("/api/house-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(housePhotoDTO)))
            .andExpect(status().isCreated());

        // Validate the HousePhoto in the database
        List<HousePhoto> housePhotoList = housePhotoRepository.findAll();
        assertThat(housePhotoList).hasSize(databaseSizeBeforeCreate + 1);
        HousePhoto testHousePhoto = housePhotoList.get(housePhotoList.size() - 1);
        assertThat(testHousePhoto.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHousePhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testHousePhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testHousePhoto.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);

        // Validate the HousePhoto in Elasticsearch
        verify(mockHousePhotoSearchRepository, times(1)).save(testHousePhoto);
    }

    @Test
    @Transactional
    public void createHousePhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = housePhotoRepository.findAll().size();

        // Create the HousePhoto with an existing ID
        housePhoto.setId(1L);
        HousePhotoDTO housePhotoDTO = housePhotoMapper.toDto(housePhoto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHousePhotoMockMvc.perform(post("/api/house-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(housePhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HousePhoto in the database
        List<HousePhoto> housePhotoList = housePhotoRepository.findAll();
        assertThat(housePhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the HousePhoto in Elasticsearch
        verify(mockHousePhotoSearchRepository, times(0)).save(housePhoto);
    }

    @Test
    @Transactional
    public void getAllHousePhotos() throws Exception {
        // Initialize the database
        housePhotoRepository.saveAndFlush(housePhoto);

        // Get all the housePhotoList
        restHousePhotoMockMvc.perform(get("/api/house-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(housePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getHousePhoto() throws Exception {
        // Initialize the database
        housePhotoRepository.saveAndFlush(housePhoto);

        // Get the housePhoto
        restHousePhotoMockMvc.perform(get("/api/house-photos/{id}", housePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(housePhoto.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingHousePhoto() throws Exception {
        // Get the housePhoto
        restHousePhotoMockMvc.perform(get("/api/house-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHousePhoto() throws Exception {
        // Initialize the database
        housePhotoRepository.saveAndFlush(housePhoto);

        int databaseSizeBeforeUpdate = housePhotoRepository.findAll().size();

        // Update the housePhoto
        HousePhoto updatedHousePhoto = housePhotoRepository.findById(housePhoto.getId()).get();
        // Disconnect from session so that the updates on updatedHousePhoto are not directly saved in db
        em.detach(updatedHousePhoto);
        updatedHousePhoto
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .createAt(UPDATED_CREATE_AT);
        HousePhotoDTO housePhotoDTO = housePhotoMapper.toDto(updatedHousePhoto);

        restHousePhotoMockMvc.perform(put("/api/house-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(housePhotoDTO)))
            .andExpect(status().isOk());

        // Validate the HousePhoto in the database
        List<HousePhoto> housePhotoList = housePhotoRepository.findAll();
        assertThat(housePhotoList).hasSize(databaseSizeBeforeUpdate);
        HousePhoto testHousePhoto = housePhotoList.get(housePhotoList.size() - 1);
        assertThat(testHousePhoto.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHousePhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testHousePhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testHousePhoto.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);

        // Validate the HousePhoto in Elasticsearch
        verify(mockHousePhotoSearchRepository, times(1)).save(testHousePhoto);
    }

    @Test
    @Transactional
    public void updateNonExistingHousePhoto() throws Exception {
        int databaseSizeBeforeUpdate = housePhotoRepository.findAll().size();

        // Create the HousePhoto
        HousePhotoDTO housePhotoDTO = housePhotoMapper.toDto(housePhoto);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHousePhotoMockMvc.perform(put("/api/house-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(housePhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HousePhoto in the database
        List<HousePhoto> housePhotoList = housePhotoRepository.findAll();
        assertThat(housePhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HousePhoto in Elasticsearch
        verify(mockHousePhotoSearchRepository, times(0)).save(housePhoto);
    }

    @Test
    @Transactional
    public void deleteHousePhoto() throws Exception {
        // Initialize the database
        housePhotoRepository.saveAndFlush(housePhoto);

        int databaseSizeBeforeDelete = housePhotoRepository.findAll().size();

        // Get the housePhoto
        restHousePhotoMockMvc.perform(delete("/api/house-photos/{id}", housePhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HousePhoto> housePhotoList = housePhotoRepository.findAll();
        assertThat(housePhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HousePhoto in Elasticsearch
        verify(mockHousePhotoSearchRepository, times(1)).deleteById(housePhoto.getId());
    }

    @Test
    @Transactional
    public void searchHousePhoto() throws Exception {
        // Initialize the database
        housePhotoRepository.saveAndFlush(housePhoto);
        when(mockHousePhotoSearchRepository.search(queryStringQuery("id:" + housePhoto.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(housePhoto), PageRequest.of(0, 1), 1));
        // Search the housePhoto
        restHousePhotoMockMvc.perform(get("/api/_search/house-photos?query=id:" + housePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(housePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HousePhoto.class);
        HousePhoto housePhoto1 = new HousePhoto();
        housePhoto1.setId(1L);
        HousePhoto housePhoto2 = new HousePhoto();
        housePhoto2.setId(housePhoto1.getId());
        assertThat(housePhoto1).isEqualTo(housePhoto2);
        housePhoto2.setId(2L);
        assertThat(housePhoto1).isNotEqualTo(housePhoto2);
        housePhoto1.setId(null);
        assertThat(housePhoto1).isNotEqualTo(housePhoto2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HousePhotoDTO.class);
        HousePhotoDTO housePhotoDTO1 = new HousePhotoDTO();
        housePhotoDTO1.setId(1L);
        HousePhotoDTO housePhotoDTO2 = new HousePhotoDTO();
        assertThat(housePhotoDTO1).isNotEqualTo(housePhotoDTO2);
        housePhotoDTO2.setId(housePhotoDTO1.getId());
        assertThat(housePhotoDTO1).isEqualTo(housePhotoDTO2);
        housePhotoDTO2.setId(2L);
        assertThat(housePhotoDTO1).isNotEqualTo(housePhotoDTO2);
        housePhotoDTO1.setId(null);
        assertThat(housePhotoDTO1).isNotEqualTo(housePhotoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(housePhotoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(housePhotoMapper.fromId(null)).isNull();
    }
}
