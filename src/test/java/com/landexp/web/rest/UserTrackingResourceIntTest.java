package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.UserTracking;
import com.landexp.domain.User;
import com.landexp.repository.UserTrackingRepository;
import com.landexp.repository.search.UserTrackingSearchRepository;
import com.landexp.service.UserTrackingService;
import com.landexp.service.dto.UserTrackingDTO;
import com.landexp.service.mapper.UserTrackingMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.UserTrackingCriteria;
import com.landexp.service.UserTrackingQueryService;

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

import com.landexp.domain.enumeration.UserActivityType;
/**
 * Test class for the UserTrackingResource REST controller.
 *
 * @see UserTrackingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class UserTrackingResourceIntTest {

    private static final UserActivityType DEFAULT_ACTIVITY_TYPE = UserActivityType.USER_SEARCH_BUY;
    private static final UserActivityType UPDATED_ACTIVITY_TYPE = UserActivityType.USER_SEARCH_RENT;

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserTrackingRepository userTrackingRepository;


    @Autowired
    private UserTrackingMapper userTrackingMapper;
    

    @Autowired
    private UserTrackingService userTrackingService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.UserTrackingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserTrackingSearchRepository mockUserTrackingSearchRepository;

    @Autowired
    private UserTrackingQueryService userTrackingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserTrackingMockMvc;

    private UserTracking userTracking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserTrackingResource userTrackingResource = new UserTrackingResource(userTrackingService, userTrackingQueryService);
        this.restUserTrackingMockMvc = MockMvcBuilders.standaloneSetup(userTrackingResource)
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
    public static UserTracking createEntity(EntityManager em) {
        UserTracking userTracking = new UserTracking()
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .sourceId(DEFAULT_SOURCE_ID)
            .sourceLink(DEFAULT_SOURCE_LINK)
            .description(DEFAULT_DESCRIPTION)
            .createAt(DEFAULT_CREATE_AT);
        return userTracking;
    }

    @Before
    public void initTest() {
        userTracking = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserTracking() throws Exception {
        int databaseSizeBeforeCreate = userTrackingRepository.findAll().size();

        // Create the UserTracking
        UserTrackingDTO userTrackingDTO = userTrackingMapper.toDto(userTracking);
        restUserTrackingMockMvc.perform(post("/api/user-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the UserTracking in the database
        List<UserTracking> userTrackingList = userTrackingRepository.findAll();
        assertThat(userTrackingList).hasSize(databaseSizeBeforeCreate + 1);
        UserTracking testUserTracking = userTrackingList.get(userTrackingList.size() - 1);
        assertThat(testUserTracking.getActivityType()).isEqualTo(DEFAULT_ACTIVITY_TYPE);
        assertThat(testUserTracking.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testUserTracking.getSourceLink()).isEqualTo(DEFAULT_SOURCE_LINK);
        assertThat(testUserTracking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserTracking.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);

        // Validate the UserTracking in Elasticsearch
        verify(mockUserTrackingSearchRepository, times(1)).save(testUserTracking);
    }

    @Test
    @Transactional
    public void createUserTrackingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userTrackingRepository.findAll().size();

        // Create the UserTracking with an existing ID
        userTracking.setId(1L);
        UserTrackingDTO userTrackingDTO = userTrackingMapper.toDto(userTracking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTrackingMockMvc.perform(post("/api/user-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserTracking in the database
        List<UserTracking> userTrackingList = userTrackingRepository.findAll();
        assertThat(userTrackingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserTracking in Elasticsearch
        verify(mockUserTrackingSearchRepository, times(0)).save(userTracking);
    }

    @Test
    @Transactional
    public void getAllUserTrackings() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList
        restUserTrackingMockMvc.perform(get("/api/user-trackings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getUserTracking() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get the userTracking
        restUserTrackingMockMvc.perform(get("/api/user-trackings/{id}", userTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userTracking.getId().intValue()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.sourceLink").value(DEFAULT_SOURCE_LINK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByActivityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where activityType equals to DEFAULT_ACTIVITY_TYPE
        defaultUserTrackingShouldBeFound("activityType.equals=" + DEFAULT_ACTIVITY_TYPE);

        // Get all the userTrackingList where activityType equals to UPDATED_ACTIVITY_TYPE
        defaultUserTrackingShouldNotBeFound("activityType.equals=" + UPDATED_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByActivityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where activityType in DEFAULT_ACTIVITY_TYPE or UPDATED_ACTIVITY_TYPE
        defaultUserTrackingShouldBeFound("activityType.in=" + DEFAULT_ACTIVITY_TYPE + "," + UPDATED_ACTIVITY_TYPE);

        // Get all the userTrackingList where activityType equals to UPDATED_ACTIVITY_TYPE
        defaultUserTrackingShouldNotBeFound("activityType.in=" + UPDATED_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByActivityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where activityType is not null
        defaultUserTrackingShouldBeFound("activityType.specified=true");

        // Get all the userTrackingList where activityType is null
        defaultUserTrackingShouldNotBeFound("activityType.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceId equals to DEFAULT_SOURCE_ID
        defaultUserTrackingShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the userTrackingList where sourceId equals to UPDATED_SOURCE_ID
        defaultUserTrackingShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultUserTrackingShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the userTrackingList where sourceId equals to UPDATED_SOURCE_ID
        defaultUserTrackingShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceId is not null
        defaultUserTrackingShouldBeFound("sourceId.specified=true");

        // Get all the userTrackingList where sourceId is null
        defaultUserTrackingShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceLink equals to DEFAULT_SOURCE_LINK
        defaultUserTrackingShouldBeFound("sourceLink.equals=" + DEFAULT_SOURCE_LINK);

        // Get all the userTrackingList where sourceLink equals to UPDATED_SOURCE_LINK
        defaultUserTrackingShouldNotBeFound("sourceLink.equals=" + UPDATED_SOURCE_LINK);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceLinkIsInShouldWork() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceLink in DEFAULT_SOURCE_LINK or UPDATED_SOURCE_LINK
        defaultUserTrackingShouldBeFound("sourceLink.in=" + DEFAULT_SOURCE_LINK + "," + UPDATED_SOURCE_LINK);

        // Get all the userTrackingList where sourceLink equals to UPDATED_SOURCE_LINK
        defaultUserTrackingShouldNotBeFound("sourceLink.in=" + UPDATED_SOURCE_LINK);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsBySourceLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where sourceLink is not null
        defaultUserTrackingShouldBeFound("sourceLink.specified=true");

        // Get all the userTrackingList where sourceLink is null
        defaultUserTrackingShouldNotBeFound("sourceLink.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where description equals to DEFAULT_DESCRIPTION
        defaultUserTrackingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userTrackingList where description equals to UPDATED_DESCRIPTION
        defaultUserTrackingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserTrackingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userTrackingList where description equals to UPDATED_DESCRIPTION
        defaultUserTrackingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where description is not null
        defaultUserTrackingShouldBeFound("description.specified=true");

        // Get all the userTrackingList where description is null
        defaultUserTrackingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where createAt equals to DEFAULT_CREATE_AT
        defaultUserTrackingShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the userTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultUserTrackingShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUserTrackingShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the userTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultUserTrackingShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where createAt is not null
        defaultUserTrackingShouldBeFound("createAt.specified=true");

        // Get all the userTrackingList where createAt is null
        defaultUserTrackingShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultUserTrackingShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the userTrackingList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultUserTrackingShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserTrackingsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        // Get all the userTrackingList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultUserTrackingShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the userTrackingList where createAt less than or equals to UPDATED_CREATE_AT
        defaultUserTrackingShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllUserTrackingsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userTracking.setUser(user);
        userTrackingRepository.saveAndFlush(userTracking);
        Long userId = user.getId();

        // Get all the userTrackingList where user equals to userId
        defaultUserTrackingShouldBeFound("userId.equals=" + userId);

        // Get all the userTrackingList where user equals to userId + 1
        defaultUserTrackingShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserTrackingShouldBeFound(String filter) throws Exception {
        restUserTrackingMockMvc.perform(get("/api/user-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserTrackingShouldNotBeFound(String filter) throws Exception {
        restUserTrackingMockMvc.perform(get("/api/user-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingUserTracking() throws Exception {
        // Get the userTracking
        restUserTrackingMockMvc.perform(get("/api/user-trackings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserTracking() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        int databaseSizeBeforeUpdate = userTrackingRepository.findAll().size();

        // Update the userTracking
        UserTracking updatedUserTracking = userTrackingRepository.findById(userTracking.getId()).get();
        // Disconnect from session so that the updates on updatedUserTracking are not directly saved in db
        em.detach(updatedUserTracking);
        updatedUserTracking
            .activityType(UPDATED_ACTIVITY_TYPE)
            .sourceId(UPDATED_SOURCE_ID)
            .sourceLink(UPDATED_SOURCE_LINK)
            .description(UPDATED_DESCRIPTION)
            .createAt(UPDATED_CREATE_AT);
        UserTrackingDTO userTrackingDTO = userTrackingMapper.toDto(updatedUserTracking);

        restUserTrackingMockMvc.perform(put("/api/user-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userTrackingDTO)))
            .andExpect(status().isOk());

        // Validate the UserTracking in the database
        List<UserTracking> userTrackingList = userTrackingRepository.findAll();
        assertThat(userTrackingList).hasSize(databaseSizeBeforeUpdate);
        UserTracking testUserTracking = userTrackingList.get(userTrackingList.size() - 1);
        assertThat(testUserTracking.getActivityType()).isEqualTo(UPDATED_ACTIVITY_TYPE);
        assertThat(testUserTracking.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testUserTracking.getSourceLink()).isEqualTo(UPDATED_SOURCE_LINK);
        assertThat(testUserTracking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserTracking.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);

        // Validate the UserTracking in Elasticsearch
        verify(mockUserTrackingSearchRepository, times(1)).save(testUserTracking);
    }

    @Test
    @Transactional
    public void updateNonExistingUserTracking() throws Exception {
        int databaseSizeBeforeUpdate = userTrackingRepository.findAll().size();

        // Create the UserTracking
        UserTrackingDTO userTrackingDTO = userTrackingMapper.toDto(userTracking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserTrackingMockMvc.perform(put("/api/user-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserTracking in the database
        List<UserTracking> userTrackingList = userTrackingRepository.findAll();
        assertThat(userTrackingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserTracking in Elasticsearch
        verify(mockUserTrackingSearchRepository, times(0)).save(userTracking);
    }

    @Test
    @Transactional
    public void deleteUserTracking() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);

        int databaseSizeBeforeDelete = userTrackingRepository.findAll().size();

        // Get the userTracking
        restUserTrackingMockMvc.perform(delete("/api/user-trackings/{id}", userTracking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserTracking> userTrackingList = userTrackingRepository.findAll();
        assertThat(userTrackingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserTracking in Elasticsearch
        verify(mockUserTrackingSearchRepository, times(1)).deleteById(userTracking.getId());
    }

    @Test
    @Transactional
    public void searchUserTracking() throws Exception {
        // Initialize the database
        userTrackingRepository.saveAndFlush(userTracking);
        when(mockUserTrackingSearchRepository.search(queryStringQuery("id:" + userTracking.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userTracking), PageRequest.of(0, 1), 1));
        // Search the userTracking
        restUserTrackingMockMvc.perform(get("/api/_search/user-trackings?query=id:" + userTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTracking.class);
        UserTracking userTracking1 = new UserTracking();
        userTracking1.setId(1L);
        UserTracking userTracking2 = new UserTracking();
        userTracking2.setId(userTracking1.getId());
        assertThat(userTracking1).isEqualTo(userTracking2);
        userTracking2.setId(2L);
        assertThat(userTracking1).isNotEqualTo(userTracking2);
        userTracking1.setId(null);
        assertThat(userTracking1).isNotEqualTo(userTracking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTrackingDTO.class);
        UserTrackingDTO userTrackingDTO1 = new UserTrackingDTO();
        userTrackingDTO1.setId(1L);
        UserTrackingDTO userTrackingDTO2 = new UserTrackingDTO();
        assertThat(userTrackingDTO1).isNotEqualTo(userTrackingDTO2);
        userTrackingDTO2.setId(userTrackingDTO1.getId());
        assertThat(userTrackingDTO1).isEqualTo(userTrackingDTO2);
        userTrackingDTO2.setId(2L);
        assertThat(userTrackingDTO1).isNotEqualTo(userTrackingDTO2);
        userTrackingDTO1.setId(null);
        assertThat(userTrackingDTO1).isNotEqualTo(userTrackingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userTrackingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userTrackingMapper.fromId(null)).isNull();
    }
}
