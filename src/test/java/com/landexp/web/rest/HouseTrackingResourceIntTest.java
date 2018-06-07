package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.HouseTracking;
import com.landexp.domain.House;
import com.landexp.domain.User;
import com.landexp.repository.HouseTrackingRepository;
import com.landexp.repository.search.HouseTrackingSearchRepository;
import com.landexp.service.HouseTrackingService;
import com.landexp.service.dto.HouseTrackingDTO;
import com.landexp.service.mapper.HouseTrackingMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.HouseTrackingCriteria;
import com.landexp.service.HouseTrackingQueryService;

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
 * Test class for the HouseTrackingResource REST controller.
 *
 * @see HouseTrackingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class HouseTrackingResourceIntTest {

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
    private HouseTrackingRepository houseTrackingRepository;


    @Autowired
    private HouseTrackingMapper houseTrackingMapper;
    

    @Autowired
    private HouseTrackingService houseTrackingService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.HouseTrackingSearchRepositoryMockConfiguration
     */
    @Autowired
    private HouseTrackingSearchRepository mockHouseTrackingSearchRepository;

    @Autowired
    private HouseTrackingQueryService houseTrackingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHouseTrackingMockMvc;

    private HouseTracking houseTracking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HouseTrackingResource houseTrackingResource = new HouseTrackingResource(houseTrackingService, houseTrackingQueryService);
        this.restHouseTrackingMockMvc = MockMvcBuilders.standaloneSetup(houseTrackingResource)
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
    public static HouseTracking createEntity(EntityManager em) {
        HouseTracking houseTracking = new HouseTracking()
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .sourceId(DEFAULT_SOURCE_ID)
            .sourceLink(DEFAULT_SOURCE_LINK)
            .description(DEFAULT_DESCRIPTION)
            .createAt(DEFAULT_CREATE_AT);
        return houseTracking;
    }

    @Before
    public void initTest() {
        houseTracking = createEntity(em);
    }

    @Test
    @Transactional
    public void createHouseTracking() throws Exception {
        int databaseSizeBeforeCreate = houseTrackingRepository.findAll().size();

        // Create the HouseTracking
        HouseTrackingDTO houseTrackingDTO = houseTrackingMapper.toDto(houseTracking);
        restHouseTrackingMockMvc.perform(post("/api/house-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the HouseTracking in the database
        List<HouseTracking> houseTrackingList = houseTrackingRepository.findAll();
        assertThat(houseTrackingList).hasSize(databaseSizeBeforeCreate + 1);
        HouseTracking testHouseTracking = houseTrackingList.get(houseTrackingList.size() - 1);
        assertThat(testHouseTracking.getActivityType()).isEqualTo(DEFAULT_ACTIVITY_TYPE);
        assertThat(testHouseTracking.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testHouseTracking.getSourceLink()).isEqualTo(DEFAULT_SOURCE_LINK);
        assertThat(testHouseTracking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHouseTracking.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);

        // Validate the HouseTracking in Elasticsearch
        verify(mockHouseTrackingSearchRepository, times(1)).save(testHouseTracking);
    }

    @Test
    @Transactional
    public void createHouseTrackingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = houseTrackingRepository.findAll().size();

        // Create the HouseTracking with an existing ID
        houseTracking.setId(1L);
        HouseTrackingDTO houseTrackingDTO = houseTrackingMapper.toDto(houseTracking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseTrackingMockMvc.perform(post("/api/house-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HouseTracking in the database
        List<HouseTracking> houseTrackingList = houseTrackingRepository.findAll();
        assertThat(houseTrackingList).hasSize(databaseSizeBeforeCreate);

        // Validate the HouseTracking in Elasticsearch
        verify(mockHouseTrackingSearchRepository, times(0)).save(houseTracking);
    }

    @Test
    @Transactional
    public void getAllHouseTrackings() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList
        restHouseTrackingMockMvc.perform(get("/api/house-trackings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getHouseTracking() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get the houseTracking
        restHouseTrackingMockMvc.perform(get("/api/house-trackings/{id}", houseTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(houseTracking.getId().intValue()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.sourceLink").value(DEFAULT_SOURCE_LINK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByActivityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where activityType equals to DEFAULT_ACTIVITY_TYPE
        defaultHouseTrackingShouldBeFound("activityType.equals=" + DEFAULT_ACTIVITY_TYPE);

        // Get all the houseTrackingList where activityType equals to UPDATED_ACTIVITY_TYPE
        defaultHouseTrackingShouldNotBeFound("activityType.equals=" + UPDATED_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByActivityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where activityType in DEFAULT_ACTIVITY_TYPE or UPDATED_ACTIVITY_TYPE
        defaultHouseTrackingShouldBeFound("activityType.in=" + DEFAULT_ACTIVITY_TYPE + "," + UPDATED_ACTIVITY_TYPE);

        // Get all the houseTrackingList where activityType equals to UPDATED_ACTIVITY_TYPE
        defaultHouseTrackingShouldNotBeFound("activityType.in=" + UPDATED_ACTIVITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByActivityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where activityType is not null
        defaultHouseTrackingShouldBeFound("activityType.specified=true");

        // Get all the houseTrackingList where activityType is null
        defaultHouseTrackingShouldNotBeFound("activityType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceId equals to DEFAULT_SOURCE_ID
        defaultHouseTrackingShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the houseTrackingList where sourceId equals to UPDATED_SOURCE_ID
        defaultHouseTrackingShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultHouseTrackingShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the houseTrackingList where sourceId equals to UPDATED_SOURCE_ID
        defaultHouseTrackingShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceId is not null
        defaultHouseTrackingShouldBeFound("sourceId.specified=true");

        // Get all the houseTrackingList where sourceId is null
        defaultHouseTrackingShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceLink equals to DEFAULT_SOURCE_LINK
        defaultHouseTrackingShouldBeFound("sourceLink.equals=" + DEFAULT_SOURCE_LINK);

        // Get all the houseTrackingList where sourceLink equals to UPDATED_SOURCE_LINK
        defaultHouseTrackingShouldNotBeFound("sourceLink.equals=" + UPDATED_SOURCE_LINK);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceLinkIsInShouldWork() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceLink in DEFAULT_SOURCE_LINK or UPDATED_SOURCE_LINK
        defaultHouseTrackingShouldBeFound("sourceLink.in=" + DEFAULT_SOURCE_LINK + "," + UPDATED_SOURCE_LINK);

        // Get all the houseTrackingList where sourceLink equals to UPDATED_SOURCE_LINK
        defaultHouseTrackingShouldNotBeFound("sourceLink.in=" + UPDATED_SOURCE_LINK);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsBySourceLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where sourceLink is not null
        defaultHouseTrackingShouldBeFound("sourceLink.specified=true");

        // Get all the houseTrackingList where sourceLink is null
        defaultHouseTrackingShouldNotBeFound("sourceLink.specified=false");
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where description equals to DEFAULT_DESCRIPTION
        defaultHouseTrackingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the houseTrackingList where description equals to UPDATED_DESCRIPTION
        defaultHouseTrackingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHouseTrackingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the houseTrackingList where description equals to UPDATED_DESCRIPTION
        defaultHouseTrackingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where description is not null
        defaultHouseTrackingShouldBeFound("description.specified=true");

        // Get all the houseTrackingList where description is null
        defaultHouseTrackingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where createAt equals to DEFAULT_CREATE_AT
        defaultHouseTrackingShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the houseTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultHouseTrackingShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultHouseTrackingShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the houseTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultHouseTrackingShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where createAt is not null
        defaultHouseTrackingShouldBeFound("createAt.specified=true");

        // Get all the houseTrackingList where createAt is null
        defaultHouseTrackingShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultHouseTrackingShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the houseTrackingList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultHouseTrackingShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHouseTrackingsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        // Get all the houseTrackingList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultHouseTrackingShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the houseTrackingList where createAt less than or equals to UPDATED_CREATE_AT
        defaultHouseTrackingShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllHouseTrackingsByHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        House house = HouseResourceIntTest.createEntity(em);
        em.persist(house);
        em.flush();
        houseTracking.setHouse(house);
        houseTrackingRepository.saveAndFlush(houseTracking);
        Long houseId = house.getId();

        // Get all the houseTrackingList where house equals to houseId
        defaultHouseTrackingShouldBeFound("houseId.equals=" + houseId);

        // Get all the houseTrackingList where house equals to houseId + 1
        defaultHouseTrackingShouldNotBeFound("houseId.equals=" + (houseId + 1));
    }


    @Test
    @Transactional
    public void getAllHouseTrackingsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        houseTracking.setUser(user);
        houseTrackingRepository.saveAndFlush(houseTracking);
        Long userId = user.getId();

        // Get all the houseTrackingList where user equals to userId
        defaultHouseTrackingShouldBeFound("userId.equals=" + userId);

        // Get all the houseTrackingList where user equals to userId + 1
        defaultHouseTrackingShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHouseTrackingShouldBeFound(String filter) throws Exception {
        restHouseTrackingMockMvc.perform(get("/api/house-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHouseTrackingShouldNotBeFound(String filter) throws Exception {
        restHouseTrackingMockMvc.perform(get("/api/house-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingHouseTracking() throws Exception {
        // Get the houseTracking
        restHouseTrackingMockMvc.perform(get("/api/house-trackings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouseTracking() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        int databaseSizeBeforeUpdate = houseTrackingRepository.findAll().size();

        // Update the houseTracking
        HouseTracking updatedHouseTracking = houseTrackingRepository.findById(houseTracking.getId()).get();
        // Disconnect from session so that the updates on updatedHouseTracking are not directly saved in db
        em.detach(updatedHouseTracking);
        updatedHouseTracking
            .activityType(UPDATED_ACTIVITY_TYPE)
            .sourceId(UPDATED_SOURCE_ID)
            .sourceLink(UPDATED_SOURCE_LINK)
            .description(UPDATED_DESCRIPTION)
            .createAt(UPDATED_CREATE_AT);
        HouseTrackingDTO houseTrackingDTO = houseTrackingMapper.toDto(updatedHouseTracking);

        restHouseTrackingMockMvc.perform(put("/api/house-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseTrackingDTO)))
            .andExpect(status().isOk());

        // Validate the HouseTracking in the database
        List<HouseTracking> houseTrackingList = houseTrackingRepository.findAll();
        assertThat(houseTrackingList).hasSize(databaseSizeBeforeUpdate);
        HouseTracking testHouseTracking = houseTrackingList.get(houseTrackingList.size() - 1);
        assertThat(testHouseTracking.getActivityType()).isEqualTo(UPDATED_ACTIVITY_TYPE);
        assertThat(testHouseTracking.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testHouseTracking.getSourceLink()).isEqualTo(UPDATED_SOURCE_LINK);
        assertThat(testHouseTracking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHouseTracking.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);

        // Validate the HouseTracking in Elasticsearch
        verify(mockHouseTrackingSearchRepository, times(1)).save(testHouseTracking);
    }

    @Test
    @Transactional
    public void updateNonExistingHouseTracking() throws Exception {
        int databaseSizeBeforeUpdate = houseTrackingRepository.findAll().size();

        // Create the HouseTracking
        HouseTrackingDTO houseTrackingDTO = houseTrackingMapper.toDto(houseTracking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHouseTrackingMockMvc.perform(put("/api/house-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HouseTracking in the database
        List<HouseTracking> houseTrackingList = houseTrackingRepository.findAll();
        assertThat(houseTrackingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HouseTracking in Elasticsearch
        verify(mockHouseTrackingSearchRepository, times(0)).save(houseTracking);
    }

    @Test
    @Transactional
    public void deleteHouseTracking() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);

        int databaseSizeBeforeDelete = houseTrackingRepository.findAll().size();

        // Get the houseTracking
        restHouseTrackingMockMvc.perform(delete("/api/house-trackings/{id}", houseTracking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HouseTracking> houseTrackingList = houseTrackingRepository.findAll();
        assertThat(houseTrackingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HouseTracking in Elasticsearch
        verify(mockHouseTrackingSearchRepository, times(1)).deleteById(houseTracking.getId());
    }

    @Test
    @Transactional
    public void searchHouseTracking() throws Exception {
        // Initialize the database
        houseTrackingRepository.saveAndFlush(houseTracking);
        when(mockHouseTrackingSearchRepository.search(queryStringQuery("id:" + houseTracking.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(houseTracking), PageRequest.of(0, 1), 1));
        // Search the houseTracking
        restHouseTrackingMockMvc.perform(get("/api/_search/house-trackings?query=id:" + houseTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].sourceLink").value(hasItem(DEFAULT_SOURCE_LINK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseTracking.class);
        HouseTracking houseTracking1 = new HouseTracking();
        houseTracking1.setId(1L);
        HouseTracking houseTracking2 = new HouseTracking();
        houseTracking2.setId(houseTracking1.getId());
        assertThat(houseTracking1).isEqualTo(houseTracking2);
        houseTracking2.setId(2L);
        assertThat(houseTracking1).isNotEqualTo(houseTracking2);
        houseTracking1.setId(null);
        assertThat(houseTracking1).isNotEqualTo(houseTracking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseTrackingDTO.class);
        HouseTrackingDTO houseTrackingDTO1 = new HouseTrackingDTO();
        houseTrackingDTO1.setId(1L);
        HouseTrackingDTO houseTrackingDTO2 = new HouseTrackingDTO();
        assertThat(houseTrackingDTO1).isNotEqualTo(houseTrackingDTO2);
        houseTrackingDTO2.setId(houseTrackingDTO1.getId());
        assertThat(houseTrackingDTO1).isEqualTo(houseTrackingDTO2);
        houseTrackingDTO2.setId(2L);
        assertThat(houseTrackingDTO1).isNotEqualTo(houseTrackingDTO2);
        houseTrackingDTO1.setId(null);
        assertThat(houseTrackingDTO1).isNotEqualTo(houseTrackingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(houseTrackingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(houseTrackingMapper.fromId(null)).isNull();
    }
}
