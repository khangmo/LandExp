package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.House;
import com.landexp.domain.City;
import com.landexp.domain.Street;
import com.landexp.domain.LandProjects;
import com.landexp.domain.User;
import com.landexp.domain.User;
import com.landexp.repository.HouseRepository;
import com.landexp.repository.search.HouseSearchRepository;
import com.landexp.service.HouseService;
import com.landexp.service.dto.HouseDTO;
import com.landexp.service.mapper.HouseMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.HouseCriteria;
import com.landexp.service.HouseQueryService;

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

import com.landexp.domain.enumeration.UserActionType;
import com.landexp.domain.enumeration.MoneyType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.LandType;
import com.landexp.domain.enumeration.SaleType;
import com.landexp.domain.enumeration.StatusType;
/**
 * Test class for the HouseResource REST controller.
 *
 * @see HouseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class HouseResourceIntTest {

    private static final UserActionType DEFAULT_ACTION_TYPE = UserActionType.FOR_BUY;
    private static final UserActionType UPDATED_ACTION_TYPE = UserActionType.FOR_SELL;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Float DEFAULT_MONEY = 1F;
    private static final Float UPDATED_MONEY = 2F;

    private static final MoneyType DEFAULT_MONEY_TYPE = MoneyType.MILLION;
    private static final MoneyType UPDATED_MONEY_TYPE = MoneyType.HUNDRED_MILLION;

    private static final Float DEFAULT_ACREAGE = 1F;
    private static final Float UPDATED_ACREAGE = 2F;

    private static final Float DEFAULT_DISCOUNT = 1F;
    private static final Float UPDATED_DISCOUNT = 2F;

    private static final DirectionType DEFAULT_DIRECTION = DirectionType.NORTH;
    private static final DirectionType UPDATED_DIRECTION = DirectionType.SOUTH;

    private static final String DEFAULT_FLOOR = "AAAAAAAAAA";
    private static final String UPDATED_FLOOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_BATH_ROOM = 1;
    private static final Integer UPDATED_BATH_ROOM = 2;

    private static final Boolean DEFAULT_PARKING = false;
    private static final Boolean UPDATED_PARKING = true;

    private static final Integer DEFAULT_BED_ROOM = 1;
    private static final Integer UPDATED_BED_ROOM = 2;

    private static final LandType DEFAULT_LAND_TYPE = LandType.APARTMENT;
    private static final LandType UPDATED_LAND_TYPE = LandType.PENHOUSE;

    private static final SaleType DEFAULT_SALE_TYPE = SaleType.SALE_BY_MYSELF;
    private static final SaleType UPDATED_SALE_TYPE = SaleType.SALE_SUPPORT;

    private static final Float DEFAULT_FEE = 1F;
    private static final Float UPDATED_FEE = 2F;

    private static final Float DEFAULT_FEE_MAX = 1F;
    private static final Float UPDATED_FEE_MAX = 2F;

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;

    private static final StatusType DEFAULT_STATUS_TYPE = StatusType.PENDING;
    private static final StatusType UPDATED_STATUS_TYPE = StatusType.PAID;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HouseRepository houseRepository;


    @Autowired
    private HouseMapper houseMapper;
    

    @Autowired
    private HouseService houseService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.HouseSearchRepositoryMockConfiguration
     */
    @Autowired
    private HouseSearchRepository mockHouseSearchRepository;

    @Autowired
    private HouseQueryService houseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHouseMockMvc;

    private House house;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HouseResource houseResource = new HouseResource(houseService, houseQueryService);
        this.restHouseMockMvc = MockMvcBuilders.standaloneSetup(houseResource)
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
    public static House createEntity(EntityManager em) {
        House house = new House()
            .actionType(DEFAULT_ACTION_TYPE)
            .address(DEFAULT_ADDRESS)
            .money(DEFAULT_MONEY)
            .moneyType(DEFAULT_MONEY_TYPE)
            .acreage(DEFAULT_ACREAGE)
            .discount(DEFAULT_DISCOUNT)
            .direction(DEFAULT_DIRECTION)
            .floor(DEFAULT_FLOOR)
            .bathRoom(DEFAULT_BATH_ROOM)
            .parking(DEFAULT_PARKING)
            .bedRoom(DEFAULT_BED_ROOM)
            .landType(DEFAULT_LAND_TYPE)
            .saleType(DEFAULT_SALE_TYPE)
            .fee(DEFAULT_FEE)
            .feeMax(DEFAULT_FEE_MAX)
            .hits(DEFAULT_HITS)
            .statusType(DEFAULT_STATUS_TYPE)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return house;
    }

    @Before
    public void initTest() {
        house = createEntity(em);
    }

    @Test
    @Transactional
    public void createHouse() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House
        HouseDTO houseDTO = houseMapper.toDto(house);
        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate + 1);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testHouse.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHouse.getMoney()).isEqualTo(DEFAULT_MONEY);
        assertThat(testHouse.getMoneyType()).isEqualTo(DEFAULT_MONEY_TYPE);
        assertThat(testHouse.getAcreage()).isEqualTo(DEFAULT_ACREAGE);
        assertThat(testHouse.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testHouse.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testHouse.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testHouse.getBathRoom()).isEqualTo(DEFAULT_BATH_ROOM);
        assertThat(testHouse.isParking()).isEqualTo(DEFAULT_PARKING);
        assertThat(testHouse.getBedRoom()).isEqualTo(DEFAULT_BED_ROOM);
        assertThat(testHouse.getLandType()).isEqualTo(DEFAULT_LAND_TYPE);
        assertThat(testHouse.getSaleType()).isEqualTo(DEFAULT_SALE_TYPE);
        assertThat(testHouse.getFee()).isEqualTo(DEFAULT_FEE);
        assertThat(testHouse.getFeeMax()).isEqualTo(DEFAULT_FEE_MAX);
        assertThat(testHouse.getHits()).isEqualTo(DEFAULT_HITS);
        assertThat(testHouse.getStatusType()).isEqualTo(DEFAULT_STATUS_TYPE);
        assertThat(testHouse.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testHouse.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the House in Elasticsearch
        verify(mockHouseSearchRepository, times(1)).save(testHouse);
    }

    @Test
    @Transactional
    public void createHouseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House with an existing ID
        house.setId(1L);
        HouseDTO houseDTO = houseMapper.toDto(house);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate);

        // Validate the House in Elasticsearch
        verify(mockHouseSearchRepository, times(0)).save(house);
    }

    @Test
    @Transactional
    public void getAllHouses() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].moneyType").value(hasItem(DEFAULT_MONEY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acreage").value(hasItem(DEFAULT_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].saleType").value(hasItem(DEFAULT_SALE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].feeMax").value(hasItem(DEFAULT_FEE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(house.getId().intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.money").value(DEFAULT_MONEY.doubleValue()))
            .andExpect(jsonPath("$.moneyType").value(DEFAULT_MONEY_TYPE.toString()))
            .andExpect(jsonPath("$.acreage").value(DEFAULT_ACREAGE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR.toString()))
            .andExpect(jsonPath("$.bathRoom").value(DEFAULT_BATH_ROOM))
            .andExpect(jsonPath("$.parking").value(DEFAULT_PARKING.booleanValue()))
            .andExpect(jsonPath("$.bedRoom").value(DEFAULT_BED_ROOM))
            .andExpect(jsonPath("$.landType").value(DEFAULT_LAND_TYPE.toString()))
            .andExpect(jsonPath("$.saleType").value(DEFAULT_SALE_TYPE.toString()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.doubleValue()))
            .andExpect(jsonPath("$.feeMax").value(DEFAULT_FEE_MAX.doubleValue()))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS))
            .andExpect(jsonPath("$.statusType").value(DEFAULT_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllHousesByActionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where actionType equals to DEFAULT_ACTION_TYPE
        defaultHouseShouldBeFound("actionType.equals=" + DEFAULT_ACTION_TYPE);

        // Get all the houseList where actionType equals to UPDATED_ACTION_TYPE
        defaultHouseShouldNotBeFound("actionType.equals=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByActionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where actionType in DEFAULT_ACTION_TYPE or UPDATED_ACTION_TYPE
        defaultHouseShouldBeFound("actionType.in=" + DEFAULT_ACTION_TYPE + "," + UPDATED_ACTION_TYPE);

        // Get all the houseList where actionType equals to UPDATED_ACTION_TYPE
        defaultHouseShouldNotBeFound("actionType.in=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByActionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where actionType is not null
        defaultHouseShouldBeFound("actionType.specified=true");

        // Get all the houseList where actionType is null
        defaultHouseShouldNotBeFound("actionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where address equals to DEFAULT_ADDRESS
        defaultHouseShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the houseList where address equals to UPDATED_ADDRESS
        defaultHouseShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHousesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHouseShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the houseList where address equals to UPDATED_ADDRESS
        defaultHouseShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHousesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where address is not null
        defaultHouseShouldBeFound("address.specified=true");

        // Get all the houseList where address is null
        defaultHouseShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where money equals to DEFAULT_MONEY
        defaultHouseShouldBeFound("money.equals=" + DEFAULT_MONEY);

        // Get all the houseList where money equals to UPDATED_MONEY
        defaultHouseShouldNotBeFound("money.equals=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where money in DEFAULT_MONEY or UPDATED_MONEY
        defaultHouseShouldBeFound("money.in=" + DEFAULT_MONEY + "," + UPDATED_MONEY);

        // Get all the houseList where money equals to UPDATED_MONEY
        defaultHouseShouldNotBeFound("money.in=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where money is not null
        defaultHouseShouldBeFound("money.specified=true");

        // Get all the houseList where money is null
        defaultHouseShouldNotBeFound("money.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where moneyType equals to DEFAULT_MONEY_TYPE
        defaultHouseShouldBeFound("moneyType.equals=" + DEFAULT_MONEY_TYPE);

        // Get all the houseList where moneyType equals to UPDATED_MONEY_TYPE
        defaultHouseShouldNotBeFound("moneyType.equals=" + UPDATED_MONEY_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where moneyType in DEFAULT_MONEY_TYPE or UPDATED_MONEY_TYPE
        defaultHouseShouldBeFound("moneyType.in=" + DEFAULT_MONEY_TYPE + "," + UPDATED_MONEY_TYPE);

        // Get all the houseList where moneyType equals to UPDATED_MONEY_TYPE
        defaultHouseShouldNotBeFound("moneyType.in=" + UPDATED_MONEY_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByMoneyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where moneyType is not null
        defaultHouseShouldBeFound("moneyType.specified=true");

        // Get all the houseList where moneyType is null
        defaultHouseShouldNotBeFound("moneyType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByAcreageIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where acreage equals to DEFAULT_ACREAGE
        defaultHouseShouldBeFound("acreage.equals=" + DEFAULT_ACREAGE);

        // Get all the houseList where acreage equals to UPDATED_ACREAGE
        defaultHouseShouldNotBeFound("acreage.equals=" + UPDATED_ACREAGE);
    }

    @Test
    @Transactional
    public void getAllHousesByAcreageIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where acreage in DEFAULT_ACREAGE or UPDATED_ACREAGE
        defaultHouseShouldBeFound("acreage.in=" + DEFAULT_ACREAGE + "," + UPDATED_ACREAGE);

        // Get all the houseList where acreage equals to UPDATED_ACREAGE
        defaultHouseShouldNotBeFound("acreage.in=" + UPDATED_ACREAGE);
    }

    @Test
    @Transactional
    public void getAllHousesByAcreageIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where acreage is not null
        defaultHouseShouldBeFound("acreage.specified=true");

        // Get all the houseList where acreage is null
        defaultHouseShouldNotBeFound("acreage.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where discount equals to DEFAULT_DISCOUNT
        defaultHouseShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the houseList where discount equals to UPDATED_DISCOUNT
        defaultHouseShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllHousesByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultHouseShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the houseList where discount equals to UPDATED_DISCOUNT
        defaultHouseShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllHousesByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where discount is not null
        defaultHouseShouldBeFound("discount.specified=true");

        // Get all the houseList where discount is null
        defaultHouseShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where direction equals to DEFAULT_DIRECTION
        defaultHouseShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the houseList where direction equals to UPDATED_DIRECTION
        defaultHouseShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllHousesByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultHouseShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the houseList where direction equals to UPDATED_DIRECTION
        defaultHouseShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void getAllHousesByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where direction is not null
        defaultHouseShouldBeFound("direction.specified=true");

        // Get all the houseList where direction is null
        defaultHouseShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByFloorIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where floor equals to DEFAULT_FLOOR
        defaultHouseShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the houseList where floor equals to UPDATED_FLOOR
        defaultHouseShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllHousesByFloorIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultHouseShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the houseList where floor equals to UPDATED_FLOOR
        defaultHouseShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void getAllHousesByFloorIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where floor is not null
        defaultHouseShouldBeFound("floor.specified=true");

        // Get all the houseList where floor is null
        defaultHouseShouldNotBeFound("floor.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByBathRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bathRoom equals to DEFAULT_BATH_ROOM
        defaultHouseShouldBeFound("bathRoom.equals=" + DEFAULT_BATH_ROOM);

        // Get all the houseList where bathRoom equals to UPDATED_BATH_ROOM
        defaultHouseShouldNotBeFound("bathRoom.equals=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBathRoomIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bathRoom in DEFAULT_BATH_ROOM or UPDATED_BATH_ROOM
        defaultHouseShouldBeFound("bathRoom.in=" + DEFAULT_BATH_ROOM + "," + UPDATED_BATH_ROOM);

        // Get all the houseList where bathRoom equals to UPDATED_BATH_ROOM
        defaultHouseShouldNotBeFound("bathRoom.in=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBathRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bathRoom is not null
        defaultHouseShouldBeFound("bathRoom.specified=true");

        // Get all the houseList where bathRoom is null
        defaultHouseShouldNotBeFound("bathRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByBathRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bathRoom greater than or equals to DEFAULT_BATH_ROOM
        defaultHouseShouldBeFound("bathRoom.greaterOrEqualThan=" + DEFAULT_BATH_ROOM);

        // Get all the houseList where bathRoom greater than or equals to UPDATED_BATH_ROOM
        defaultHouseShouldNotBeFound("bathRoom.greaterOrEqualThan=" + UPDATED_BATH_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBathRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bathRoom less than or equals to DEFAULT_BATH_ROOM
        defaultHouseShouldNotBeFound("bathRoom.lessThan=" + DEFAULT_BATH_ROOM);

        // Get all the houseList where bathRoom less than or equals to UPDATED_BATH_ROOM
        defaultHouseShouldBeFound("bathRoom.lessThan=" + UPDATED_BATH_ROOM);
    }


    @Test
    @Transactional
    public void getAllHousesByParkingIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where parking equals to DEFAULT_PARKING
        defaultHouseShouldBeFound("parking.equals=" + DEFAULT_PARKING);

        // Get all the houseList where parking equals to UPDATED_PARKING
        defaultHouseShouldNotBeFound("parking.equals=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllHousesByParkingIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where parking in DEFAULT_PARKING or UPDATED_PARKING
        defaultHouseShouldBeFound("parking.in=" + DEFAULT_PARKING + "," + UPDATED_PARKING);

        // Get all the houseList where parking equals to UPDATED_PARKING
        defaultHouseShouldNotBeFound("parking.in=" + UPDATED_PARKING);
    }

    @Test
    @Transactional
    public void getAllHousesByParkingIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where parking is not null
        defaultHouseShouldBeFound("parking.specified=true");

        // Get all the houseList where parking is null
        defaultHouseShouldNotBeFound("parking.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByBedRoomIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bedRoom equals to DEFAULT_BED_ROOM
        defaultHouseShouldBeFound("bedRoom.equals=" + DEFAULT_BED_ROOM);

        // Get all the houseList where bedRoom equals to UPDATED_BED_ROOM
        defaultHouseShouldNotBeFound("bedRoom.equals=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBedRoomIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bedRoom in DEFAULT_BED_ROOM or UPDATED_BED_ROOM
        defaultHouseShouldBeFound("bedRoom.in=" + DEFAULT_BED_ROOM + "," + UPDATED_BED_ROOM);

        // Get all the houseList where bedRoom equals to UPDATED_BED_ROOM
        defaultHouseShouldNotBeFound("bedRoom.in=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBedRoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bedRoom is not null
        defaultHouseShouldBeFound("bedRoom.specified=true");

        // Get all the houseList where bedRoom is null
        defaultHouseShouldNotBeFound("bedRoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByBedRoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bedRoom greater than or equals to DEFAULT_BED_ROOM
        defaultHouseShouldBeFound("bedRoom.greaterOrEqualThan=" + DEFAULT_BED_ROOM);

        // Get all the houseList where bedRoom greater than or equals to UPDATED_BED_ROOM
        defaultHouseShouldNotBeFound("bedRoom.greaterOrEqualThan=" + UPDATED_BED_ROOM);
    }

    @Test
    @Transactional
    public void getAllHousesByBedRoomIsLessThanSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where bedRoom less than or equals to DEFAULT_BED_ROOM
        defaultHouseShouldNotBeFound("bedRoom.lessThan=" + DEFAULT_BED_ROOM);

        // Get all the houseList where bedRoom less than or equals to UPDATED_BED_ROOM
        defaultHouseShouldBeFound("bedRoom.lessThan=" + UPDATED_BED_ROOM);
    }


    @Test
    @Transactional
    public void getAllHousesByLandTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where landType equals to DEFAULT_LAND_TYPE
        defaultHouseShouldBeFound("landType.equals=" + DEFAULT_LAND_TYPE);

        // Get all the houseList where landType equals to UPDATED_LAND_TYPE
        defaultHouseShouldNotBeFound("landType.equals=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByLandTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where landType in DEFAULT_LAND_TYPE or UPDATED_LAND_TYPE
        defaultHouseShouldBeFound("landType.in=" + DEFAULT_LAND_TYPE + "," + UPDATED_LAND_TYPE);

        // Get all the houseList where landType equals to UPDATED_LAND_TYPE
        defaultHouseShouldNotBeFound("landType.in=" + UPDATED_LAND_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByLandTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where landType is not null
        defaultHouseShouldBeFound("landType.specified=true");

        // Get all the houseList where landType is null
        defaultHouseShouldNotBeFound("landType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesBySaleTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where saleType equals to DEFAULT_SALE_TYPE
        defaultHouseShouldBeFound("saleType.equals=" + DEFAULT_SALE_TYPE);

        // Get all the houseList where saleType equals to UPDATED_SALE_TYPE
        defaultHouseShouldNotBeFound("saleType.equals=" + UPDATED_SALE_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesBySaleTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where saleType in DEFAULT_SALE_TYPE or UPDATED_SALE_TYPE
        defaultHouseShouldBeFound("saleType.in=" + DEFAULT_SALE_TYPE + "," + UPDATED_SALE_TYPE);

        // Get all the houseList where saleType equals to UPDATED_SALE_TYPE
        defaultHouseShouldNotBeFound("saleType.in=" + UPDATED_SALE_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesBySaleTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where saleType is not null
        defaultHouseShouldBeFound("saleType.specified=true");

        // Get all the houseList where saleType is null
        defaultHouseShouldNotBeFound("saleType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where fee equals to DEFAULT_FEE
        defaultHouseShouldBeFound("fee.equals=" + DEFAULT_FEE);

        // Get all the houseList where fee equals to UPDATED_FEE
        defaultHouseShouldNotBeFound("fee.equals=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllHousesByFeeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where fee in DEFAULT_FEE or UPDATED_FEE
        defaultHouseShouldBeFound("fee.in=" + DEFAULT_FEE + "," + UPDATED_FEE);

        // Get all the houseList where fee equals to UPDATED_FEE
        defaultHouseShouldNotBeFound("fee.in=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllHousesByFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where fee is not null
        defaultHouseShouldBeFound("fee.specified=true");

        // Get all the houseList where fee is null
        defaultHouseShouldNotBeFound("fee.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByFeeMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where feeMax equals to DEFAULT_FEE_MAX
        defaultHouseShouldBeFound("feeMax.equals=" + DEFAULT_FEE_MAX);

        // Get all the houseList where feeMax equals to UPDATED_FEE_MAX
        defaultHouseShouldNotBeFound("feeMax.equals=" + UPDATED_FEE_MAX);
    }

    @Test
    @Transactional
    public void getAllHousesByFeeMaxIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where feeMax in DEFAULT_FEE_MAX or UPDATED_FEE_MAX
        defaultHouseShouldBeFound("feeMax.in=" + DEFAULT_FEE_MAX + "," + UPDATED_FEE_MAX);

        // Get all the houseList where feeMax equals to UPDATED_FEE_MAX
        defaultHouseShouldNotBeFound("feeMax.in=" + UPDATED_FEE_MAX);
    }

    @Test
    @Transactional
    public void getAllHousesByFeeMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where feeMax is not null
        defaultHouseShouldBeFound("feeMax.specified=true");

        // Get all the houseList where feeMax is null
        defaultHouseShouldNotBeFound("feeMax.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByHitsIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where hits equals to DEFAULT_HITS
        defaultHouseShouldBeFound("hits.equals=" + DEFAULT_HITS);

        // Get all the houseList where hits equals to UPDATED_HITS
        defaultHouseShouldNotBeFound("hits.equals=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllHousesByHitsIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where hits in DEFAULT_HITS or UPDATED_HITS
        defaultHouseShouldBeFound("hits.in=" + DEFAULT_HITS + "," + UPDATED_HITS);

        // Get all the houseList where hits equals to UPDATED_HITS
        defaultHouseShouldNotBeFound("hits.in=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllHousesByHitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where hits is not null
        defaultHouseShouldBeFound("hits.specified=true");

        // Get all the houseList where hits is null
        defaultHouseShouldNotBeFound("hits.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByHitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where hits greater than or equals to DEFAULT_HITS
        defaultHouseShouldBeFound("hits.greaterOrEqualThan=" + DEFAULT_HITS);

        // Get all the houseList where hits greater than or equals to UPDATED_HITS
        defaultHouseShouldNotBeFound("hits.greaterOrEqualThan=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    public void getAllHousesByHitsIsLessThanSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where hits less than or equals to DEFAULT_HITS
        defaultHouseShouldNotBeFound("hits.lessThan=" + DEFAULT_HITS);

        // Get all the houseList where hits less than or equals to UPDATED_HITS
        defaultHouseShouldBeFound("hits.lessThan=" + UPDATED_HITS);
    }


    @Test
    @Transactional
    public void getAllHousesByStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where statusType equals to DEFAULT_STATUS_TYPE
        defaultHouseShouldBeFound("statusType.equals=" + DEFAULT_STATUS_TYPE);

        // Get all the houseList where statusType equals to UPDATED_STATUS_TYPE
        defaultHouseShouldNotBeFound("statusType.equals=" + UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where statusType in DEFAULT_STATUS_TYPE or UPDATED_STATUS_TYPE
        defaultHouseShouldBeFound("statusType.in=" + DEFAULT_STATUS_TYPE + "," + UPDATED_STATUS_TYPE);

        // Get all the houseList where statusType equals to UPDATED_STATUS_TYPE
        defaultHouseShouldNotBeFound("statusType.in=" + UPDATED_STATUS_TYPE);
    }

    @Test
    @Transactional
    public void getAllHousesByStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where statusType is not null
        defaultHouseShouldBeFound("statusType.specified=true");

        // Get all the houseList where statusType is null
        defaultHouseShouldNotBeFound("statusType.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where createAt equals to DEFAULT_CREATE_AT
        defaultHouseShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the houseList where createAt equals to UPDATED_CREATE_AT
        defaultHouseShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultHouseShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the houseList where createAt equals to UPDATED_CREATE_AT
        defaultHouseShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where createAt is not null
        defaultHouseShouldBeFound("createAt.specified=true");

        // Get all the houseList where createAt is null
        defaultHouseShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultHouseShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the houseList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultHouseShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultHouseShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the houseList where createAt less than or equals to UPDATED_CREATE_AT
        defaultHouseShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllHousesByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where updateAt equals to DEFAULT_UPDATE_AT
        defaultHouseShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the houseList where updateAt equals to UPDATED_UPDATE_AT
        defaultHouseShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultHouseShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the houseList where updateAt equals to UPDATED_UPDATE_AT
        defaultHouseShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where updateAt is not null
        defaultHouseShouldBeFound("updateAt.specified=true");

        // Get all the houseList where updateAt is null
        defaultHouseShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllHousesByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where updateAt greater than or equals to DEFAULT_UPDATE_AT
        defaultHouseShouldBeFound("updateAt.greaterOrEqualThan=" + DEFAULT_UPDATE_AT);

        // Get all the houseList where updateAt greater than or equals to UPDATED_UPDATE_AT
        defaultHouseShouldNotBeFound("updateAt.greaterOrEqualThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllHousesByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList where updateAt less than or equals to DEFAULT_UPDATE_AT
        defaultHouseShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the houseList where updateAt less than or equals to UPDATED_UPDATE_AT
        defaultHouseShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }


    @Test
    @Transactional
    public void getAllHousesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        house.setCity(city);
        houseRepository.saveAndFlush(house);
        Long cityId = city.getId();

        // Get all the houseList where city equals to cityId
        defaultHouseShouldBeFound("cityId.equals=" + cityId);

        // Get all the houseList where city equals to cityId + 1
        defaultHouseShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }


    @Test
    @Transactional
    public void getAllHousesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        Street street = StreetResourceIntTest.createEntity(em);
        em.persist(street);
        em.flush();
        house.setStreet(street);
        houseRepository.saveAndFlush(house);
        Long streetId = street.getId();

        // Get all the houseList where street equals to streetId
        defaultHouseShouldBeFound("streetId.equals=" + streetId);

        // Get all the houseList where street equals to streetId + 1
        defaultHouseShouldNotBeFound("streetId.equals=" + (streetId + 1));
    }


    @Test
    @Transactional
    public void getAllHousesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        LandProjects project = LandProjectsResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        house.setProject(project);
        houseRepository.saveAndFlush(house);
        Long projectId = project.getId();

        // Get all the houseList where project equals to projectId
        defaultHouseShouldBeFound("projectId.equals=" + projectId);

        // Get all the houseList where project equals to projectId + 1
        defaultHouseShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllHousesByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User createBy = UserResourceIntTest.createEntity(em);
        em.persist(createBy);
        em.flush();
        house.setCreateBy(createBy);
        houseRepository.saveAndFlush(house);
        Long createById = createBy.getId();

        // Get all the houseList where createBy equals to createById
        defaultHouseShouldBeFound("createById.equals=" + createById);

        // Get all the houseList where createBy equals to createById + 1
        defaultHouseShouldNotBeFound("createById.equals=" + (createById + 1));
    }


    @Test
    @Transactional
    public void getAllHousesByUpdateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User updateBy = UserResourceIntTest.createEntity(em);
        em.persist(updateBy);
        em.flush();
        house.setUpdateBy(updateBy);
        houseRepository.saveAndFlush(house);
        Long updateById = updateBy.getId();

        // Get all the houseList where updateBy equals to updateById
        defaultHouseShouldBeFound("updateById.equals=" + updateById);

        // Get all the houseList where updateBy equals to updateById + 1
        defaultHouseShouldNotBeFound("updateById.equals=" + (updateById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHouseShouldBeFound(String filter) throws Exception {
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].moneyType").value(hasItem(DEFAULT_MONEY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acreage").value(hasItem(DEFAULT_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].saleType").value(hasItem(DEFAULT_SALE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].feeMax").value(hasItem(DEFAULT_FEE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHouseShouldNotBeFound(String filter) throws Exception {
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingHouse() throws Exception {
        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house
        House updatedHouse = houseRepository.findById(house.getId()).get();
        // Disconnect from session so that the updates on updatedHouse are not directly saved in db
        em.detach(updatedHouse);
        updatedHouse
            .actionType(UPDATED_ACTION_TYPE)
            .address(UPDATED_ADDRESS)
            .money(UPDATED_MONEY)
            .moneyType(UPDATED_MONEY_TYPE)
            .acreage(UPDATED_ACREAGE)
            .discount(UPDATED_DISCOUNT)
            .direction(UPDATED_DIRECTION)
            .floor(UPDATED_FLOOR)
            .bathRoom(UPDATED_BATH_ROOM)
            .parking(UPDATED_PARKING)
            .bedRoom(UPDATED_BED_ROOM)
            .landType(UPDATED_LAND_TYPE)
            .saleType(UPDATED_SALE_TYPE)
            .fee(UPDATED_FEE)
            .feeMax(UPDATED_FEE_MAX)
            .hits(UPDATED_HITS)
            .statusType(UPDATED_STATUS_TYPE)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        HouseDTO houseDTO = houseMapper.toDto(updatedHouse);

        restHouseMockMvc.perform(put("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testHouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHouse.getMoney()).isEqualTo(UPDATED_MONEY);
        assertThat(testHouse.getMoneyType()).isEqualTo(UPDATED_MONEY_TYPE);
        assertThat(testHouse.getAcreage()).isEqualTo(UPDATED_ACREAGE);
        assertThat(testHouse.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testHouse.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testHouse.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testHouse.getBathRoom()).isEqualTo(UPDATED_BATH_ROOM);
        assertThat(testHouse.isParking()).isEqualTo(UPDATED_PARKING);
        assertThat(testHouse.getBedRoom()).isEqualTo(UPDATED_BED_ROOM);
        assertThat(testHouse.getLandType()).isEqualTo(UPDATED_LAND_TYPE);
        assertThat(testHouse.getSaleType()).isEqualTo(UPDATED_SALE_TYPE);
        assertThat(testHouse.getFee()).isEqualTo(UPDATED_FEE);
        assertThat(testHouse.getFeeMax()).isEqualTo(UPDATED_FEE_MAX);
        assertThat(testHouse.getHits()).isEqualTo(UPDATED_HITS);
        assertThat(testHouse.getStatusType()).isEqualTo(UPDATED_STATUS_TYPE);
        assertThat(testHouse.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testHouse.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the House in Elasticsearch
        verify(mockHouseSearchRepository, times(1)).save(testHouse);
    }

    @Test
    @Transactional
    public void updateNonExistingHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Create the House
        HouseDTO houseDTO = houseMapper.toDto(house);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHouseMockMvc.perform(put("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the House in Elasticsearch
        verify(mockHouseSearchRepository, times(0)).save(house);
    }

    @Test
    @Transactional
    public void deleteHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        int databaseSizeBeforeDelete = houseRepository.findAll().size();

        // Get the house
        restHouseMockMvc.perform(delete("/api/houses/{id}", house.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the House in Elasticsearch
        verify(mockHouseSearchRepository, times(1)).deleteById(house.getId());
    }

    @Test
    @Transactional
    public void searchHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        when(mockHouseSearchRepository.search(queryStringQuery("id:" + house.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(house), PageRequest.of(0, 1), 1));
        // Search the house
        restHouseMockMvc.perform(get("/api/_search/houses?query=id:" + house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].moneyType").value(hasItem(DEFAULT_MONEY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].acreage").value(hasItem(DEFAULT_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR.toString())))
            .andExpect(jsonPath("$.[*].bathRoom").value(hasItem(DEFAULT_BATH_ROOM)))
            .andExpect(jsonPath("$.[*].parking").value(hasItem(DEFAULT_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].bedRoom").value(hasItem(DEFAULT_BED_ROOM)))
            .andExpect(jsonPath("$.[*].landType").value(hasItem(DEFAULT_LAND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].saleType").value(hasItem(DEFAULT_SALE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].feeMax").value(hasItem(DEFAULT_FEE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].statusType").value(hasItem(DEFAULT_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(House.class);
        House house1 = new House();
        house1.setId(1L);
        House house2 = new House();
        house2.setId(house1.getId());
        assertThat(house1).isEqualTo(house2);
        house2.setId(2L);
        assertThat(house1).isNotEqualTo(house2);
        house1.setId(null);
        assertThat(house1).isNotEqualTo(house2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseDTO.class);
        HouseDTO houseDTO1 = new HouseDTO();
        houseDTO1.setId(1L);
        HouseDTO houseDTO2 = new HouseDTO();
        assertThat(houseDTO1).isNotEqualTo(houseDTO2);
        houseDTO2.setId(houseDTO1.getId());
        assertThat(houseDTO1).isEqualTo(houseDTO2);
        houseDTO2.setId(2L);
        assertThat(houseDTO1).isNotEqualTo(houseDTO2);
        houseDTO1.setId(null);
        assertThat(houseDTO1).isNotEqualTo(houseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(houseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(houseMapper.fromId(null)).isNull();
    }
}
