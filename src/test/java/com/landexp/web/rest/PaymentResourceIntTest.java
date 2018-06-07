package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.Payment;
import com.landexp.domain.House;
import com.landexp.domain.User;
import com.landexp.domain.User;
import com.landexp.domain.User;
import com.landexp.repository.PaymentRepository;
import com.landexp.repository.search.PaymentSearchRepository;
import com.landexp.service.PaymentService;
import com.landexp.service.dto.PaymentDTO;
import com.landexp.service.mapper.PaymentMapper;
import com.landexp.web.rest.errors.ExceptionTranslator;
import com.landexp.service.dto.PaymentCriteria;
import com.landexp.service.PaymentQueryService;

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

import com.landexp.domain.enumeration.PaymentStatusType;
/**
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class PaymentResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_MONEY = 1F;
    private static final Float UPDATED_MONEY = 2F;

    private static final LocalDate DEFAULT_PAID_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAID_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final PaymentStatusType DEFAULT_PAYMENT_STATUS = PaymentStatusType.OPEN;
    private static final PaymentStatusType UPDATED_PAYMENT_STATUS = PaymentStatusType.PENDING;

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private PaymentMapper paymentMapper;
    

    @Autowired
    private PaymentService paymentService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.PaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentSearchRepository mockPaymentSearchRepository;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentResource paymentResource = new PaymentResource(paymentService, paymentQueryService);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
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
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .code(DEFAULT_CODE)
            .money(DEFAULT_MONEY)
            .paidTime(DEFAULT_PAID_TIME)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return payment;
    }

    @Before
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPayment.getMoney()).isEqualTo(DEFAULT_MONEY);
        assertThat(testPayment.getPaidTime()).isEqualTo(DEFAULT_PAID_TIME);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPayment.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).save(testPayment);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].paidTime").value(hasItem(DEFAULT_PAID_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.money").value(DEFAULT_MONEY.doubleValue()))
            .andExpect(jsonPath("$.paidTime").value(DEFAULT_PAID_TIME.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllPaymentsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where code equals to DEFAULT_CODE
        defaultPaymentShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the paymentList where code equals to UPDATED_CODE
        defaultPaymentShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPaymentShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the paymentList where code equals to UPDATED_CODE
        defaultPaymentShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where code is not null
        defaultPaymentShouldBeFound("code.specified=true");

        // Get all the paymentList where code is null
        defaultPaymentShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByMoneyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where money equals to DEFAULT_MONEY
        defaultPaymentShouldBeFound("money.equals=" + DEFAULT_MONEY);

        // Get all the paymentList where money equals to UPDATED_MONEY
        defaultPaymentShouldNotBeFound("money.equals=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByMoneyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where money in DEFAULT_MONEY or UPDATED_MONEY
        defaultPaymentShouldBeFound("money.in=" + DEFAULT_MONEY + "," + UPDATED_MONEY);

        // Get all the paymentList where money equals to UPDATED_MONEY
        defaultPaymentShouldNotBeFound("money.in=" + UPDATED_MONEY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByMoneyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where money is not null
        defaultPaymentShouldBeFound("money.specified=true");

        // Get all the paymentList where money is null
        defaultPaymentShouldNotBeFound("money.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidTime equals to DEFAULT_PAID_TIME
        defaultPaymentShouldBeFound("paidTime.equals=" + DEFAULT_PAID_TIME);

        // Get all the paymentList where paidTime equals to UPDATED_PAID_TIME
        defaultPaymentShouldNotBeFound("paidTime.equals=" + UPDATED_PAID_TIME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidTimeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidTime in DEFAULT_PAID_TIME or UPDATED_PAID_TIME
        defaultPaymentShouldBeFound("paidTime.in=" + DEFAULT_PAID_TIME + "," + UPDATED_PAID_TIME);

        // Get all the paymentList where paidTime equals to UPDATED_PAID_TIME
        defaultPaymentShouldNotBeFound("paidTime.in=" + UPDATED_PAID_TIME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidTime is not null
        defaultPaymentShouldBeFound("paidTime.specified=true");

        // Get all the paymentList where paidTime is null
        defaultPaymentShouldNotBeFound("paidTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidTime greater than or equals to DEFAULT_PAID_TIME
        defaultPaymentShouldBeFound("paidTime.greaterOrEqualThan=" + DEFAULT_PAID_TIME);

        // Get all the paymentList where paidTime greater than or equals to UPDATED_PAID_TIME
        defaultPaymentShouldNotBeFound("paidTime.greaterOrEqualThan=" + UPDATED_PAID_TIME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidTime less than or equals to DEFAULT_PAID_TIME
        defaultPaymentShouldNotBeFound("paidTime.lessThan=" + DEFAULT_PAID_TIME);

        // Get all the paymentList where paidTime less than or equals to UPDATED_PAID_TIME
        defaultPaymentShouldBeFound("paidTime.lessThan=" + UPDATED_PAID_TIME);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus is not null
        defaultPaymentShouldBeFound("paymentStatus.specified=true");

        // Get all the paymentList where paymentStatus is null
        defaultPaymentShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createAt equals to DEFAULT_CREATE_AT
        defaultPaymentShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the paymentList where createAt equals to UPDATED_CREATE_AT
        defaultPaymentShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultPaymentShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the paymentList where createAt equals to UPDATED_CREATE_AT
        defaultPaymentShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createAt is not null
        defaultPaymentShouldBeFound("createAt.specified=true");

        // Get all the paymentList where createAt is null
        defaultPaymentShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createAt greater than or equals to DEFAULT_CREATE_AT
        defaultPaymentShouldBeFound("createAt.greaterOrEqualThan=" + DEFAULT_CREATE_AT);

        // Get all the paymentList where createAt greater than or equals to UPDATED_CREATE_AT
        defaultPaymentShouldNotBeFound("createAt.greaterOrEqualThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createAt less than or equals to DEFAULT_CREATE_AT
        defaultPaymentShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the paymentList where createAt less than or equals to UPDATED_CREATE_AT
        defaultPaymentShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }


    @Test
    @Transactional
    public void getAllPaymentsByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updateAt equals to DEFAULT_UPDATE_AT
        defaultPaymentShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the paymentList where updateAt equals to UPDATED_UPDATE_AT
        defaultPaymentShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultPaymentShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the paymentList where updateAt equals to UPDATED_UPDATE_AT
        defaultPaymentShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updateAt is not null
        defaultPaymentShouldBeFound("updateAt.specified=true");

        // Get all the paymentList where updateAt is null
        defaultPaymentShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updateAt greater than or equals to DEFAULT_UPDATE_AT
        defaultPaymentShouldBeFound("updateAt.greaterOrEqualThan=" + DEFAULT_UPDATE_AT);

        // Get all the paymentList where updateAt greater than or equals to UPDATED_UPDATE_AT
        defaultPaymentShouldNotBeFound("updateAt.greaterOrEqualThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updateAt less than or equals to DEFAULT_UPDATE_AT
        defaultPaymentShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the paymentList where updateAt less than or equals to UPDATED_UPDATE_AT
        defaultPaymentShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }


    @Test
    @Transactional
    public void getAllPaymentsByHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        House house = HouseResourceIntTest.createEntity(em);
        em.persist(house);
        em.flush();
        payment.setHouse(house);
        paymentRepository.saveAndFlush(payment);
        Long houseId = house.getId();

        // Get all the paymentList where house equals to houseId
        defaultPaymentShouldBeFound("houseId.equals=" + houseId);

        // Get all the paymentList where house equals to houseId + 1
        defaultPaymentShouldNotBeFound("houseId.equals=" + (houseId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        User customer = UserResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        payment.setCustomer(customer);
        paymentRepository.saveAndFlush(payment);
        Long customerId = customer.getId();

        // Get all the paymentList where customer equals to customerId
        defaultPaymentShouldBeFound("customerId.equals=" + customerId);

        // Get all the paymentList where customer equals to customerId + 1
        defaultPaymentShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User createBy = UserResourceIntTest.createEntity(em);
        em.persist(createBy);
        em.flush();
        payment.setCreateBy(createBy);
        paymentRepository.saveAndFlush(payment);
        Long createById = createBy.getId();

        // Get all the paymentList where createBy equals to createById
        defaultPaymentShouldBeFound("createById.equals=" + createById);

        // Get all the paymentList where createBy equals to createById + 1
        defaultPaymentShouldNotBeFound("createById.equals=" + (createById + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByUpdateByIsEqualToSomething() throws Exception {
        // Initialize the database
        User updateBy = UserResourceIntTest.createEntity(em);
        em.persist(updateBy);
        em.flush();
        payment.setUpdateBy(updateBy);
        paymentRepository.saveAndFlush(payment);
        Long updateById = updateBy.getId();

        // Get all the paymentList where updateBy equals to updateById
        defaultPaymentShouldBeFound("updateById.equals=" + updateById);

        // Get all the paymentList where updateBy equals to updateById + 1
        defaultPaymentShouldNotBeFound("updateById.equals=" + (updateById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].paidTime").value(hasItem(DEFAULT_PAID_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .code(UPDATED_CODE)
            .money(UPDATED_MONEY)
            .paidTime(UPDATED_PAID_TIME)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPayment.getMoney()).isEqualTo(UPDATED_MONEY);
        assertThat(testPayment.getPaidTime()).isEqualTo(UPDATED_PAID_TIME);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPayment.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).save(testPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Get the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).deleteById(payment.getId());
    }

    @Test
    @Transactional
    public void searchPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        when(mockPaymentSearchRepository.search(queryStringQuery("id:" + payment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payment), PageRequest.of(0, 1), 1));
        // Search the payment
        restPaymentMockMvc.perform(get("/api/_search/payments?query=id:" + payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].money").value(hasItem(DEFAULT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].paidTime").value(hasItem(DEFAULT_PAID_TIME.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = new Payment();
        payment1.setId(1L);
        Payment payment2 = new Payment();
        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);
        payment2.setId(2L);
        assertThat(payment1).isNotEqualTo(payment2);
        payment1.setId(null);
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setId(1L);
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setId(paymentDTO1.getId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setId(2L);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMapper.fromId(null)).isNull();
    }
}
