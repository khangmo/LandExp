package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.SearchTracking;
import com.landexp.domain.User;
import com.landexp.domain.City;
import com.landexp.domain.Street;
import com.landexp.repository.SearchTrackingRepository;
import com.landexp.repository.search.SearchTrackingSearchRepository;
import com.landexp.service.SearchTrackingService;
import com.landexp.service.dto.SearchTrackingDTO;
import com.landexp.service.mapper.SearchTrackingMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.SearchTrackingCriteria;
import com.landexp.service.SearchTrackingQueryService;

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
 * Test class for the SearchTrackingResource REST controller.
 *
 * @see SearchTrackingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class SearchTrackingResourceIntTest {

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

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SearchTrackingRepository searchTrackingRepository;


    @Autowired
    private SearchTrackingMapper searchTrackingMapper;
    

    @Autowired
    private SearchTrackingService searchTrackingService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.SearchTrackingSearchRepositoryMockConfiguration
     */
    @Autowired
    private SearchTrackingSearchRepository mockSearchTrackingSearchRepository;

    @Autowired
    private SearchTrackingQueryService searchTrackingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchTrackingMockMvc;

    private SearchTracking searchTracking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchTrackingResource searchTrackingResource = new SearchTrackingResource(searchTrackingService, searchTrackingQueryService);
        this.restSearchTrackingMockMvc = MockMvcBuilders.standaloneSetup(searchTrackingResource)
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
    public static SearchTracking createEntity(EntityManager em) {
        SearchTracking searchTracking = new SearchTracking()
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
            .createAt(DEFAULT_CREATE_AT);
        return searchTracking;
    }

    @Before
    public void initTest() {
        searchTracking = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchTracking() throws Exception {
        int databaseSizeBeforeCreate = searchTrackingRepository.findAll().size();

        // Create the SearchTracking
        SearchTrackingDTO searchTrackingDTO = searchTrackingMapper.toDto(searchTracking);
        restSearchTrackingMockMvc.perform(post("/api/search-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchTrackingDTO)))
            .andExpect(status().isCreated());

        // Validate the SearchTracking in the database
        List<SearchTracking> searchTrackingList = searchTrackingRepository.findAll();
        assertThat(searchTrackingList).hasSize(databaseSizeBeforeCreate + 1);
        SearchTracking testSearchTracking = searchTrackingList.get(searchTrackingList.size() - 1);
        assertThat(testSearchTracking.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testSearchTracking.getKeyword()).isEqualTo(DEFAULT_KEYWORD);
        assertThat(testSearchTracking.getCostFrom()).isEqualTo(DEFAULT_COST_FROM);
        assertThat(testSearchTracking.getCostTo()).isEqualTo(DEFAULT_COST_TO);
        assertThat(testSearchTracking.getAcreageFrom()).isEqualTo(DEFAULT_ACREAGE_FROM);
        assertThat(testSearchTracking.getAcreageTo()).isEqualTo(DEFAULT_ACREAGE_TO);
        assertThat(testSearchTracking.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testSearchTracking.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testSearchTracking.getBathRoom()).isEqualTo(DEFAULT_BATH_ROOM);
        assertThat(testSearchTracking.getBedRoom()).isEqualTo(DEFAULT_BED_ROOM);
        assertThat(testSearchTracking.isParking()).isEqualTo(DEFAULT_PARKING);
        assertThat(testSearchTracking.getLandType()).isEqualTo(DEFAULT_LAND_TYPE);
        assertThat(testSearchTracking.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);

        // Validate the SearchTracking in Elasticsearch
        verify(mockSearchTrackingSearchRepository, times(1)).save(testSearchTracking);
    }

    @Test
    @Transactional
    public void createSearchTrackingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchTrackingRepository.findAll().size();

        // Create the SearchTracking with an existing ID
        searchTracking.setId(1L);
        SearchTrackingDTO searchTrackingDTO = searchTrackingMapper.toDto(searchTracking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchTrackingMockMvc.perform(post("/api/search-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchTracking in the database
        List<SearchTracking> searchTrackingList = searchTrackingRepository.findAll();
        assertThat(searchTrackingList).hasSize(databaseSizeBeforeCreate);

        // Validate the SearchTracking in Elasticsearch
        verify(mockSearchTrackingSearchRepository, times(0)).save(searchTracking);
    }

    @Test
    @Transactional
    public void getAllSearchTrackings() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList
        restSearchTrackingMockMvc.perform(get("/api/search-trackings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchTracking.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getSearchTracking() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get the searchTracking
        restSearchTrackingMockMvc.perform(get("/api/search-trackings/{id}", searchTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchTracking.getId().intValue()))
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
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByActionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where actionType equals to DEFAULT_ACTION_TYPE
        defaultSearchTrackingShouldBeFound("actionType.equals=" + DEFAULT_ACTION_TYPE);

        // Get all the searchTrackingList where actionType equals to UPDATED_ACTION_TYPE
        defaultSearchTrackingShouldNotBeFound("actionType.equals=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByActionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where actionType in DEFAULT_ACTION_TYPE or UPDATED_ACTION_TYPE
        defaultSearchTrackingShouldBeFound("actionType.in=" + DEFAULT_ACTION_TYPE + "," + UPDATED_ACTION_TYPE);

        // Get all the searchTrackingList where actionType equals to UPDATED_ACTION_TYPE
        defaultSearchTrackingShouldNotBeFound("actionType.in=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByActionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where actionType is not null
        defaultSearchTrackingShouldBeFound("actionType.specified=true");

        // Get all the searchTrackingList where actionType is null
        defaultSearchTrackingShouldNotBeFound("actionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where keyword equals to DEFAULT_KEYWORD
        defaultSearchTrackingShouldBeFound("keyword.equals=" + DEFAULT_KEYWORD);

        // Get all the searchTrackingList where keyword equals to UPDATED_KEYWORD
        defaultSearchTrackingShouldNotBeFound("keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where keyword in DEFAULT_KEYWORD or UPDATED_KEYWORD
        defaultSearchTrackingShouldBeFound("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD);

        // Get all the searchTrackingList where keyword equals to UPDATED_KEYWORD
        defaultSearchTrackingShouldNotBeFound("keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where keyword is not null
        defaultSearchTrackingShouldBeFound("keyword.specified=true");

        // Get all the searchTrackingList where keyword is null
        defaultSearchTrackingShouldNotBeFound("keyword.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostFromIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costFrom equals to DEFAULT_COST_FROM
        defaultSearchTrackingShouldBeFound("costFrom.equals=" + DEFAULT_COST_FROM);

        // Get all the searchTrackingList where costFrom equals to UPDATED_COST_FROM
        defaultSearchTrackingShouldNotBeFound("costFrom.equals=" + UPDATED_COST_FROM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostFromIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costFrom in DEFAULT_COST_FROM or UPDATED_COST_FROM
        defaultSearchTrackingShouldBeFound("costFrom.in=" + DEFAULT_COST_FROM + "," + UPDATED_COST_FROM);

        // Get all the searchTrackingList where costFrom equals to UPDATED_COST_FROM
        defaultSearchTrackingShouldNotBeFound("costFrom.in=" + UPDATED_COST_FROM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costFrom is not null
        defaultSearchTrackingShouldBeFound("costFrom.specified=true");

        // Get all the searchTrackingList where costFrom is null
        defaultSearchTrackingShouldNotBeFound("costFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostToIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costTo equals to DEFAULT_COST_TO
        defaultSearchTrackingShouldBeFound("costTo.equals=" + DEFAULT_COST_TO);

        // Get all the searchTrackingList where costTo equals to UPDATED_COST_TO
        defaultSearchTrackingShouldNotBeFound("costTo.equals=" + UPDATED_COST_TO);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostToIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costTo in DEFAULT_COST_TO or UPDATED_COST_TO
        defaultSearchTrackingShouldBeFound("costTo.in=" + DEFAULT_COST_TO + "," + UPDATED_COST_TO);

        // Get all the searchTrackingList where costTo equals to UPDATED_COST_TO
        defaultSearchTrackingShouldNotBeFound("costTo.in=" + UPDATED_COST_TO);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCostToIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where costTo is not null
        defaultSearchTrackingShouldBeFound("costTo.specified=true");

        // Get all the searchTrackingList where costTo is null
        defaultSearchTrackingShouldNotBeFound("costTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageFromIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageFrom equals to DEFAULT_ACREAGE_FROM
        defaultSearchTrackingShouldBeFound("acreageFrom.equals=" + DEFAULT_ACREAGE_FROM);

        // Get all the searchTrackingList where acreageFrom equals to UPDATED_ACREAGE_FROM
        defaultSearchTrackingShouldNotBeFound("acreageFrom.equals=" + UPDATED_ACREAGE_FROM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageFromIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageFrom in DEFAULT_ACREAGE_FROM or UPDATED_ACREAGE_FROM
        defaultSearchTrackingShouldBeFound("acreageFrom.in=" + DEFAULT_ACREAGE_FROM + "," + UPDATED_ACREAGE_FROM);

        // Get all the searchTrackingList where acreageFrom equals to UPDATED_ACREAGE_FROM
        defaultSearchTrackingShouldNotBeFound("acreageFrom.in=" + UPDATED_ACREAGE_FROM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageFrom is not null
        defaultSearchTrackingShouldBeFound("acreageFrom.specified=true");

        // Get all the searchTrackingList where acreageFrom is null
        defaultSearchTrackingShouldNotBeFound("acreageFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageToIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageTo equals to DEFAULT_ACREAGE_TO
        defaultSearchTrackingShouldBeFound("acreageTo.equals=" + DEFAULT_ACREAGE_TO);

        // Get all the searchTrackingList where acreageTo equals to UPDATED_ACREAGE_TO
        defaultSearchTrackingShouldNotBeFound("acreageTo.equals=" + UPDATED_ACREAGE_TO);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageToIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageTo in DEFAULT_ACREAGE_TO or UPDATED_ACREAGE_TO
        defaultSearchTrackingShouldBeFound("acreageTo.in=" + DEFAULT_ACREAGE_TO + "," + UPDATED_ACREAGE_TO);

        // Get all the searchTrackingList where acreageTo equals to UPDATED_ACREAGE_TO
        defaultSearchTrackingShouldNotBeFound("acreageTo.in=" + UPDATED_ACREAGE_TO);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByAcreageToIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where acreageTo is not null
        defaultSearchTrackingShouldBeFound("acreageTo.specified=true");

        // Get all the searchTrackingList where acreageTo is null
        defaultSearchTrackingShouldNotBeFound("acreageTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where direction equals to DEFAULT_DIRECTION
        defaultSearchTrackingShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the searchTrackingList where direction equals to UPDATED_DIRECTION
        defaultSearchTrackingShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultSearchTrackingShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the searchTrackingList where direction equals to UPDATED_DIRECTION
        defaultSearchTrackingShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where direction is not null
        defaultSearchTrackingShouldBeFound("direction.specified=true");

        // Get all the searchTrackingList where direction is null
        defaultSearchTrackingShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where floor equals to DEFAULT_FLOOR
        defaultSearchTrackingShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the searchTrackingList where floor equals to UPDATED_FLOOR
        defaultSearchTrackingShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultSearchTrackingShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the searchTrackingList where floor equals to UPDATED_FLOOR
        defaultSearchTrackingShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where floor is not null
        defaultSearchTrackingShouldBeFound("floor.specified=true");

        // Get all the searchTrackingList where floor is null
        defaultSearchTrackingShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBathRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bathRoom equals to DEFAULT_BATH_ROOM
        defaultSearchTrackingShouldBeFound("bathRoom.equals=" + DEFAULT_BATH_ROOM);

        // Get all the searchTrackingList where bathRoom equals to UPDATED_BATH_ROOM
        defaultSearchTrackingShouldNotBeFound("bathRoom.equals=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBathRoomIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bathRoom in DEFAULT_BATH_ROOM or UPDATED_BATH_ROOM
        defaultSearchTrackingShouldBeFound("bathRoom.in=" + DEFAULT_BATH_ROOM + "," + UPDATED_BATH_ROOM);

        // Get all the searchTrackingList where bathRoom equals to UPDATED_BATH_ROOM
        defaultSearchTrackingShouldNotBeFound("bathRoom.in=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBathRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bathRoom is not null
        defaultSearchTrackingShouldBeFound("bathRoom.specified=true");

        // Get all the searchTrackingList where bathRoom is null
        defaultSearchTrackingShouldNotBeFound("bathRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBathRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bathRoom greater than or equals to DEFAULT_BATH_ROOM
        defaultSearchTrackingShouldBeFound("bathRoom.greaterOrEqualThan=" + DEFAULT_BATH_ROOM);

        // Get all the searchTrackingList where bathRoom greater than or equals to UPDATED_BATH_ROOM
        defaultSearchTrackingShouldNotBeFound("bathRoom.greaterOrEqualThan=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBathRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bathRoom less than or equals to DEFAULT_BATH_ROOM
        defaultSearchTrackingShouldNotBeFound("bathRoom.lessThan=" + DEFAULT_BATH_ROOM);

        // Get all the searchTrackingList where bathRoom less than or equals to UPDATED_BATH_ROOM
        defaultSearchTrackingShouldBeFound("bathRoom.lessThan=" + UPDATED_BATH_ROOM);
    }


    @Test
    @Transactional
    public void getAllSearchTrackingsByBedRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bedRoom equals to DEFAULT_BED_ROOM
        defaultSearchTrackingShouldBeFound("bedRoom.equals=" + DEFAULT_BED_ROOM);

        // Get all the searchTrackingList where bedRoom equals to UPDATED_BED_ROOM
        defaultSearchTrackingShouldNotBeFound("bedRoom.equals=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBedRoomIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bedRoom in DEFAULT_BED_ROOM or UPDATED_BED_ROOM
        defaultSearchTrackingShouldBeFound("bedRoom.in=" + DEFAULT_BED_ROOM + "," + UPDATED_BED_ROOM);

        // Get all the searchTrackingList where bedRoom equals to UPDATED_BED_ROOM
        defaultSearchTrackingShouldNotBeFound("bedRoom.in=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBedRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bedRoom is not null
        defaultSearchTrackingShouldBeFound("bedRoom.specified=true");

        // Get all the searchTrackingList where bedRoom is null
        defaultSearchTrackingShouldNotBeFound("bedRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBedRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bedRoom greater than or equals to DEFAULT_BED_ROOM
        defaultSearchTrackingShouldBeFound("bedRoom.greaterOrEqualThan=" + DEFAULT_BED_ROOM);

        // Get all the searchTrackingList where bedRoom greater than or equals to UPDATED_BED_ROOM
        defaultSearchTrackingShouldNotBeFound("bedRoom.greaterOrEqualThan=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByBedRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where bedRoom less than or equals to DEFAULT_BED_ROOM
        defaultSearchTrackingShouldNotBeFound("bedRoom.lessThan=" + DEFAULT_BED_ROOM);

        // Get all the searchTrackingList where bedRoom less than or equals to UPDATED_BED_ROOM
        defaultSearchTrackingShouldBeFound("bedRoom.lessThan=" + UPDATED_BED_ROOM);
    }


    @Test
    @Transactional
    public void getAllSearchTrackingsByParkingIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where parking equals to DEFAULT_PARKING
        defaultSearchTrackingShouldBeFound("parking.equals=" + DEFAULT_PARKING);

        // Get all the searchTrackingList where parking equals to UPDATED_PARKING
        defaultSearchTrackingShouldNotBeFound("parking.equals=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByParkingIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where parking in DEFAULT_PARKING or UPDATED_PARKING
        defaultSearchTrackingShouldBeFound("parking.in=" + DEFAULT_PARKING + "," + UPDATED_PARKING);

        // Get all the searchTrackingList where parking equals to UPDATED_PARKING
        defaultSearchTrackingShouldNotBeFound("parking.in=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByParkingIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where parking is not null
        defaultSearchTrackingShouldBeFound("parking.specified=true");

        // Get all the searchTrackingList where parking is null
        defaultSearchTrackingShouldNotBeFound("parking.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByLandTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where landType equals to DEFAULT_LAND_TYPE
        defaultSearchTrackingShouldBeFound("landType.equals=" + DEFAULT_LAND_TYPE);

        // Get all the searchTrackingList where landType equals to UPDATED_LAND_TYPE
        defaultSearchTrackingShouldNotBeFound("landType.equals=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByLandTypeIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where landType in DEFAULT_LAND_TYPE or UPDATED_LAND_TYPE
        defaultSearchTrackingShouldBeFound("landType.in=" + DEFAULT_LAND_TYPE + "," + UPDATED_LAND_TYPE);

        // Get all the searchTrackingList where landType equals to UPDATED_LAND_TYPE
        defaultSearchTrackingShouldNotBeFound("landType.in=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByLandTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where landType is not null
        defaultSearchTrackingShouldBeFound("landType.specified=true");

        // Get all the searchTrackingList where landType is null
        defaultSearchTrackingShouldNotBeFound("landType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where createAt equals to DEFAULT_CREATE_AT
        defaultSearchTrackingShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the searchTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultSearchTrackingShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultSearchTrackingShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the searchTrackingList where createAt equals to UPDATED_CREATE_AT
        defaultSearchTrackingShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where createAt is not null
        defaultSearchTrackingShouldBeFound("createAt.specified=true");

        // Get all the searchTrackingList where createAt is null
        defaultSearchTrackingShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultSearchTrackingShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the searchTrackingList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultSearchTrackingShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllSearchTrackingsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        // Get all the searchTrackingList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultSearchTrackingShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the searchTrackingList where createAt less than or equals to UPDATED_CREATE_AT
        defaultSearchTrackingShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllSearchTrackingsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        searchTracking.setUser(user);
        searchTrackingRepository.saveAndFlush(searchTracking);
        Long userId = user.getId();

        // Get all the searchTrackingList where user equals to userId
        defaultSearchTrackingShouldBeFound("userId.equals=" + userId);

        // Get all the searchTrackingList where user equals to userId + 1
        defaultSearchTrackingShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllSearchTrackingsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        searchTracking.setCity(city);
        searchTrackingRepository.saveAndFlush(searchTracking);
        Long cityId = city.getId();

        // Get all the searchTrackingList where city equals to cityId
        defaultSearchTrackingShouldBeFound("cityId.equals=" + cityId);

        // Get all the searchTrackingList where city equals to cityId + 1
        defaultSearchTrackingShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }


    @Test
    @Transactional
    public void getAllSearchTrackingsByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        Street street = StreetResourceIntTest.createEntity(em);
        em.persist(street);
        em.flush();
        searchTracking.setStreet(street);
        searchTrackingRepository.saveAndFlush(searchTracking);
        Long streetId = street.getId();

        // Get all the searchTrackingList where street equals to streetId
        defaultSearchTrackingShouldBeFound("streetId.equals=" + streetId);

        // Get all the searchTrackingList where street equals to streetId + 1
        defaultSearchTrackingShouldNotBeFound("streetId.equals=" + (streetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSearchTrackingShouldBeFound(String filter) throws Exception {
        restSearchTrackingMockMvc.perform(get("/api/search-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchTracking.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSearchTrackingShouldNotBeFound(String filter) throws Exception {
        restSearchTrackingMockMvc.perform(get("/api/search-trackings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingSearchTracking() throws Exception {
        // Get the searchTracking
        restSearchTrackingMockMvc.perform(get("/api/search-trackings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchTracking() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        int databaseSizeBeforeUpdate = searchTrackingRepository.findAll().size();

        // Update the searchTracking
        SearchTracking updatedSearchTracking = searchTrackingRepository.findById(searchTracking.getId()).get();
        // Disconnect from session so that the updates on updatedSearchTracking are not directly saved in db
        em.detach(updatedSearchTracking);
        updatedSearchTracking
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
            .createAt(UPDATED_CREATE_AT);
        SearchTrackingDTO searchTrackingDTO = searchTrackingMapper.toDto(updatedSearchTracking);

        restSearchTrackingMockMvc.perform(put("/api/search-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchTrackingDTO)))
            .andExpect(status().isOk());

        // Validate the SearchTracking in the database
        List<SearchTracking> searchTrackingList = searchTrackingRepository.findAll();
        assertThat(searchTrackingList).hasSize(databaseSizeBeforeUpdate);
        SearchTracking testSearchTracking = searchTrackingList.get(searchTrackingList.size() - 1);
        assertThat(testSearchTracking.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testSearchTracking.getKeyword()).isEqualTo(UPDATED_KEYWORD);
        assertThat(testSearchTracking.getCostFrom()).isEqualTo(UPDATED_COST_FROM);
        assertThat(testSearchTracking.getCostTo()).isEqualTo(UPDATED_COST_TO);
        assertThat(testSearchTracking.getAcreageFrom()).isEqualTo(UPDATED_ACREAGE_FROM);
        assertThat(testSearchTracking.getAcreageTo()).isEqualTo(UPDATED_ACREAGE_TO);
        assertThat(testSearchTracking.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testSearchTracking.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testSearchTracking.getBathRoom()).isEqualTo(UPDATED_BATH_ROOM);
        assertThat(testSearchTracking.getBedRoom()).isEqualTo(UPDATED_BED_ROOM);
        assertThat(testSearchTracking.isParking()).isEqualTo(UPDATED_PARKING);
        assertThat(testSearchTracking.getLandType()).isEqualTo(UPDATED_LAND_TYPE);
        assertThat(testSearchTracking.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);

        // Validate the SearchTracking in Elasticsearch
        verify(mockSearchTrackingSearchRepository, times(1)).save(testSearchTracking);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchTracking() throws Exception {
        int databaseSizeBeforeUpdate = searchTrackingRepository.findAll().size();

        // Create the SearchTracking
        SearchTrackingDTO searchTrackingDTO = searchTrackingMapper.toDto(searchTracking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSearchTrackingMockMvc.perform(put("/api/search-trackings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchTracking in the database
        List<SearchTracking> searchTrackingList = searchTrackingRepository.findAll();
        assertThat(searchTrackingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SearchTracking in Elasticsearch
        verify(mockSearchTrackingSearchRepository, times(0)).save(searchTracking);
    }

    @Test
    @Transactional
    public void deleteSearchTracking() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);

        int databaseSizeBeforeDelete = searchTrackingRepository.findAll().size();

        // Get the searchTracking
        restSearchTrackingMockMvc.perform(delete("/api/search-trackings/{id}", searchTracking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchTracking> searchTrackingList = searchTrackingRepository.findAll();
        assertThat(searchTrackingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SearchTracking in Elasticsearch
        verify(mockSearchTrackingSearchRepository, times(1)).deleteById(searchTracking.getId());
    }

    @Test
    @Transactional
    public void searchSearchTracking() throws Exception {
        // Initialize the database
        searchTrackingRepository.saveAndFlush(searchTracking);
        when(mockSearchTrackingSearchRepository.search(queryStringQuery("id:" + searchTracking.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(searchTracking), PageRequest.of(0, 1), 1));
        // Search the searchTracking
        restSearchTrackingMockMvc.perform(get("/api/_search/search-trackings?query=id:" + searchTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchTracking.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchTracking.class);
        SearchTracking searchTracking1 = new SearchTracking();
        searchTracking1.setId(1L);
        SearchTracking searchTracking2 = new SearchTracking();
        searchTracking2.setId(searchTracking1.getId());
        assertThat(searchTracking1).isEqualTo(searchTracking2);
        searchTracking2.setId(2L);
        assertThat(searchTracking1).isNotEqualTo(searchTracking2);
        searchTracking1.setId(null);
        assertThat(searchTracking1).isNotEqualTo(searchTracking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchTrackingDTO.class);
        SearchTrackingDTO searchTrackingDTO1 = new SearchTrackingDTO();
        searchTrackingDTO1.setId(1L);
        SearchTrackingDTO searchTrackingDTO2 = new SearchTrackingDTO();
        assertThat(searchTrackingDTO1).isNotEqualTo(searchTrackingDTO2);
        searchTrackingDTO2.setId(searchTrackingDTO1.getId());
        assertThat(searchTrackingDTO1).isEqualTo(searchTrackingDTO2);
        searchTrackingDTO2.setId(2L);
        assertThat(searchTrackingDTO1).isNotEqualTo(searchTrackingDTO2);
        searchTrackingDTO1.setId(null);
        assertThat(searchTrackingDTO1).isNotEqualTo(searchTrackingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(searchTrackingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(searchTrackingMapper.fromId(null)).isNull();
    }
}
