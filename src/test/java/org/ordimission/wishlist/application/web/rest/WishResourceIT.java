package org.ordimission.wishlist.application.web.rest;

import org.ordimission.wishlist.application.WishListApplicationApp;
import org.ordimission.wishlist.application.domain.Wish;
import org.ordimission.wishlist.application.repository.WishRepository;
import org.ordimission.wishlist.application.repository.search.WishSearchRepository;
import org.ordimission.wishlist.application.service.WishService;
import org.ordimission.wishlist.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.ordimission.wishlist.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WishResource} REST controller.
 */
@SpringBootTest(classes = WishListApplicationApp.class)
public class WishResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;
    private static final Double SMALLER_UNIT_PRICE = 1D - 1D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private WishService wishService;

    /**
     * This repository is mocked in the org.ordimission.wishlist.application.repository.search test package.
     *
     * @see org.ordimission.wishlist.application.repository.search.WishSearchRepositoryMockConfiguration
     */
    @Autowired
    private WishSearchRepository mockWishSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWishMockMvc;

    private Wish wish;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WishResource wishResource = new WishResource(wishService);
        this.restWishMockMvc = MockMvcBuilders.standaloneSetup(wishResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wish createEntity(EntityManager em) {
        Wish wish = new Wish()
            .description(DEFAULT_DESCRIPTION)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .unit(DEFAULT_UNIT)
            .model(DEFAULT_MODEL)
            .brand(DEFAULT_BRAND);
        return wish;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wish createUpdatedEntity(EntityManager em) {
        Wish wish = new Wish()
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND);
        return wish;
    }

    @BeforeEach
    public void initTest() {
        wish = createEntity(em);
    }

    @Test
    @Transactional
    public void createWish() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // Create the Wish
        restWishMockMvc.perform(post("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isCreated());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate + 1);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWish.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testWish.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWish.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testWish.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testWish.getBrand()).isEqualTo(DEFAULT_BRAND);

        // Validate the Wish in Elasticsearch
        verify(mockWishSearchRepository, times(1)).save(testWish);
    }

    @Test
    @Transactional
    public void createWishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // Create the Wish with an existing ID
        wish.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishMockMvc.perform(post("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate);

        // Validate the Wish in Elasticsearch
        verify(mockWishSearchRepository, times(0)).save(wish);
    }


    @Test
    @Transactional
    public void getAllWishes() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get all the wishList
        restWishMockMvc.perform(get("/api/wishes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())));
    }
    
    @Test
    @Transactional
    public void getWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wish.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWish() throws Exception {
        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWish() throws Exception {
        // Initialize the database
        wishService.save(wish);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWishSearchRepository);

        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish
        Wish updatedWish = wishRepository.findById(wish.getId()).get();
        // Disconnect from session so that the updates on updatedWish are not directly saved in db
        em.detach(updatedWish);
        updatedWish
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND);

        restWishMockMvc.perform(put("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWish)))
            .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWish.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testWish.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWish.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testWish.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testWish.getBrand()).isEqualTo(UPDATED_BRAND);

        // Validate the Wish in Elasticsearch
        verify(mockWishSearchRepository, times(1)).save(testWish);
    }

    @Test
    @Transactional
    public void updateNonExistingWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Create the Wish

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishMockMvc.perform(put("/api/wishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Wish in Elasticsearch
        verify(mockWishSearchRepository, times(0)).save(wish);
    }

    @Test
    @Transactional
    public void deleteWish() throws Exception {
        // Initialize the database
        wishService.save(wish);

        int databaseSizeBeforeDelete = wishRepository.findAll().size();

        // Delete the wish
        restWishMockMvc.perform(delete("/api/wishes/{id}", wish.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Wish in Elasticsearch
        verify(mockWishSearchRepository, times(1)).deleteById(wish.getId());
    }

    @Test
    @Transactional
    public void searchWish() throws Exception {
        // Initialize the database
        wishService.save(wish);
        when(mockWishSearchRepository.search(queryStringQuery("id:" + wish.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(wish), PageRequest.of(0, 1), 1));
        // Search the wish
        restWishMockMvc.perform(get("/api/_search/wishes?query=id:" + wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wish.class);
        Wish wish1 = new Wish();
        wish1.setId(1L);
        Wish wish2 = new Wish();
        wish2.setId(wish1.getId());
        assertThat(wish1).isEqualTo(wish2);
        wish2.setId(2L);
        assertThat(wish1).isNotEqualTo(wish2);
        wish1.setId(null);
        assertThat(wish1).isNotEqualTo(wish2);
    }
}
