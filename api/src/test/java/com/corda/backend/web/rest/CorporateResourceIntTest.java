/*
package com.corda.backend.web.rest;

import com.corda.backend.ApiApp;

import com.corda.backend.domain.Corporate;
import com.corda.backend.repository.CorporateRepository;
import com.corda.backend.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.corda.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

*/
/**
 * Test class for the CorporateResource REST controller.
 *
 * @see CorporateResource
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApp.class)
public class CorporateResourceIntTest {

    private static final String DEFAULT_CORPORATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_NAME = "BBBBBBBBBB";

    @Autowired
    private CorporateRepository corporateRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorporateMockMvc;

    private Corporate corporate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorporateResource corporateResource = new CorporateResource(corporateRepository);
        this.restCorporateMockMvc = MockMvcBuilders.standaloneSetup(corporateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    */
/**
 * Create an entity for this test.
 *
 * This is a static method, as tests for other entities might also need it,
 * if they test an entity which requires the current entity.
 *//*

    public static Corporate createEntity(EntityManager em) {
        Corporate corporate = new Corporate()
            .corporate_id(DEFAULT_CORPORATE_ID)
            .corporate_name(DEFAULT_CORPORATE_NAME);
        return corporate;
    }

    @Before
    public void initTest() {
        corporate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorporate() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // Create the Corporate
        restCorporateMockMvc.perform(post("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isCreated());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate + 1);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCorporate_id()).isEqualTo(DEFAULT_CORPORATE_ID);
        assertThat(testCorporate.getCorporate_name()).isEqualTo(DEFAULT_CORPORATE_NAME);
    }

    @Test
    @Transactional
    public void createCorporateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // Create the Corporate with an existing ID
        corporate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporateMockMvc.perform(post("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorporates() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList
        restCorporateMockMvc.perform(get("/api/corporates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
            .andExpect(jsonPath("$.[*].corporate_id").value(hasItem(DEFAULT_CORPORATE_ID.toString())))
            .andExpect(jsonPath("$.[*].corporate_name").value(hasItem(DEFAULT_CORPORATE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get the corporate
        restCorporateMockMvc.perform(get("/api/corporates/{id}", corporate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corporate.getId().intValue()))
            .andExpect(jsonPath("$.corporate_id").value(DEFAULT_CORPORATE_ID.toString()))
            .andExpect(jsonPath("$.corporate_name").value(DEFAULT_CORPORATE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorporate() throws Exception {
        // Get the corporate
        restCorporateMockMvc.perform(get("/api/corporates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate
        Corporate updatedCorporate = corporateRepository.findById(corporate.getId()).get();
        // Disconnect from session so that the updates on updatedCorporate are not directly saved in db
        em.detach(updatedCorporate);
        updatedCorporate
            .corporate_id(UPDATED_CORPORATE_ID)
            .corporate_name(UPDATED_CORPORATE_NAME);

        restCorporateMockMvc.perform(put("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCorporate)))
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCorporate_id()).isEqualTo(UPDATED_CORPORATE_ID);
        assertThat(testCorporate.getCorporate_name()).isEqualTo(UPDATED_CORPORATE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Create the Corporate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporateMockMvc.perform(put("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        int databaseSizeBeforeDelete = corporateRepository.findAll().size();

        // Get the corporate
        restCorporateMockMvc.perform(delete("/api/corporates/{id}", corporate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corporate.class);
        Corporate corporate1 = new Corporate();
        corporate1.setId(1L);
        Corporate corporate2 = new Corporate();
        corporate2.setId(corporate1.getId());
        assertThat(corporate1).isEqualTo(corporate2);
        corporate2.setId(2L);
        assertThat(corporate1).isNotEqualTo(corporate2);
        corporate1.setId(null);
        assertThat(corporate1).isNotEqualTo(corporate2);
    }
}
*/
