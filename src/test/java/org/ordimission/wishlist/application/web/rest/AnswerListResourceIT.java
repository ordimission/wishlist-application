package org.ordimission.wishlist.application.web.rest;

import org.ordimission.wishlist.application.WishListApplicationApp;
import org.ordimission.wishlist.application.domain.AnswerList;
import org.ordimission.wishlist.application.repository.AnswerListRepository;
import org.ordimission.wishlist.application.repository.search.AnswerListSearchRepository;
import org.ordimission.wishlist.application.service.AnswerListService;
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
 * Integration tests for the {@link AnswerListResource} REST controller.
 */
@SpringBootTest(classes = WishListApplicationApp.class)
public class AnswerListResourceIT {

    @Autowired
    private AnswerListRepository answerListRepository;

    @Autowired
    private AnswerListService answerListService;

    /**
     * This repository is mocked in the org.ordimission.wishlist.application.repository.search test package.
     *
     * @see org.ordimission.wishlist.application.repository.search.AnswerListSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnswerListSearchRepository mockAnswerListSearchRepository;

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

    private MockMvc restAnswerListMockMvc;

    private AnswerList answerList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnswerListResource answerListResource = new AnswerListResource(answerListService);
        this.restAnswerListMockMvc = MockMvcBuilders.standaloneSetup(answerListResource)
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
    public static AnswerList createEntity(EntityManager em) {
        AnswerList answerList = new AnswerList();
        return answerList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerList createUpdatedEntity(EntityManager em) {
        AnswerList answerList = new AnswerList();
        return answerList;
    }

    @BeforeEach
    public void initTest() {
        answerList = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswerList() throws Exception {
        int databaseSizeBeforeCreate = answerListRepository.findAll().size();

        // Create the AnswerList
        restAnswerListMockMvc.perform(post("/api/answer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerList)))
            .andExpect(status().isCreated());

        // Validate the AnswerList in the database
        List<AnswerList> answerListList = answerListRepository.findAll();
        assertThat(answerListList).hasSize(databaseSizeBeforeCreate + 1);
        AnswerList testAnswerList = answerListList.get(answerListList.size() - 1);

        // Validate the AnswerList in Elasticsearch
        verify(mockAnswerListSearchRepository, times(1)).save(testAnswerList);
    }

    @Test
    @Transactional
    public void createAnswerListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerListRepository.findAll().size();

        // Create the AnswerList with an existing ID
        answerList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerListMockMvc.perform(post("/api/answer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerList)))
            .andExpect(status().isBadRequest());

        // Validate the AnswerList in the database
        List<AnswerList> answerListList = answerListRepository.findAll();
        assertThat(answerListList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnswerList in Elasticsearch
        verify(mockAnswerListSearchRepository, times(0)).save(answerList);
    }


    @Test
    @Transactional
    public void getAllAnswerLists() throws Exception {
        // Initialize the database
        answerListRepository.saveAndFlush(answerList);

        // Get all the answerListList
        restAnswerListMockMvc.perform(get("/api/answer-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answerList.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAnswerList() throws Exception {
        // Initialize the database
        answerListRepository.saveAndFlush(answerList);

        // Get the answerList
        restAnswerListMockMvc.perform(get("/api/answer-lists/{id}", answerList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answerList.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAnswerList() throws Exception {
        // Get the answerList
        restAnswerListMockMvc.perform(get("/api/answer-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswerList() throws Exception {
        // Initialize the database
        answerListService.save(answerList);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnswerListSearchRepository);

        int databaseSizeBeforeUpdate = answerListRepository.findAll().size();

        // Update the answerList
        AnswerList updatedAnswerList = answerListRepository.findById(answerList.getId()).get();
        // Disconnect from session so that the updates on updatedAnswerList are not directly saved in db
        em.detach(updatedAnswerList);

        restAnswerListMockMvc.perform(put("/api/answer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnswerList)))
            .andExpect(status().isOk());

        // Validate the AnswerList in the database
        List<AnswerList> answerListList = answerListRepository.findAll();
        assertThat(answerListList).hasSize(databaseSizeBeforeUpdate);
        AnswerList testAnswerList = answerListList.get(answerListList.size() - 1);

        // Validate the AnswerList in Elasticsearch
        verify(mockAnswerListSearchRepository, times(1)).save(testAnswerList);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswerList() throws Exception {
        int databaseSizeBeforeUpdate = answerListRepository.findAll().size();

        // Create the AnswerList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerListMockMvc.perform(put("/api/answer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerList)))
            .andExpect(status().isBadRequest());

        // Validate the AnswerList in the database
        List<AnswerList> answerListList = answerListRepository.findAll();
        assertThat(answerListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnswerList in Elasticsearch
        verify(mockAnswerListSearchRepository, times(0)).save(answerList);
    }

    @Test
    @Transactional
    public void deleteAnswerList() throws Exception {
        // Initialize the database
        answerListService.save(answerList);

        int databaseSizeBeforeDelete = answerListRepository.findAll().size();

        // Delete the answerList
        restAnswerListMockMvc.perform(delete("/api/answer-lists/{id}", answerList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnswerList> answerListList = answerListRepository.findAll();
        assertThat(answerListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnswerList in Elasticsearch
        verify(mockAnswerListSearchRepository, times(1)).deleteById(answerList.getId());
    }

    @Test
    @Transactional
    public void searchAnswerList() throws Exception {
        // Initialize the database
        answerListService.save(answerList);
        when(mockAnswerListSearchRepository.search(queryStringQuery("id:" + answerList.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(answerList), PageRequest.of(0, 1), 1));
        // Search the answerList
        restAnswerListMockMvc.perform(get("/api/_search/answer-lists?query=id:" + answerList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answerList.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerList.class);
        AnswerList answerList1 = new AnswerList();
        answerList1.setId(1L);
        AnswerList answerList2 = new AnswerList();
        answerList2.setId(answerList1.getId());
        assertThat(answerList1).isEqualTo(answerList2);
        answerList2.setId(2L);
        assertThat(answerList1).isNotEqualTo(answerList2);
        answerList1.setId(null);
        assertThat(answerList1).isNotEqualTo(answerList2);
    }
}
