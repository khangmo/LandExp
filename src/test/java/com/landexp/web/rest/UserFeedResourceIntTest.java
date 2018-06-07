package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.UserFeed;
import com.landexp.repository.UserFeedRepository;
import com.landexp.repository.search.UserFeedSearchRepository;
import com.landexp.service.UserFeedService;
import com.landexp.service.dto.UserFeedDTO;
import com.landexp.service.mapper.UserFeedMapper;
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
 * Test class for the UserFeedResource REST controller.
 *
 * @see UserFeedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class UserFeedResourceIntTest {

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserFeedRepository userFeedRepository;


    @Autowired
    private UserFeedMapper userFeedMapper;
    

    @Autowired
    private UserFeedService userFeedService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.UserFeedSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserFeedSearchRepository mockUserFeedSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserFeedMockMvc;

    private UserFeed userFeed;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserFeedResource userFeedResource = new UserFeedResource(userFeedService);
        this.restUserFeedMockMvc = MockMvcBuilders.standaloneSetup(userFeedResource)
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
    public static UserFeed createEntity(EntityManager em) {
        UserFeed userFeed = new UserFeed()
            .body(DEFAULT_BODY)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return userFeed;
    }

    @Before
    public void initTest() {
        userFeed = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserFeed() throws Exception {
        int databaseSizeBeforeCreate = userFeedRepository.findAll().size();

        // Create the UserFeed
        UserFeedDTO userFeedDTO = userFeedMapper.toDto(userFeed);
        restUserFeedMockMvc.perform(post("/api/user-feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFeedDTO)))
            .andExpect(status().isCreated());

        // Validate the UserFeed in the database
        List<UserFeed> userFeedList = userFeedRepository.findAll();
        assertThat(userFeedList).hasSize(databaseSizeBeforeCreate + 1);
        UserFeed testUserFeed = userFeedList.get(userFeedList.size() - 1);
        assertThat(testUserFeed.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testUserFeed.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUserFeed.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the UserFeed in Elasticsearch
        verify(mockUserFeedSearchRepository, times(1)).save(testUserFeed);
    }

    @Test
    @Transactional
    public void createUserFeedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userFeedRepository.findAll().size();

        // Create the UserFeed with an existing ID
        userFeed.setId(1L);
        UserFeedDTO userFeedDTO = userFeedMapper.toDto(userFeed);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFeedMockMvc.perform(post("/api/user-feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFeedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserFeed in the database
        List<UserFeed> userFeedList = userFeedRepository.findAll();
        assertThat(userFeedList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserFeed in Elasticsearch
        verify(mockUserFeedSearchRepository, times(0)).save(userFeed);
    }

    @Test
    @Transactional
    public void getAllUserFeeds() throws Exception {
        // Initialize the database
        userFeedRepository.saveAndFlush(userFeed);

        // Get all the userFeedList
        restUserFeedMockMvc.perform(get("/api/user-feeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getUserFeed() throws Exception {
        // Initialize the database
        userFeedRepository.saveAndFlush(userFeed);

        // Get the userFeed
        restUserFeedMockMvc.perform(get("/api/user-feeds/{id}", userFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userFeed.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingUserFeed() throws Exception {
        // Get the userFeed
        restUserFeedMockMvc.perform(get("/api/user-feeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFeed() throws Exception {
        // Initialize the database
        userFeedRepository.saveAndFlush(userFeed);

        int databaseSizeBeforeUpdate = userFeedRepository.findAll().size();

        // Update the userFeed
        UserFeed updatedUserFeed = userFeedRepository.findById(userFeed.getId()).get();
        // Disconnect from session so that the updates on updatedUserFeed are not directly saved in db
        em.detach(updatedUserFeed);
        updatedUserFeed
            .body(UPDATED_BODY)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        UserFeedDTO userFeedDTO = userFeedMapper.toDto(updatedUserFeed);

        restUserFeedMockMvc.perform(put("/api/user-feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFeedDTO)))
            .andExpect(status().isOk());

        // Validate the UserFeed in the database
        List<UserFeed> userFeedList = userFeedRepository.findAll();
        assertThat(userFeedList).hasSize(databaseSizeBeforeUpdate);
        UserFeed testUserFeed = userFeedList.get(userFeedList.size() - 1);
        assertThat(testUserFeed.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testUserFeed.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUserFeed.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the UserFeed in Elasticsearch
        verify(mockUserFeedSearchRepository, times(1)).save(testUserFeed);
    }

    @Test
    @Transactional
    public void updateNonExistingUserFeed() throws Exception {
        int databaseSizeBeforeUpdate = userFeedRepository.findAll().size();

        // Create the UserFeed
        UserFeedDTO userFeedDTO = userFeedMapper.toDto(userFeed);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserFeedMockMvc.perform(put("/api/user-feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFeedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserFeed in the database
        List<UserFeed> userFeedList = userFeedRepository.findAll();
        assertThat(userFeedList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserFeed in Elasticsearch
        verify(mockUserFeedSearchRepository, times(0)).save(userFeed);
    }

    @Test
    @Transactional
    public void deleteUserFeed() throws Exception {
        // Initialize the database
        userFeedRepository.saveAndFlush(userFeed);

        int databaseSizeBeforeDelete = userFeedRepository.findAll().size();

        // Get the userFeed
        restUserFeedMockMvc.perform(delete("/api/user-feeds/{id}", userFeed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFeed> userFeedList = userFeedRepository.findAll();
        assertThat(userFeedList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserFeed in Elasticsearch
        verify(mockUserFeedSearchRepository, times(1)).deleteById(userFeed.getId());
    }

    @Test
    @Transactional
    public void searchUserFeed() throws Exception {
        // Initialize the database
        userFeedRepository.saveAndFlush(userFeed);
        when(mockUserFeedSearchRepository.search(queryStringQuery("id:" + userFeed.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userFeed), PageRequest.of(0, 1), 1));
        // Search the userFeed
        restUserFeedMockMvc.perform(get("/api/_search/user-feeds?query=id:" + userFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFeed.class);
        UserFeed userFeed1 = new UserFeed();
        userFeed1.setId(1L);
        UserFeed userFeed2 = new UserFeed();
        userFeed2.setId(userFeed1.getId());
        assertThat(userFeed1).isEqualTo(userFeed2);
        userFeed2.setId(2L);
        assertThat(userFeed1).isNotEqualTo(userFeed2);
        userFeed1.setId(null);
        assertThat(userFeed1).isNotEqualTo(userFeed2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFeedDTO.class);
        UserFeedDTO userFeedDTO1 = new UserFeedDTO();
        userFeedDTO1.setId(1L);
        UserFeedDTO userFeedDTO2 = new UserFeedDTO();
        assertThat(userFeedDTO1).isNotEqualTo(userFeedDTO2);
        userFeedDTO2.setId(userFeedDTO1.getId());
        assertThat(userFeedDTO1).isEqualTo(userFeedDTO2);
        userFeedDTO2.setId(2L);
        assertThat(userFeedDTO1).isNotEqualTo(userFeedDTO2);
        userFeedDTO1.setId(null);
        assertThat(userFeedDTO1).isNotEqualTo(userFeedDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userFeedMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userFeedMapper.fromId(null)).isNull();
    }
}
