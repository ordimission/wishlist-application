package org.ordimission.wishlist.application.web.rest;

import org.ordimission.wishlist.application.WishListApplicationApp;
import org.ordimission.wishlist.application.domain.Answer;
import org.ordimission.wishlist.application.repository.AnswerRepository;
import org.ordimission.wishlist.application.repository.search.AnswerSearchRepository;
import org.ordimission.wishlist.application.service.AnswerService;
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
 * Integration tests for the {@link AnswerResource} REST controller.
 */
@SpringBootTest(classes = WishListApplicationApp.class)
public class AnswerResourceIT {

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
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerService answerService;

    /**
     * This repository is mocked in the org.ordimission.wishlist.application.repository.search test package.
     *
     * @see org.ordimission.wishlist.application.repository.search.AnswerSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnswerSearchRepository mockAnswerSearchRepository;

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

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnswerResource answerResource = new AnswerResource(answerService);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource)
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
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer()
            .quantity(DEFAULT_QUANTITY)
            .unit(DEFAULT_UNIT)
            .model(DEFAULT_MODEL)
            .brand(DEFAULT_BRAND);
        return answer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createUpdatedEntity(EntityManager em) {
        Answer answer = new Answer()
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND);
        return answer;
    }

    @BeforeEach
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answer)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testAnswer.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testAnswer.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testAnswer.getBrand()).isEqualTo(DEFAULT_BRAND);

        // Validate the Answer in Elasticsearch
        verify(mockAnswerSearchRepository, times(1)).save(testAnswer);
    }

    @Test
    @Transactional
    public void createAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer with an existing ID
        answer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answer)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Answer in Elasticsearch
        verify(mockAnswerSearchRepository, times(0)).save(answer);
    }


    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())));
    }
    
    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerService.save(answer);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnswerSearchRepository);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();
        // Disconnect from session so that the updates on updatedAnswer are not directly saved in db
        em.detach(updatedAnswer);
        updatedAnswer
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .model(UPDATED_MODEL)
            .brand(UPDATED_BRAND);

        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnswer)))
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAnswer.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testAnswer.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testAnswer.getBrand()).isEqualTo(UPDATED_BRAND);

        // Validate the Answer in Elasticsearch
        verify(mockAnswerSearchRepository, times(1)).save(testAnswer);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Create the Answer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answer)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Answer in Elasticsearch
        verify(mockAnswerSearchRepository, times(0)).save(answer);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerService.save(answer);

        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Delete the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Answer in Elasticsearch
        verify(mockAnswerSearchRepository, times(1)).deleteById(answer.getId());
    }

    @Test
    @Transactional
    public void searchAnswer() throws Exception {
        // Initialize the database
        answerService.save(answer);
        when(mockAnswerSearchRepository.search(queryStringQuery("id:" + answer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(answer), PageRequest.of(0, 1), 1));
        // Search the answer
        restAnswerMockMvc.perform(get("/api/_search/answers?query=id:" + answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = new Answer();
        answer1.setId(1L);
        Answer answer2 = new Answer();
        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);
        answer2.setId(2L);
        assertThat(answer1).isNotEqualTo(answer2);
        answer1.setId(null);
        assertThat(answer1).isNotEqualTo(answer2);
    }
}
