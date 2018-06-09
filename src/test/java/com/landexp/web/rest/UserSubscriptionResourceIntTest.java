package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.UserSubscription;
import com.landexp.domain.User;
import com.landexp.domain.City;
import com.landexp.domain.District;
import com.landexp.domain.Street;
import com.landexp.repository.UserSubscriptionRepository;
import com.landexp.repository.search.UserSubscriptionSearchRepository;
import com.landexp.service.UserSubscriptionService;
import com.landexp.service.dto.UserSubscriptionDTO;
import com.landexp.service.mapper.UserSubscriptionMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.UserSubscriptionCriteria;
import com.landexp.service.UserSubscriptionQueryService;

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
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.LandType;
/**
 * Test class for the UserSubscriptionResource REST controller.
 *
 * @see UserSubscriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class UserSubscriptionResourceIntTest {

    private static final UserActivityType DEFAULT_ACTION_TYPE = UserActivityType.USER_SEARCH_BUY;
    private static final UserActivityType UPDATED_ACTION_TYPE = UserActivityType.USER_SEARCH_RENT;

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final Float DEFAULT_COST_FROM = 1F;
    private static final Float UPDATED_COST_FROM = 2F;

    private static final Float DEFAULT_COST_TO = 1F;
    private static final Float UPDATED_COST_TO = 2F;

    private static final Float DEFAULT_ACREAGE_FROM = 1F;
    private static final Float UPDATED_ACREAGE_FROM = 2F;

    private static final Float DEFAULT_ACREAGE_TO = 1F;
    private static final Float UPDATED_ACREAGE_TO = 2F;

    private static final DirectionType DEFAULT_DIRECTION = DirectionType.NORTH;
    private static final DirectionType UPDATED_DIRECTION = DirectionType.SOUTH;

    private static final String DEFAULT_FLOOR = "AAAAAAAAAA";
    private static final String UPDATED_FLOOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_BATH_ROOM = 1;
    private static final Integer UPDATED_BATH_ROOM = 2;

    private static final Integer DEFAULT_BED_ROOM = 1;
    private static final Integer UPDATED_BED_ROOM = 2;

    private static final Boolean DEFAULT_PARKING = false;
    private static final Boolean UPDATED_PARKING = true;

    private static final LandType DEFAULT_LAND_TYPE = LandType.APARTMENT;
    private static final LandType UPDATED_LAND_TYPE = LandType.PEN_HOUSE;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;


    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;
    

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.UserSubscriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserSubscriptionSearchRepository mockUserSubscriptionSearchRepository;

    @Autowired
    private UserSubscriptionQueryService userSubscriptionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserSubscriptionMockMvc;

    private UserSubscription userSubscription;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserSubscriptionResource userSubscriptionResource = new UserSubscriptionResource(userSubscriptionService, userSubscriptionQueryService);
        this.restUserSubscriptionMockMvc = MockMvcBuilders.standaloneSetup(userSubscriptionResource)
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
    public static UserSubscription createEntity(EntityManager em) {
        UserSubscription userSubscription = new UserSubscription()
            .actionType(DEFAULT_ACTION_TYPE)
            .keyword(DEFAULT_KEYWORD)
            .costFrom(DEFAULT_COST_FROM)
            .costTo(DEFAULT_COST_TO)
            .acreageFrom(DEFAULT_ACREAGE_FROM)
            .acreageTo(DEFAULT_ACREAGE_TO)
            .direction(DEFAULT_DIRECTION)
            .floor(DEFAULT_FLOOR)
            .bathRoom(DEFAULT_BATH_ROOM)
            .bedRoom(DEFAULT_BED_ROOM)
            .parking(DEFAULT_PARKING)
            .landType(DEFAULT_LAND_TYPE)
            .enabled(DEFAULT_ENABLED)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return userSubscription;
    }

    @Before
    public void initTest() {
        userSubscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserSubscription() throws Exception {
        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);
        restUserSubscriptionMockMvc.perform(post("/api/user-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testUserSubscription.getKeyword()).isEqualTo(DEFAULT_KEYWORD);
        assertThat(testUserSubscription.getCostFrom()).isEqualTo(DEFAULT_COST_FROM);
        assertThat(testUserSubscription.getCostTo()).isEqualTo(DEFAULT_COST_TO);
        assertThat(testUserSubscription.getAcreageFrom()).isEqualTo(DEFAULT_ACREAGE_FROM);
        assertThat(testUserSubscription.getAcreageTo()).isEqualTo(DEFAULT_ACREAGE_TO);
        assertThat(testUserSubscription.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testUserSubscription.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testUserSubscription.getBathRoom()).isEqualTo(DEFAULT_BATH_ROOM);
        assertThat(testUserSubscription.getBedRoom()).isEqualTo(DEFAULT_BED_ROOM);
        assertThat(testUserSubscription.isParking()).isEqualTo(DEFAULT_PARKING);
        assertThat(testUserSubscription.getLandType()).isEqualTo(DEFAULT_LAND_TYPE);
        assertThat(testUserSubscription.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testUserSubscription.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUserSubscription.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the UserSubscription in Elasticsearch
        verify(mockUserSubscriptionSearchRepository, times(1)).save(testUserSubscription);
    }

    @Test
    @Transactional
    public void createUserSubscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();

        // Create the UserSubscription with an existing ID
        userSubscription.setId(1L);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSubscriptionMockMvc.perform(post("/api/user-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserSubscription in Elasticsearch
        verify(mockUserSubscriptionSearchRepository, times(0)).save(userSubscription);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptions() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList
        restUserSubscriptionMockMvc.perform(get("/api/user-subscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].costFrom").value(hasItem(DEFAULT_COST_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].costTo").value(hasItem(DEFAULT_COST_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageFrom").value(hasItem(DEFAULT_ACREAGE_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageTo").value(hasItem(DEFAULT_ACREAGE_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get the userSubscription
        restUserSubscriptionMockMvc.perform(get("/api/user-subscriptions/{id}", userSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userSubscription.getId().intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD.toString()))
            .andExpect(jsonPath("$.costFrom").value(DEFAULT_COST_FROM.doubleValue()))
            .andExpect(jsonPath("$.costTo").value(DEFAULT_COST_TO.doubleValue()))
            .andExpect(jsonPath("$.acreageFrom").value(DEFAULT_ACREAGE_FROM.doubleValue()))
            .andExpect(jsonPath("$.acreageTo").value(DEFAULT_ACREAGE_TO.doubleValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR.toString()))
            .andExpect(jsonPath("$.bathRoom").value(DEFAULT_BATH_ROOM))
            .andExpect(jsonPath("$.bedRoom").value(DEFAULT_BED_ROOM))
            .andExpect(jsonPath("$.parking").value(DEFAULT_PARKING.booleanValue()))
            .andExpect(jsonPath("$.landType").value(DEFAULT_LAND_TYPE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByActionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where actionType equals to DEFAULT_ACTION_TYPE
        defaultUserSubscriptionShouldBeFound("actionType.equals=" + DEFAULT_ACTION_TYPE);

        // Get all the userSubscriptionList where actionType equals to UPDATED_ACTION_TYPE
        defaultUserSubscriptionShouldNotBeFound("actionType.equals=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByActionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where actionType in DEFAULT_ACTION_TYPE or UPDATED_ACTION_TYPE
        defaultUserSubscriptionShouldBeFound("actionType.in=" + DEFAULT_ACTION_TYPE + "," + UPDATED_ACTION_TYPE);

        // Get all the userSubscriptionList where actionType equals to UPDATED_ACTION_TYPE
        defaultUserSubscriptionShouldNotBeFound("actionType.in=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByActionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where actionType is not null
        defaultUserSubscriptionShouldBeFound("actionType.specified=true");

        // Get all the userSubscriptionList where actionType is null
        defaultUserSubscriptionShouldNotBeFound("actionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where keyword equals to DEFAULT_KEYWORD
        defaultUserSubscriptionShouldBeFound("keyword.equals=" + DEFAULT_KEYWORD);

        // Get all the userSubscriptionList where keyword equals to UPDATED_KEYWORD
        defaultUserSubscriptionShouldNotBeFound("keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where keyword in DEFAULT_KEYWORD or UPDATED_KEYWORD
        defaultUserSubscriptionShouldBeFound("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD);

        // Get all the userSubscriptionList where keyword equals to UPDATED_KEYWORD
        defaultUserSubscriptionShouldNotBeFound("keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where keyword is not null
        defaultUserSubscriptionShouldBeFound("keyword.specified=true");

        // Get all the userSubscriptionList where keyword is null
        defaultUserSubscriptionShouldNotBeFound("keyword.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostFromIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costFrom equals to DEFAULT_COST_FROM
        defaultUserSubscriptionShouldBeFound("costFrom.equals=" + DEFAULT_COST_FROM);

        // Get all the userSubscriptionList where costFrom equals to UPDATED_COST_FROM
        defaultUserSubscriptionShouldNotBeFound("costFrom.equals=" + UPDATED_COST_FROM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostFromIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costFrom in DEFAULT_COST_FROM or UPDATED_COST_FROM
        defaultUserSubscriptionShouldBeFound("costFrom.in=" + DEFAULT_COST_FROM + "," + UPDATED_COST_FROM);

        // Get all the userSubscriptionList where costFrom equals to UPDATED_COST_FROM
        defaultUserSubscriptionShouldNotBeFound("costFrom.in=" + UPDATED_COST_FROM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costFrom is not null
        defaultUserSubscriptionShouldBeFound("costFrom.specified=true");

        // Get all the userSubscriptionList where costFrom is null
        defaultUserSubscriptionShouldNotBeFound("costFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostToIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costTo equals to DEFAULT_COST_TO
        defaultUserSubscriptionShouldBeFound("costTo.equals=" + DEFAULT_COST_TO);

        // Get all the userSubscriptionList where costTo equals to UPDATED_COST_TO
        defaultUserSubscriptionShouldNotBeFound("costTo.equals=" + UPDATED_COST_TO);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostToIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costTo in DEFAULT_COST_TO or UPDATED_COST_TO
        defaultUserSubscriptionShouldBeFound("costTo.in=" + DEFAULT_COST_TO + "," + UPDATED_COST_TO);

        // Get all the userSubscriptionList where costTo equals to UPDATED_COST_TO
        defaultUserSubscriptionShouldNotBeFound("costTo.in=" + UPDATED_COST_TO);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCostToIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where costTo is not null
        defaultUserSubscriptionShouldBeFound("costTo.specified=true");

        // Get all the userSubscriptionList where costTo is null
        defaultUserSubscriptionShouldNotBeFound("costTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageFromIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageFrom equals to DEFAULT_ACREAGE_FROM
        defaultUserSubscriptionShouldBeFound("acreageFrom.equals=" + DEFAULT_ACREAGE_FROM);

        // Get all the userSubscriptionList where acreageFrom equals to UPDATED_ACREAGE_FROM
        defaultUserSubscriptionShouldNotBeFound("acreageFrom.equals=" + UPDATED_ACREAGE_FROM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageFromIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageFrom in DEFAULT_ACREAGE_FROM or UPDATED_ACREAGE_FROM
        defaultUserSubscriptionShouldBeFound("acreageFrom.in=" + DEFAULT_ACREAGE_FROM + "," + UPDATED_ACREAGE_FROM);

        // Get all the userSubscriptionList where acreageFrom equals to UPDATED_ACREAGE_FROM
        defaultUserSubscriptionShouldNotBeFound("acreageFrom.in=" + UPDATED_ACREAGE_FROM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageFrom is not null
        defaultUserSubscriptionShouldBeFound("acreageFrom.specified=true");

        // Get all the userSubscriptionList where acreageFrom is null
        defaultUserSubscriptionShouldNotBeFound("acreageFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageToIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageTo equals to DEFAULT_ACREAGE_TO
        defaultUserSubscriptionShouldBeFound("acreageTo.equals=" + DEFAULT_ACREAGE_TO);

        // Get all the userSubscriptionList where acreageTo equals to UPDATED_ACREAGE_TO
        defaultUserSubscriptionShouldNotBeFound("acreageTo.equals=" + UPDATED_ACREAGE_TO);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageToIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageTo in DEFAULT_ACREAGE_TO or UPDATED_ACREAGE_TO
        defaultUserSubscriptionShouldBeFound("acreageTo.in=" + DEFAULT_ACREAGE_TO + "," + UPDATED_ACREAGE_TO);

        // Get all the userSubscriptionList where acreageTo equals to UPDATED_ACREAGE_TO
        defaultUserSubscriptionShouldNotBeFound("acreageTo.in=" + UPDATED_ACREAGE_TO);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByAcreageToIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where acreageTo is not null
        defaultUserSubscriptionShouldBeFound("acreageTo.specified=true");

        // Get all the userSubscriptionList where acreageTo is null
        defaultUserSubscriptionShouldNotBeFound("acreageTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where direction equals to DEFAULT_DIRECTION
        defaultUserSubscriptionShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the userSubscriptionList where direction equals to UPDATED_DIRECTION
        defaultUserSubscriptionShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultUserSubscriptionShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the userSubscriptionList where direction equals to UPDATED_DIRECTION
        defaultUserSubscriptionShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where direction is not null
        defaultUserSubscriptionShouldBeFound("direction.specified=true");

        // Get all the userSubscriptionList where direction is null
        defaultUserSubscriptionShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where floor equals to DEFAULT_FLOOR
        defaultUserSubscriptionShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the userSubscriptionList where floor equals to UPDATED_FLOOR
        defaultUserSubscriptionShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultUserSubscriptionShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the userSubscriptionList where floor equals to UPDATED_FLOOR
        defaultUserSubscriptionShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where floor is not null
        defaultUserSubscriptionShouldBeFound("floor.specified=true");

        // Get all the userSubscriptionList where floor is null
        defaultUserSubscriptionShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBathRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bathRoom equals to DEFAULT_BATH_ROOM
        defaultUserSubscriptionShouldBeFound("bathRoom.equals=" + DEFAULT_BATH_ROOM);

        // Get all the userSubscriptionList where bathRoom equals to UPDATED_BATH_ROOM
        defaultUserSubscriptionShouldNotBeFound("bathRoom.equals=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBathRoomIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bathRoom in DEFAULT_BATH_ROOM or UPDATED_BATH_ROOM
        defaultUserSubscriptionShouldBeFound("bathRoom.in=" + DEFAULT_BATH_ROOM + "," + UPDATED_BATH_ROOM);

        // Get all the userSubscriptionList where bathRoom equals to UPDATED_BATH_ROOM
        defaultUserSubscriptionShouldNotBeFound("bathRoom.in=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBathRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bathRoom is not null
        defaultUserSubscriptionShouldBeFound("bathRoom.specified=true");

        // Get all the userSubscriptionList where bathRoom is null
        defaultUserSubscriptionShouldNotBeFound("bathRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBathRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bathRoom greater than or equals to DEFAULT_BATH_ROOM
        defaultUserSubscriptionShouldBeFound("bathRoom.greaterOrEqualThan=" + DEFAULT_BATH_ROOM);

        // Get all the userSubscriptionList where bathRoom greater than or equals to UPDATED_BATH_ROOM
        defaultUserSubscriptionShouldNotBeFound("bathRoom.greaterOrEqualThan=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBathRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bathRoom less than or equals to DEFAULT_BATH_ROOM
        defaultUserSubscriptionShouldNotBeFound("bathRoom.lessThan=" + DEFAULT_BATH_ROOM);

        // Get all the userSubscriptionList where bathRoom less than or equals to UPDATED_BATH_ROOM
        defaultUserSubscriptionShouldBeFound("bathRoom.lessThan=" + UPDATED_BATH_ROOM);
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByBedRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bedRoom equals to DEFAULT_BED_ROOM
        defaultUserSubscriptionShouldBeFound("bedRoom.equals=" + DEFAULT_BED_ROOM);

        // Get all the userSubscriptionList where bedRoom equals to UPDATED_BED_ROOM
        defaultUserSubscriptionShouldNotBeFound("bedRoom.equals=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBedRoomIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bedRoom in DEFAULT_BED_ROOM or UPDATED_BED_ROOM
        defaultUserSubscriptionShouldBeFound("bedRoom.in=" + DEFAULT_BED_ROOM + "," + UPDATED_BED_ROOM);

        // Get all the userSubscriptionList where bedRoom equals to UPDATED_BED_ROOM
        defaultUserSubscriptionShouldNotBeFound("bedRoom.in=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBedRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bedRoom is not null
        defaultUserSubscriptionShouldBeFound("bedRoom.specified=true");

        // Get all the userSubscriptionList where bedRoom is null
        defaultUserSubscriptionShouldNotBeFound("bedRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBedRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bedRoom greater than or equals to DEFAULT_BED_ROOM
        defaultUserSubscriptionShouldBeFound("bedRoom.greaterOrEqualThan=" + DEFAULT_BED_ROOM);

        // Get all the userSubscriptionList where bedRoom greater than or equals to UPDATED_BED_ROOM
        defaultUserSubscriptionShouldNotBeFound("bedRoom.greaterOrEqualThan=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByBedRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where bedRoom less than or equals to DEFAULT_BED_ROOM
        defaultUserSubscriptionShouldNotBeFound("bedRoom.lessThan=" + DEFAULT_BED_ROOM);

        // Get all the userSubscriptionList where bedRoom less than or equals to UPDATED_BED_ROOM
        defaultUserSubscriptionShouldBeFound("bedRoom.lessThan=" + UPDATED_BED_ROOM);
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByParkingIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where parking equals to DEFAULT_PARKING
        defaultUserSubscriptionShouldBeFound("parking.equals=" + DEFAULT_PARKING);

        // Get all the userSubscriptionList where parking equals to UPDATED_PARKING
        defaultUserSubscriptionShouldNotBeFound("parking.equals=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByParkingIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where parking in DEFAULT_PARKING or UPDATED_PARKING
        defaultUserSubscriptionShouldBeFound("parking.in=" + DEFAULT_PARKING + "," + UPDATED_PARKING);

        // Get all the userSubscriptionList where parking equals to UPDATED_PARKING
        defaultUserSubscriptionShouldNotBeFound("parking.in=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByParkingIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where parking is not null
        defaultUserSubscriptionShouldBeFound("parking.specified=true");

        // Get all the userSubscriptionList where parking is null
        defaultUserSubscriptionShouldNotBeFound("parking.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByLandTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where landType equals to DEFAULT_LAND_TYPE
        defaultUserSubscriptionShouldBeFound("landType.equals=" + DEFAULT_LAND_TYPE);

        // Get all the userSubscriptionList where landType equals to UPDATED_LAND_TYPE
        defaultUserSubscriptionShouldNotBeFound("landType.equals=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByLandTypeIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where landType in DEFAULT_LAND_TYPE or UPDATED_LAND_TYPE
        defaultUserSubscriptionShouldBeFound("landType.in=" + DEFAULT_LAND_TYPE + "," + UPDATED_LAND_TYPE);

        // Get all the userSubscriptionList where landType equals to UPDATED_LAND_TYPE
        defaultUserSubscriptionShouldNotBeFound("landType.in=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByLandTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where landType is not null
        defaultUserSubscriptionShouldBeFound("landType.specified=true");

        // Get all the userSubscriptionList where landType is null
        defaultUserSubscriptionShouldNotBeFound("landType.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where enabled equals to DEFAULT_ENABLED
        defaultUserSubscriptionShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the userSubscriptionList where enabled equals to UPDATED_ENABLED
        defaultUserSubscriptionShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultUserSubscriptionShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the userSubscriptionList where enabled equals to UPDATED_ENABLED
        defaultUserSubscriptionShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where enabled is not null
        defaultUserSubscriptionShouldBeFound("enabled.specified=true");

        // Get all the userSubscriptionList where enabled is null
        defaultUserSubscriptionShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where createAt equals to DEFAULT_CREATE_AT
        defaultUserSubscriptionShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the userSubscriptionList where createAt equals to UPDATED_CREATE_AT
        defaultUserSubscriptionShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUserSubscriptionShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the userSubscriptionList where createAt equals to UPDATED_CREATE_AT
        defaultUserSubscriptionShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where createAt is not null
        defaultUserSubscriptionShouldBeFound("createAt.specified=true");

        // Get all the userSubscriptionList where createAt is null
        defaultUserSubscriptionShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultUserSubscriptionShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the userSubscriptionList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultUserSubscriptionShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultUserSubscriptionShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the userSubscriptionList where createAt less than or equals to UPDATED_CREATE_AT
        defaultUserSubscriptionShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where updateAt equals to DEFAULT_UPDATE_AT
        defaultUserSubscriptionShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the userSubscriptionList where updateAt equals to UPDATED_UPDATE_AT
        defaultUserSubscriptionShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultUserSubscriptionShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the userSubscriptionList where updateAt equals to UPDATED_UPDATE_AT
        defaultUserSubscriptionShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where updateAt is not null
        defaultUserSubscriptionShouldBeFound("updateAt.specified=true");

        // Get all the userSubscriptionList where updateAt is null
        defaultUserSubscriptionShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where updateAt greater than or equals to DEFAULT_UPDATE_AT
        defaultUserSubscriptionShouldBeFound("updateAt.greaterOrEqualThan=" + DEFAULT_UPDATE_AT);

        // Get all the userSubscriptionList where updateAt greater than or equals to UPDATED_UPDATE_AT
        defaultUserSubscriptionShouldNotBeFound("updateAt.greaterOrEqualThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserSubscriptionsByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where updateAt less than or equals to DEFAULT_UPDATE_AT
        defaultUserSubscriptionShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the userSubscriptionList where updateAt less than or equals to UPDATED_UPDATE_AT
        defaultUserSubscriptionShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userSubscription.setUser(user);
        userSubscriptionRepository.saveAndFlush(userSubscription);
        Long userId = user.getId();

        // Get all the userSubscriptionList where user equals to userId
        defaultUserSubscriptionShouldBeFound("userId.equals=" + userId);

        // Get all the userSubscriptionList where user equals to userId + 1
        defaultUserSubscriptionShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        userSubscription.setCity(city);
        userSubscriptionRepository.saveAndFlush(userSubscription);
        Long cityId = city.getId();

        // Get all the userSubscriptionList where city equals to cityId
        defaultUserSubscriptionShouldBeFound("cityId.equals=" + cityId);

        // Get all the userSubscriptionList where city equals to cityId + 1
        defaultUserSubscriptionShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        District district = DistrictResourceIntTest.createEntity(em);
        em.persist(district);
        em.flush();
        userSubscription.setDistrict(district);
        userSubscriptionRepository.saveAndFlush(userSubscription);
        Long districtId = district.getId();

        // Get all the userSubscriptionList where district equals to districtId
        defaultUserSubscriptionShouldBeFound("districtId.equals=" + districtId);

        // Get all the userSubscriptionList where district equals to districtId + 1
        defaultUserSubscriptionShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }


    @Test
    @Transactional
    public void getAllUserSubscriptionsByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        Street street = StreetResourceIntTest.createEntity(em);
        em.persist(street);
        em.flush();
        userSubscription.setStreet(street);
        userSubscriptionRepository.saveAndFlush(userSubscription);
        Long streetId = street.getId();

        // Get all the userSubscriptionList where street equals to streetId
        defaultUserSubscriptionShouldBeFound("streetId.equals=" + streetId);

        // Get all the userSubscriptionList where street equals to streetId + 1
        defaultUserSubscriptionShouldNotBeFound("streetId.equals=" + (streetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserSubscriptionShouldBeFound(String filter) throws Exception {
        restUserSubscriptionMockMvc.perform(get("/api/user-subscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].costFrom").value(hasItem(DEFAULT_COST_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].costTo").value(hasItem(DEFAULT_COST_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageFrom").value(hasItem(DEFAULT_ACREAGE_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageTo").value(hasItem(DEFAULT_ACREAGE_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserSubscriptionShouldNotBeFound(String filter) throws Exception {
        restUserSubscriptionMockMvc.perform(get("/api/user-subscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingUserSubscription() throws Exception {
        // Get the userSubscription
        restUserSubscriptionMockMvc.perform(get("/api/user-subscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription
        UserSubscription updatedUserSubscription = userSubscriptionRepository.findById(userSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedUserSubscription are not directly saved in db
        em.detach(updatedUserSubscription);
        updatedUserSubscription
            .actionType(UPDATED_ACTION_TYPE)
            .keyword(UPDATED_KEYWORD)
            .costFrom(UPDATED_COST_FROM)
            .costTo(UPDATED_COST_TO)
            .acreageFrom(UPDATED_ACREAGE_FROM)
            .acreageTo(UPDATED_ACREAGE_TO)
            .direction(UPDATED_DIRECTION)
            .floor(UPDATED_FLOOR)
            .bathRoom(UPDATED_BATH_ROOM)
            .bedRoom(UPDATED_BED_ROOM)
            .parking(UPDATED_PARKING)
            .landType(UPDATED_LAND_TYPE)
            .enabled(UPDATED_ENABLED)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(updatedUserSubscription);

        restUserSubscriptionMockMvc.perform(put("/api/user-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO)))
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testUserSubscription.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testUserSubscription.getCostFrom()).isEqualTo(UPDATED_COST_FROM);
        assertThat(testUserSubscription.getCostTo()).isEqualTo(UPDATED_COST_TO);
        assertThat(testUserSubscription.getAcreageFrom()).isEqualTo(UPDATED_ACREAGE_FROM);
        assertThat(testUserSubscription.getAcreageTo()).isEqualTo(UPDATED_ACREAGE_TO);
        assertThat(testUserSubscription.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testUserSubscription.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testUserSubscription.getBathRoom()).isEqualTo(UPDATED_BATH_ROOM);
        assertThat(testUserSubscription.getBedRoom()).isEqualTo(UPDATED_BED_ROOM);
        assertThat(testUserSubscription.isParking()).isEqualTo(UPDATED_PARKING);
        assertThat(testUserSubscription.getLandType()).isEqualTo(UPDATED_LAND_TYPE);
        assertThat(testUserSubscription.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testUserSubscription.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUserSubscription.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the UserSubscription in Elasticsearch
        verify(mockUserSubscriptionSearchRepository, times(1)).save(testUserSubscription);
    }

    @Test
    @Transactional
    public void updateNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserSubscriptionMockMvc.perform(put("/api/user-subscriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserSubscription in Elasticsearch
        verify(mockUserSubscriptionSearchRepository, times(0)).save(userSubscription);
    }

    @Test
    @Transactional
    public void deleteUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeDelete = userSubscriptionRepository.findAll().size();

        // Get the userSubscription
        restUserSubscriptionMockMvc.perform(delete("/api/user-subscriptions/{id}", userSubscription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserSubscription in Elasticsearch
        verify(mockUserSubscriptionSearchRepository, times(1)).deleteById(userSubscription.getId());
    }

    @Test
    @Transactional
    public void searchUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);
        when(mockUserSubscriptionSearchRepository.search(queryStringQuery("id:" + userSubscription.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userSubscription), PageRequest.of(0, 1), 1));
        // Search the userSubscription
        restUserSubscriptionMockMvc.perform(get("/api/_search/user-subscriptions?query=id:" + userSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].costFrom").value(hasItem(DEFAULT_COST_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].costTo").value(hasItem(DEFAULT_COST_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageFrom").value(hasItem(DEFAULT_ACREAGE_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].acreageTo").value(hasItem(DEFAULT_ACREAGE_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSubscription.class);
        UserSubscription userSubscription1 = new UserSubscription();
        userSubscription1.setId(1L);
        UserSubscription userSubscription2 = new UserSubscription();
        userSubscription2.setId(userSubscription1.getId());
        assertThat(userSubscription1).isEqualTo(userSubscription2);
        userSubscription2.setId(2L);
        assertThat(userSubscription1).isNotEqualTo(userSubscription2);
        userSubscription1.setId(null);
        assertThat(userSubscription1).isNotEqualTo(userSubscription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSubscriptionDTO.class);
        UserSubscriptionDTO userSubscriptionDTO1 = new UserSubscriptionDTO();
        userSubscriptionDTO1.setId(1L);
        UserSubscriptionDTO userSubscriptionDTO2 = new UserSubscriptionDTO();
        assertThat(userSubscriptionDTO1).isNotEqualTo(userSubscriptionDTO2);
        userSubscriptionDTO2.setId(userSubscriptionDTO1.getId());
        assertThat(userSubscriptionDTO1).isEqualTo(userSubscriptionDTO2);
        userSubscriptionDTO2.setId(2L);
        assertThat(userSubscriptionDTO1).isNotEqualTo(userSubscriptionDTO2);
        userSubscriptionDTO1.setId(null);
        assertThat(userSubscriptionDTO1).isNotEqualTo(userSubscriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userSubscriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userSubscriptionMapper.fromId(null)).isNull();
    }
}
