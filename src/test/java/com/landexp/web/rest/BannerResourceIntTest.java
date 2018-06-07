package com.landexp.web.rest;

import com.landexp.LandexpApp;

import com.landexp.domain.Banner;
import com.landexp.repository.BannerRepository;
import com.landexp.repository.search.BannerSearchRepository;
import com.landexp.service.BannerService;
import com.landexp.service.dto.BannerDTO;
import com.landexp.service.mapper.BannerMapper;
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
 * Test class for the BannerResource REST controller.
 *
 * @see BannerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LandexpApp.class)
public class BannerResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_ALIAS = "BBBBBBBBBB";

    private static final Integer DEFAULT_AREA = 1;
    private static final Integer UPDATED_AREA = 2;

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;

    private static final LocalDate DEFAULT_PUBLIC_UP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLIC_UP = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PUBLIC_DOWN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLIC_DOWN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BannerRepository bannerRepository;


    @Autowired
    private BannerMapper bannerMapper;
    

    @Autowired
    private BannerService bannerService;

    /**
     * This repository is mocked in the com.landexp.repository.search test package.
     *
     * @see com.landexp.repository.search.BannerSearchRepositoryMockConfiguration
     */
    @Autowired
    private BannerSearchRepository mockBannerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBannerMockMvc;

    private Banner banner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BannerResource bannerResource = new BannerResource(bannerService);
        this.restBannerMockMvc = MockMvcBuilders.standaloneSetup(bannerResource)
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
    public static Banner createEntity(EntityManager em) {
        Banner banner = new Banner()
            .title(DEFAULT_TITLE)
            .titleAlias(DEFAULT_TITLE_ALIAS)
            .area(DEFAULT_AREA)
            .hits(DEFAULT_HITS)
            .publicUp(DEFAULT_PUBLIC_UP)
            .publicDown(DEFAULT_PUBLIC_DOWN)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return banner;
    }

    @Before
    public void initTest() {
        banner = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanner() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);
        restBannerMockMvc.perform(post("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isCreated());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate + 1);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBanner.getTitleAlias()).isEqualTo(DEFAULT_TITLE_ALIAS);
        assertThat(testBanner.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testBanner.getHits()).isEqualTo(DEFAULT_HITS);
        assertThat(testBanner.getPublicUp()).isEqualTo(DEFAULT_PUBLIC_UP);
        assertThat(testBanner.getPublicDown()).isEqualTo(DEFAULT_PUBLIC_DOWN);
        assertThat(testBanner.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testBanner.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Banner in Elasticsearch
        verify(mockBannerSearchRepository, times(1)).save(testBanner);
    }

    @Test
    @Transactional
    public void createBannerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();

        // Create the Banner with an existing ID
        banner.setId(1L);
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerMockMvc.perform(post("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Banner in Elasticsearch
        verify(mockBannerSearchRepository, times(0)).save(banner);
    }

    @Test
    @Transactional
    public void getAllBanners() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList
        restBannerMockMvc.perform(get("/api/banners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].titleAlias").value(hasItem(DEFAULT_TITLE_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].publicUp").value(hasItem(DEFAULT_PUBLIC_UP.toString())))
            .andExpect(jsonPath("$.[*].publicDown").value(hasItem(DEFAULT_PUBLIC_DOWN.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get the banner
        restBannerMockMvc.perform(get("/api/banners/{id}", banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(banner.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.titleAlias").value(DEFAULT_TITLE_ALIAS.toString()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS))
            .andExpect(jsonPath("$.publicUp").value(DEFAULT_PUBLIC_UP.toString()))
            .andExpect(jsonPath("$.publicDown").value(DEFAULT_PUBLIC_DOWN.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBanner() throws Exception {
        // Get the banner
        restBannerMockMvc.perform(get("/api/banners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Update the banner
        Banner updatedBanner = bannerRepository.findById(banner.getId()).get();
        // Disconnect from session so that the updates on updatedBanner are not directly saved in db
        em.detach(updatedBanner);
        updatedBanner
            .title(UPDATED_TITLE)
            .titleAlias(UPDATED_TITLE_ALIAS)
            .area(UPDATED_AREA)
            .hits(UPDATED_HITS)
            .publicUp(UPDATED_PUBLIC_UP)
            .publicDown(UPDATED_PUBLIC_DOWN)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT);
        BannerDTO bannerDTO = bannerMapper.toDto(updatedBanner);

        restBannerMockMvc.perform(put("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isOk());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBanner.getTitleAlias()).isEqualTo(UPDATED_TITLE_ALIAS);
        assertThat(testBanner.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testBanner.getHits()).isEqualTo(UPDATED_HITS);
        assertThat(testBanner.getPublicUp()).isEqualTo(UPDATED_PUBLIC_UP);
        assertThat(testBanner.getPublicDown()).isEqualTo(UPDATED_PUBLIC_DOWN);
        assertThat(testBanner.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testBanner.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Banner in Elasticsearch
        verify(mockBannerSearchRepository, times(1)).save(testBanner);
    }

    @Test
    @Transactional
    public void updateNonExistingBanner() throws Exception {
        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBannerMockMvc.perform(put("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Banner in Elasticsearch
        verify(mockBannerSearchRepository, times(0)).save(banner);
    }

    @Test
    @Transactional
    public void deleteBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        int databaseSizeBeforeDelete = bannerRepository.findAll().size();

        // Get the banner
        restBannerMockMvc.perform(delete("/api/banners/{id}", banner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Banner in Elasticsearch
        verify(mockBannerSearchRepository, times(1)).deleteById(banner.getId());
    }

    @Test
    @Transactional
    public void searchBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);
        when(mockBannerSearchRepository.search(queryStringQuery("id:" + banner.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(banner), PageRequest.of(0, 1), 1));
        // Search the banner
        restBannerMockMvc.perform(get("/api/_search/banners?query=id:" + banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].titleAlias").value(hasItem(DEFAULT_TITLE_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].publicUp").value(hasItem(DEFAULT_PUBLIC_UP.toString())))
            .andExpect(jsonPath("$.[*].publicDown").value(hasItem(DEFAULT_PUBLIC_DOWN.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banner.class);
        Banner banner1 = new Banner();
        banner1.setId(1L);
        Banner banner2 = new Banner();
        banner2.setId(banner1.getId());
        assertThat(banner1).isEqualTo(banner2);
        banner2.setId(2L);
        assertThat(banner1).isNotEqualTo(banner2);
        banner1.setId(null);
        assertThat(banner1).isNotEqualTo(banner2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BannerDTO.class);
        BannerDTO bannerDTO1 = new BannerDTO();
        bannerDTO1.setId(1L);
        BannerDTO bannerDTO2 = new BannerDTO();
        assertThat(bannerDTO1).isNotEqualTo(bannerDTO2);
        bannerDTO2.setId(bannerDTO1.getId());
        assertThat(bannerDTO1).isEqualTo(bannerDTO2);
        bannerDTO2.setId(2L);
        assertThat(bannerDTO1).isNotEqualTo(bannerDTO2);
        bannerDTO1.setId(null);
        assertThat(bannerDTO1).isNotEqualTo(bannerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bannerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bannerMapper.fromId(null)).isNull();
    }
}
