package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.UserFinancial;
import com.landexp.domain.User;
import com.landexp.repository.UserFinancialRepository;
import com.landexp.repository.search.UserFinancialSearchRepository;
import com.landexp.service.UserFinancialService;
import com.landexp.service.dto.UserFinancialDTO;
import com.landexp.service.mapper.UserFinancialMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.UserFinancialCriteria;
import com.landexp.service.UserFinancialQueryService;

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
 * Test class for the UserFinancialResource REST controller.
 *
 * @see UserFinancialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class UserFinancialResourceIntTest {

    private static final Float DEFAULT_HOUSE_PRICE = 1F;
    private static final Float UPDATED_HOUSE_PRICE = 2F;

    private static final Float DEFAULT_HOUSE_ACREAGE = 1F;
    private static final Float UPDATED_HOUSE_ACREAGE = 2F;

    private static final Float DEFAULT_LOAN_RATE = 1F;
    private static final Float UPDATED_LOAN_RATE = 2F;

    private static final Float DEFAULT_LOAN_FROM_PEOPLE = 1F;
    private static final Float UPDATED_LOAN_FROM_PEOPLE = 2F;

    private static final Float DEFAULT_CUSTOMER_MONEY_HAVE = 1F;
    private static final Float UPDATED_CUSTOMER_MONEY_HAVE = 2F;

    private static final String DEFAULT_CUSTOMER_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_EMAIL = "BBBBBBBBBB";

    @Autowired
    private UserFinancialRepository userFinancialRepository;


    @Autowired
    private UserFinancialMapper userFinancialMapper;
    

    @Autowired
    private UserFinancialService userFinancialService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.UserFinancialSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserFinancialSearchRepository mockUserFinancialSearchRepository;

    @Autowired
    private UserFinancialQueryService userFinancialQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserFinancialMockMvc;

    private UserFinancial userFinancial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserFinancialResource userFinancialResource = new UserFinancialResource(userFinancialService, userFinancialQueryService);
        this.restUserFinancialMockMvc = MockMvcBuilders.standaloneSetup(userFinancialResource)
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
    public static UserFinancial createEntity(EntityManager em) {
        UserFinancial userFinancial = new UserFinancial()
            .housePrice(DEFAULT_HOUSE_PRICE)
            .houseAcreage(DEFAULT_HOUSE_ACREAGE)
            .loanRate(DEFAULT_LOAN_RATE)
            .loanFromPeople(DEFAULT_LOAN_FROM_PEOPLE)
            .customerMoneyHave(DEFAULT_CUSTOMER_MONEY_HAVE)
            .customerMobile(DEFAULT_CUSTOMER_MOBILE)
            .customerEmail(DEFAULT_CUSTOMER_EMAIL);
        return userFinancial;
    }

    @Before
    public void initTest() {
        userFinancial = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserFinancial() throws Exception {
        int databaseSizeBeforeCreate = userFinancialRepository.findAll().size();

        // Create the UserFinancial
        UserFinancialDTO userFinancialDTO = userFinancialMapper.toDto(userFinancial);
        restUserFinancialMockMvc.perform(post("/api/user-financials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFinancialDTO)))
            .andExpect(status().isCreated());

        // Validate the UserFinancial in the database
        List<UserFinancial> userFinancialList = userFinancialRepository.findAll();
        assertThat(userFinancialList).hasSize(databaseSizeBeforeCreate + 1);
        UserFinancial testUserFinancial = userFinancialList.get(userFinancialList.size() - 1);
        assertThat(testUserFinancial.getHousePrice()).isEqualTo(DEFAULT_HOUSE_PRICE);
        assertThat(testUserFinancial.getHouseAcreage()).isEqualTo(DEFAULT_HOUSE_ACREAGE);
        assertThat(testUserFinancial.getLoanRate()).isEqualTo(DEFAULT_LOAN_RATE);
        assertThat(testUserFinancial.getLoanFromPeople()).isEqualTo(DEFAULT_LOAN_FROM_PEOPLE);
        assertThat(testUserFinancial.getCustomerMoneyHave()).isEqualTo(DEFAULT_CUSTOMER_MONEY_HAVE);
        assertThat(testUserFinancial.getCustomerMobile()).isEqualTo(DEFAULT_CUSTOMER_MOBILE);
        assertThat(testUserFinancial.getCustomerEmail()).isEqualTo(DEFAULT_CUSTOMER_EMAIL);

        // Validate the UserFinancial in Elasticsearch
        verify(mockUserFinancialSearchRepository, times(1)).save(testUserFinancial);
    }

    @Test
    @Transactional
    public void createUserFinancialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userFinancialRepository.findAll().size();

        // Create the UserFinancial with an existing ID
        userFinancial.setId(1L);
        UserFinancialDTO userFinancialDTO = userFinancialMapper.toDto(userFinancial);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFinancialMockMvc.perform(post("/api/user-financials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFinancialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserFinancial in the database
        List<UserFinancial> userFinancialList = userFinancialRepository.findAll();
        assertThat(userFinancialList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserFinancial in Elasticsearch
        verify(mockUserFinancialSearchRepository, times(0)).save(userFinancial);
    }

    @Test
    @Transactional
    public void getAllUserFinancials() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList
        restUserFinancialMockMvc.perform(get("/api/user-financials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFinancial.getId().intValue())))
            .andExpect(jsonPath("$.[*].housePrice").value(hasItem(DEFAULT_HOUSE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].houseAcreage").value(hasItem(DEFAULT_HOUSE_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanRate").value(hasItem(DEFAULT_LOAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanFromPeople").value(hasItem(DEFAULT_LOAN_FROM_PEOPLE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMoneyHave").value(hasItem(DEFAULT_CUSTOMER_MONEY_HAVE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMobile").value(hasItem(DEFAULT_CUSTOMER_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL.toString())));
    }
    

    @Test
    @Transactional
    public void getUserFinancial() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get the userFinancial
        restUserFinancialMockMvc.perform(get("/api/user-financials/{id}", userFinancial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userFinancial.getId().intValue()))
            .andExpect(jsonPath("$.housePrice").value(DEFAULT_HOUSE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.houseAcreage").value(DEFAULT_HOUSE_ACREAGE.doubleValue()))
            .andExpect(jsonPath("$.loanRate").value(DEFAULT_LOAN_RATE.doubleValue()))
            .andExpect(jsonPath("$.loanFromPeople").value(DEFAULT_LOAN_FROM_PEOPLE.doubleValue()))
            .andExpect(jsonPath("$.customerMoneyHave").value(DEFAULT_CUSTOMER_MONEY_HAVE.doubleValue()))
            .andExpect(jsonPath("$.customerMobile").value(DEFAULT_CUSTOMER_MOBILE.toString()))
            .andExpect(jsonPath("$.customerEmail").value(DEFAULT_CUSTOMER_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHousePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where housePrice equals to DEFAULT_HOUSE_PRICE
        defaultUserFinancialShouldBeFound("housePrice.equals=" + DEFAULT_HOUSE_PRICE);

        // Get all the userFinancialList where housePrice equals to UPDATED_HOUSE_PRICE
        defaultUserFinancialShouldNotBeFound("housePrice.equals=" + UPDATED_HOUSE_PRICE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHousePriceIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where housePrice in DEFAULT_HOUSE_PRICE or UPDATED_HOUSE_PRICE
        defaultUserFinancialShouldBeFound("housePrice.in=" + DEFAULT_HOUSE_PRICE + "," + UPDATED_HOUSE_PRICE);

        // Get all the userFinancialList where housePrice equals to UPDATED_HOUSE_PRICE
        defaultUserFinancialShouldNotBeFound("housePrice.in=" + UPDATED_HOUSE_PRICE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHousePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where housePrice is not null
        defaultUserFinancialShouldBeFound("housePrice.specified=true");

        // Get all the userFinancialList where housePrice is null
        defaultUserFinancialShouldNotBeFound("housePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHouseAcreageIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where houseAcreage equals to DEFAULT_HOUSE_ACREAGE
        defaultUserFinancialShouldBeFound("houseAcreage.equals=" + DEFAULT_HOUSE_ACREAGE);

        // Get all the userFinancialList where houseAcreage equals to UPDATED_HOUSE_ACREAGE
        defaultUserFinancialShouldNotBeFound("houseAcreage.equals=" + UPDATED_HOUSE_ACREAGE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHouseAcreageIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where houseAcreage in DEFAULT_HOUSE_ACREAGE or UPDATED_HOUSE_ACREAGE
        defaultUserFinancialShouldBeFound("houseAcreage.in=" + DEFAULT_HOUSE_ACREAGE + "," + UPDATED_HOUSE_ACREAGE);

        // Get all the userFinancialList where houseAcreage equals to UPDATED_HOUSE_ACREAGE
        defaultUserFinancialShouldNotBeFound("houseAcreage.in=" + UPDATED_HOUSE_ACREAGE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByHouseAcreageIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where houseAcreage is not null
        defaultUserFinancialShouldBeFound("houseAcreage.specified=true");

        // Get all the userFinancialList where houseAcreage is null
        defaultUserFinancialShouldNotBeFound("houseAcreage.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanRateIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanRate equals to DEFAULT_LOAN_RATE
        defaultUserFinancialShouldBeFound("loanRate.equals=" + DEFAULT_LOAN_RATE);

        // Get all the userFinancialList where loanRate equals to UPDATED_LOAN_RATE
        defaultUserFinancialShouldNotBeFound("loanRate.equals=" + UPDATED_LOAN_RATE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanRateIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanRate in DEFAULT_LOAN_RATE or UPDATED_LOAN_RATE
        defaultUserFinancialShouldBeFound("loanRate.in=" + DEFAULT_LOAN_RATE + "," + UPDATED_LOAN_RATE);

        // Get all the userFinancialList where loanRate equals to UPDATED_LOAN_RATE
        defaultUserFinancialShouldNotBeFound("loanRate.in=" + UPDATED_LOAN_RATE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanRate is not null
        defaultUserFinancialShouldBeFound("loanRate.specified=true");

        // Get all the userFinancialList where loanRate is null
        defaultUserFinancialShouldNotBeFound("loanRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanFromPeopleIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanFromPeople equals to DEFAULT_LOAN_FROM_PEOPLE
        defaultUserFinancialShouldBeFound("loanFromPeople.equals=" + DEFAULT_LOAN_FROM_PEOPLE);

        // Get all the userFinancialList where loanFromPeople equals to UPDATED_LOAN_FROM_PEOPLE
        defaultUserFinancialShouldNotBeFound("loanFromPeople.equals=" + UPDATED_LOAN_FROM_PEOPLE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanFromPeopleIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanFromPeople in DEFAULT_LOAN_FROM_PEOPLE or UPDATED_LOAN_FROM_PEOPLE
        defaultUserFinancialShouldBeFound("loanFromPeople.in=" + DEFAULT_LOAN_FROM_PEOPLE + "," + UPDATED_LOAN_FROM_PEOPLE);

        // Get all the userFinancialList where loanFromPeople equals to UPDATED_LOAN_FROM_PEOPLE
        defaultUserFinancialShouldNotBeFound("loanFromPeople.in=" + UPDATED_LOAN_FROM_PEOPLE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByLoanFromPeopleIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where loanFromPeople is not null
        defaultUserFinancialShouldBeFound("loanFromPeople.specified=true");

        // Get all the userFinancialList where loanFromPeople is null
        defaultUserFinancialShouldNotBeFound("loanFromPeople.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMoneyHaveIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMoneyHave equals to DEFAULT_CUSTOMER_MONEY_HAVE
        defaultUserFinancialShouldBeFound("customerMoneyHave.equals=" + DEFAULT_CUSTOMER_MONEY_HAVE);

        // Get all the userFinancialList where customerMoneyHave equals to UPDATED_CUSTOMER_MONEY_HAVE
        defaultUserFinancialShouldNotBeFound("customerMoneyHave.equals=" + UPDATED_CUSTOMER_MONEY_HAVE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMoneyHaveIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMoneyHave in DEFAULT_CUSTOMER_MONEY_HAVE or UPDATED_CUSTOMER_MONEY_HAVE
        defaultUserFinancialShouldBeFound("customerMoneyHave.in=" + DEFAULT_CUSTOMER_MONEY_HAVE + "," + UPDATED_CUSTOMER_MONEY_HAVE);

        // Get all the userFinancialList where customerMoneyHave equals to UPDATED_CUSTOMER_MONEY_HAVE
        defaultUserFinancialShouldNotBeFound("customerMoneyHave.in=" + UPDATED_CUSTOMER_MONEY_HAVE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMoneyHaveIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMoneyHave is not null
        defaultUserFinancialShouldBeFound("customerMoneyHave.specified=true");

        // Get all the userFinancialList where customerMoneyHave is null
        defaultUserFinancialShouldNotBeFound("customerMoneyHave.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMobile equals to DEFAULT_CUSTOMER_MOBILE
        defaultUserFinancialShouldBeFound("customerMobile.equals=" + DEFAULT_CUSTOMER_MOBILE);

        // Get all the userFinancialList where customerMobile equals to UPDATED_CUSTOMER_MOBILE
        defaultUserFinancialShouldNotBeFound("customerMobile.equals=" + UPDATED_CUSTOMER_MOBILE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMobileIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMobile in DEFAULT_CUSTOMER_MOBILE or UPDATED_CUSTOMER_MOBILE
        defaultUserFinancialShouldBeFound("customerMobile.in=" + DEFAULT_CUSTOMER_MOBILE + "," + UPDATED_CUSTOMER_MOBILE);

        // Get all the userFinancialList where customerMobile equals to UPDATED_CUSTOMER_MOBILE
        defaultUserFinancialShouldNotBeFound("customerMobile.in=" + UPDATED_CUSTOMER_MOBILE);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerMobile is not null
        defaultUserFinancialShouldBeFound("customerMobile.specified=true");

        // Get all the userFinancialList where customerMobile is null
        defaultUserFinancialShouldNotBeFound("customerMobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerEmail equals to DEFAULT_CUSTOMER_EMAIL
        defaultUserFinancialShouldBeFound("customerEmail.equals=" + DEFAULT_CUSTOMER_EMAIL);

        // Get all the userFinancialList where customerEmail equals to UPDATED_CUSTOMER_EMAIL
        defaultUserFinancialShouldNotBeFound("customerEmail.equals=" + UPDATED_CUSTOMER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerEmail in DEFAULT_CUSTOMER_EMAIL or UPDATED_CUSTOMER_EMAIL
        defaultUserFinancialShouldBeFound("customerEmail.in=" + DEFAULT_CUSTOMER_EMAIL + "," + UPDATED_CUSTOMER_EMAIL);

        // Get all the userFinancialList where customerEmail equals to UPDATED_CUSTOMER_EMAIL
        defaultUserFinancialShouldNotBeFound("customerEmail.in=" + UPDATED_CUSTOMER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByCustomerEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        // Get all the userFinancialList where customerEmail is not null
        defaultUserFinancialShouldBeFound("customerEmail.specified=true");

        // Get all the userFinancialList where customerEmail is null
        defaultUserFinancialShouldNotBeFound("customerEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserFinancialsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userFinancial.setUser(user);
        userFinancialRepository.saveAndFlush(userFinancial);
        Long userId = user.getId();

        // Get all the userFinancialList where user equals to userId
        defaultUserFinancialShouldBeFound("userId.equals=" + userId);

        // Get all the userFinancialList where user equals to userId + 1
        defaultUserFinancialShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultUserFinancialShouldBeFound(String filter) throws Exception {
        restUserFinancialMockMvc.perform(get("/api/user-financials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFinancial.getId().intValue())))
            .andExpect(jsonPath("$.[*].housePrice").value(hasItem(DEFAULT_HOUSE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].houseAcreage").value(hasItem(DEFAULT_HOUSE_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanRate").value(hasItem(DEFAULT_LOAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanFromPeople").value(hasItem(DEFAULT_LOAN_FROM_PEOPLE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMoneyHave").value(hasItem(DEFAULT_CUSTOMER_MONEY_HAVE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMobile").value(hasItem(DEFAULT_CUSTOMER_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultUserFinancialShouldNotBeFound(String filter) throws Exception {
        restUserFinancialMockMvc.perform(get("/api/user-financials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingUserFinancial() throws Exception {
        // Get the userFinancial
        restUserFinancialMockMvc.perform(get("/api/user-financials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFinancial() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        int databaseSizeBeforeUpdate = userFinancialRepository.findAll().size();

        // Update the userFinancial
        UserFinancial updatedUserFinancial = userFinancialRepository.findById(userFinancial.getId()).get();
        // Disconnect from session so that the updates on updatedUserFinancial are not directly saved in db
        em.detach(updatedUserFinancial);
        updatedUserFinancial
            .housePrice(UPDATED_HOUSE_PRICE)
            .houseAcreage(UPDATED_HOUSE_ACREAGE)
            .loanRate(UPDATED_LOAN_RATE)
            .loanFromPeople(UPDATED_LOAN_FROM_PEOPLE)
            .customerMoneyHave(UPDATED_CUSTOMER_MONEY_HAVE)
            .customerMobile(UPDATED_CUSTOMER_MOBILE)
            .customerEmail(UPDATED_CUSTOMER_EMAIL);
        UserFinancialDTO userFinancialDTO = userFinancialMapper.toDto(updatedUserFinancial);

        restUserFinancialMockMvc.perform(put("/api/user-financials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFinancialDTO)))
            .andExpect(status().isOk());

        // Validate the UserFinancial in the database
        List<UserFinancial> userFinancialList = userFinancialRepository.findAll();
        assertThat(userFinancialList).hasSize(databaseSizeBeforeUpdate);
        UserFinancial testUserFinancial = userFinancialList.get(userFinancialList.size() - 1);
        assertThat(testUserFinancial.getHousePrice()).isEqualTo(UPDATED_HOUSE_PRICE);
        assertThat(testUserFinancial.getHouseAcreage()).isEqualTo(UPDATED_HOUSE_ACREAGE);
        assertThat(testUserFinancial.getLoanRate()).isEqualTo(UPDATED_LOAN_RATE);
        assertThat(testUserFinancial.getLoanFromPeople()).isEqualTo(UPDATED_LOAN_FROM_PEOPLE);
        assertThat(testUserFinancial.getCustomerMoneyHave()).isEqualTo(UPDATED_CUSTOMER_MONEY_HAVE);
        assertThat(testUserFinancial.getCustomerMobile()).isEqualTo(UPDATED_CUSTOMER_MOBILE);
        assertThat(testUserFinancial.getCustomerEmail()).isEqualTo(UPDATED_CUSTOMER_EMAIL);

        // Validate the UserFinancial in Elasticsearch
        verify(mockUserFinancialSearchRepository, times(1)).save(testUserFinancial);
    }

    @Test
    @Transactional
    public void updateNonExistingUserFinancial() throws Exception {
        int databaseSizeBeforeUpdate = userFinancialRepository.findAll().size();

        // Create the UserFinancial
        UserFinancialDTO userFinancialDTO = userFinancialMapper.toDto(userFinancial);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserFinancialMockMvc.perform(put("/api/user-financials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFinancialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserFinancial in the database
        List<UserFinancial> userFinancialList = userFinancialRepository.findAll();
        assertThat(userFinancialList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserFinancial in Elasticsearch
        verify(mockUserFinancialSearchRepository, times(0)).save(userFinancial);
    }

    @Test
    @Transactional
    public void deleteUserFinancial() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);

        int databaseSizeBeforeDelete = userFinancialRepository.findAll().size();

        // Get the userFinancial
        restUserFinancialMockMvc.perform(delete("/api/user-financials/{id}", userFinancial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFinancial> userFinancialList = userFinancialRepository.findAll();
        assertThat(userFinancialList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserFinancial in Elasticsearch
        verify(mockUserFinancialSearchRepository, times(1)).deleteById(userFinancial.getId());
    }

    @Test
    @Transactional
    public void searchUserFinancial() throws Exception {
        // Initialize the database
        userFinancialRepository.saveAndFlush(userFinancial);
        when(mockUserFinancialSearchRepository.search(queryStringQuery("id:" + userFinancial.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userFinancial), PageRequest.of(0, 1), 1));
        // Search the userFinancial
        restUserFinancialMockMvc.perform(get("/api/_search/user-financials?query=id:" + userFinancial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFinancial.getId().intValue())))
            .andExpect(jsonPath("$.[*].housePrice").value(hasItem(DEFAULT_HOUSE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].houseAcreage").value(hasItem(DEFAULT_HOUSE_ACREAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanRate").value(hasItem(DEFAULT_LOAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanFromPeople").value(hasItem(DEFAULT_LOAN_FROM_PEOPLE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMoneyHave").value(hasItem(DEFAULT_CUSTOMER_MONEY_HAVE.doubleValue())))
            .andExpect(jsonPath("$.[*].customerMobile").value(hasItem(DEFAULT_CUSTOMER_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFinancial.class);
        UserFinancial userFinancial1 = new UserFinancial();
        userFinancial1.setId(1L);
        UserFinancial userFinancial2 = new UserFinancial();
        userFinancial2.setId(userFinancial1.getId());
        assertThat(userFinancial1).isEqualTo(userFinancial2);
        userFinancial2.setId(2L);
        assertThat(userFinancial1).isNotEqualTo(userFinancial2);
        userFinancial1.setId(null);
        assertThat(userFinancial1).isNotEqualTo(userFinancial2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFinancialDTO.class);
        UserFinancialDTO userFinancialDTO1 = new UserFinancialDTO();
        userFinancialDTO1.setId(1L);
        UserFinancialDTO userFinancialDTO2 = new UserFinancialDTO();
        assertThat(userFinancialDTO1).isNotEqualTo(userFinancialDTO2);
        userFinancialDTO2.setId(userFinancialDTO1.getId());
        assertThat(userFinancialDTO1).isEqualTo(userFinancialDTO2);
        userFinancialDTO2.setId(2L);
        assertThat(userFinancialDTO1).isNotEqualTo(userFinancialDTO2);
        userFinancialDTO1.setId(null);
        assertThat(userFinancialDTO1).isNotEqualTo(userFinancialDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userFinancialMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userFinancialMapper.fromId(null)).isNull();
    }
}
