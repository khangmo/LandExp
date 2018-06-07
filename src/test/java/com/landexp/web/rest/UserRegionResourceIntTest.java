package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.UserRegion;
import com.landexp.repository.UserRegionRepository;
import com.landexp.repository.search.UserRegionSearchRepository;
import com.landexp.service.UserRegionService;
import com.landexp.service.dto.UserRegionDTO;
import com.landexp.service.mapper.UserRegionMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the UserRegionResource REST controller.
 *
 * @see UserRegionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class UserRegionResourceIntTest {

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserRegionRepository userRegionRepository;
    @Mock
    private UserRegionRepository userRegionRepositoryMock;

    @Autowired
    private UserRegionMapper userRegionMapper;
    
    @Mock
    private UserRegionService userRegionServiceMock;

    @Autowired
    private UserRegionService userRegionService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.UserRegionSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserRegionSearchRepository mockUserRegionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserRegionMockMvc;

    private UserRegion userRegion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserRegionResource userRegionResource = new UserRegionResource(userRegionService);
        this.restUserRegionMockMvc = MockMvcBuilders.standaloneSetup(userRegionResource)
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
    public static UserRegion createEntity(EntityManager em) {
        UserRegion userRegion = new UserRegion()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return userRegion;
    }

    @Before
    public void initTest() {
        userRegion = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRegion() throws Exception {
        int databaseSizeBeforeCreate = userRegionRepository.findAll().size();

        // Create the UserRegion
        UserRegionDTO userRegionDTO = userRegionMapper.toDto(userRegion);
        restUserRegionMockMvc.perform(post("/api/user-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRegionDTO)))
            .andExpect(status().isCreated());

        // Validate the UserRegion in the database
        List<UserRegion> userRegionList = userRegionRepository.findAll();
        assertThat(userRegionList).hasSize(databaseSizeBeforeCreate + 1);
        UserRegion testUserRegion = userRegionList.get(userRegionList.size() - 1);
        assertThat(testUserRegion.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUserRegion.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the UserRegion in Elasticsearch
        verify(mockUserRegionSearchRepository, times(1)).save(testUserRegion);
    }

    @Test
    @Transactional
    public void createUserRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRegionRepository.findAll().size();

        // Create the UserRegion with an existing ID
        userRegion.setId(1L);
        UserRegionDTO userRegionDTO = userRegionMapper.toDto(userRegion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRegionMockMvc.perform(post("/api/user-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRegionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRegion in the database
        List<UserRegion> userRegionList = userRegionRepository.findAll();
        assertThat(userRegionList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserRegion in Elasticsearch
        verify(mockUserRegionSearchRepository, times(0)).save(userRegion);
    }

    @Test
    @Transactional
    public void getAllUserRegions() throws Exception {
        // Initialize the database
        userRegionRepository.saveAndFlush(userRegion);

        // Get all the userRegionList
        restUserRegionMockMvc.perform(get("/api/user-regions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    
    public void getAllUserRegionsWithEagerRelationshipsIsEnabled() throws Exception {
        UserRegionResource userRegionResource = new UserRegionResource(userRegionServiceMock);
        when(userRegionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserRegionMockMvc = MockMvcBuilders.standaloneSetup(userRegionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserRegionMockMvc.perform(get("/api/user-regions?eagerload=true"))
        .andExpect(status().isOk());

        verify(userRegionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllUserRegionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserRegionResource userRegionResource = new UserRegionResource(userRegionServiceMock);
            when(userRegionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserRegionMockMvc = MockMvcBuilders.standaloneSetup(userRegionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserRegionMockMvc.perform(get("/api/user-regions?eagerload=true"))
        .andExpect(status().isOk());

            verify(userRegionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserRegion() throws Exception {
        // Initialize the database
        userRegionRepository.saveAndFlush(userRegion);

        // Get the userRegion
        restUserRegionMockMvc.perform(get("/api/user-regions/{id}", userRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRegion.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingUserRegion() throws Exception {
        // Get the userRegion
        restUserRegionMockMvc.perform(get("/api/user-regions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRegion() throws Exception {
        // Initialize the database
        userRegionRepository.saveAndFlush(userRegion);

        int databaseSizeBeforeUpdate = userRegionRepository.findAll().size();

        // Update the userRegion
        UserRegion updatedUserRegion = userRegionRepository.findById(userRegion.getId()).get();
        // Disconnect from session so that the updates on updatedUserRegion are not directly saved in db
        em.detach(updatedUserRegion);
        updatedUserRegion
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        UserRegionDTO userRegionDTO = userRegionMapper.toDto(updatedUserRegion);

        restUserRegionMockMvc.perform(put("/api/user-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRegionDTO)))
            .andExpect(status().isOk());

        // Validate the UserRegion in the database
        List<UserRegion> userRegionList = userRegionRepository.findAll();
        assertThat(userRegionList).hasSize(databaseSizeBeforeUpdate);
        UserRegion testUserRegion = userRegionList.get(userRegionList.size() - 1);
        assertThat(testUserRegion.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUserRegion.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the UserRegion in Elasticsearch
        verify(mockUserRegionSearchRepository, times(1)).save(testUserRegion);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRegion() throws Exception {
        int databaseSizeBeforeUpdate = userRegionRepository.findAll().size();

        // Create the UserRegion
        UserRegionDTO userRegionDTO = userRegionMapper.toDto(userRegion);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserRegionMockMvc.perform(put("/api/user-regions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRegionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRegion in the database
        List<UserRegion> userRegionList = userRegionRepository.findAll();
        assertThat(userRegionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserRegion in Elasticsearch
        verify(mockUserRegionSearchRepository, times(0)).save(userRegion);
    }

    @Test
    @Transactional
    public void deleteUserRegion() throws Exception {
        // Initialize the database
        userRegionRepository.saveAndFlush(userRegion);

        int databaseSizeBeforeDelete = userRegionRepository.findAll().size();

        // Get the userRegion
        restUserRegionMockMvc.perform(delete("/api/user-regions/{id}", userRegion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRegion> userRegionList = userRegionRepository.findAll();
        assertThat(userRegionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserRegion in Elasticsearch
        verify(mockUserRegionSearchRepository, times(1)).deleteById(userRegion.getId());
    }

    @Test
    @Transactional
    public void searchUserRegion() throws Exception {
        // Initialize the database
        userRegionRepository.saveAndFlush(userRegion);
        when(mockUserRegionSearchRepository.search(queryStringQuery("id:" + userRegion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userRegion), PageRequest.of(0, 1), 1));
        // Search the userRegion
        restUserRegionMockMvc.perform(get("/api/_search/user-regions?query=id:" + userRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRegion.class);
        UserRegion userRegion1 = new UserRegion();
        userRegion1.setId(1L);
        UserRegion userRegion2 = new UserRegion();
        userRegion2.setId(userRegion1.getId());
        assertThat(userRegion1).isEqualTo(userRegion2);
        userRegion2.setId(2L);
        assertThat(userRegion1).isNotEqualTo(userRegion2);
        userRegion1.setId(null);
        assertThat(userRegion1).isNotEqualTo(userRegion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRegionDTO.class);
        UserRegionDTO userRegionDTO1 = new UserRegionDTO();
        userRegionDTO1.setId(1L);
        UserRegionDTO userRegionDTO2 = new UserRegionDTO();
        assertThat(userRegionDTO1).isNotEqualTo(userRegionDTO2);
        userRegionDTO2.setId(userRegionDTO1.getId());
        assertThat(userRegionDTO1).isEqualTo(userRegionDTO2);
        userRegionDTO2.setId(2L);
        assertThat(userRegionDTO1).isNotEqualTo(userRegionDTO2);
        userRegionDTO1.setId(null);
        assertThat(userRegionDTO1).isNotEqualTo(userRegionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userRegionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userRegionMapper.fromId(null)).isNull();
    }
}
