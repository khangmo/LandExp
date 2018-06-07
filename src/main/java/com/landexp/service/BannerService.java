package com.landexp.service;

import com.landexp.domain.Banner;
import com.landexp.repository.BannerRepository;
import com.landexp.repository.search.BannerSearchRepository;
import com.landexp.service.dto.BannerDTO;
import com.landexp.service.mapper.BannerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Banner.
 */
@Service
@Transactional
public class BannerService {

    private final Logger log = LoggerFactory.getLogger(BannerService.class);

    private final BannerRepository bannerRepository;

    private final BannerMapper bannerMapper;

    private final BannerSearchRepository bannerSearchRepository;

    public BannerService(BannerRepository bannerRepository, BannerMapper bannerMapper, BannerSearchRepository bannerSearchRepository) {
        this.bannerRepository = bannerRepository;
        this.bannerMapper = bannerMapper;
        this.bannerSearchRepository = bannerSearchRepository;
    }

    /**
     * Save a banner.
     *
     * @param bannerDTO the entity to save
     * @return the persisted entity
     */
    public BannerDTO save(BannerDTO bannerDTO) {
        log.debug("Request to save Banner : {}", bannerDTO);
        Banner banner = bannerMapper.toEntity(bannerDTO);
        banner = bannerRepository.save(banner);
        BannerDTO result = bannerMapper.toDto(banner);
        bannerSearchRepository.save(banner);
        return result;
    }

    /**
     * Get all the banners.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BannerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banners");
        return bannerRepository.findAll(pageable)
            .map(bannerMapper::toDto);
    }


    /**
     * Get one banner by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BannerDTO> findOne(Long id) {
        log.debug("Request to get Banner : {}", id);
        return bannerRepository.findById(id)
            .map(bannerMapper::toDto);
    }

    /**
     * Delete the banner by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Banner : {}", id);
        bannerRepository.deleteById(id);
        bannerSearchRepository.deleteById(id);
    }

    /**
     * Search for the banner corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BannerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Banners for query {}", query);
        return bannerSearchRepository.search(queryStringQuery(query), pageable)
            .map(bannerMapper::toDto);
    }
}
