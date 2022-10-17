package com.samhello.sam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samhello.sam.IntegrationTest;
import com.samhello.sam.domain.SocialNetWork;
import com.samhello.sam.domain.enumeration.TypeOfSocial;
import com.samhello.sam.repository.SocialNetWorkRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SocialNetWorkResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SocialNetWorkResourceIT {

    private static final Long DEFAULT_TEACHER_ID = 1L;
    private static final Long UPDATED_TEACHER_ID = 2L;

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TypeOfSocial DEFAULT_TYPE = TypeOfSocial.FACEBOOK;
    private static final TypeOfSocial UPDATED_TYPE = TypeOfSocial.GOOGLE;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/social-net-works";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialNetWorkRepository socialNetWorkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialNetWorkMockMvc;

    private SocialNetWork socialNetWork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialNetWork createEntity(EntityManager em) {
        SocialNetWork socialNetWork = new SocialNetWork()
            .teacherId(DEFAULT_TEACHER_ID)
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .expiredTime(DEFAULT_EXPIRED_TIME)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return socialNetWork;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialNetWork createUpdatedEntity(EntityManager em) {
        SocialNetWork socialNetWork = new SocialNetWork()
            .teacherId(UPDATED_TEACHER_ID)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .expiredTime(UPDATED_EXPIRED_TIME)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return socialNetWork;
    }

    @BeforeEach
    public void initTest() {
        socialNetWork = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialNetWork() throws Exception {
        int databaseSizeBeforeCreate = socialNetWorkRepository.findAll().size();
        // Create the SocialNetWork
        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isCreated());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeCreate + 1);
        SocialNetWork testSocialNetWork = socialNetWorkList.get(socialNetWorkList.size() - 1);
        assertThat(testSocialNetWork.getTeacherId()).isEqualTo(DEFAULT_TEACHER_ID);
        assertThat(testSocialNetWork.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testSocialNetWork.getExpiredTime()).isEqualTo(DEFAULT_EXPIRED_TIME);
        assertThat(testSocialNetWork.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSocialNetWork.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSocialNetWork.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSocialNetWorkWithExistingId() throws Exception {
        // Create the SocialNetWork with an existing ID
        socialNetWork.setId(1L);

        int databaseSizeBeforeCreate = socialNetWorkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeacherIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setTeacherId(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccessTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setAccessToken(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiredTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setExpiredTime(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setType(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setCreatedAt(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialNetWorkRepository.findAll().size();
        // set the field null
        socialNetWork.setUpdatedAt(null);

        // Create the SocialNetWork, which fails.

        restSocialNetWorkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isBadRequest());

        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialNetWorks() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        // Get all the socialNetWorkList
        restSocialNetWorkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].teacherId").value(hasItem(DEFAULT_TEACHER_ID.intValue())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN)))
            .andExpect(jsonPath("$.[*].expiredTime").value(hasItem(DEFAULT_EXPIRED_TIME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSocialNetWork() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        // Get the socialNetWork
        restSocialNetWorkMockMvc
            .perform(get(ENTITY_API_URL_ID, socialNetWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialNetWork.getId().intValue()))
            .andExpect(jsonPath("$.teacherId").value(DEFAULT_TEACHER_ID.intValue()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN))
            .andExpect(jsonPath("$.expiredTime").value(DEFAULT_EXPIRED_TIME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSocialNetWork() throws Exception {
        // Get the socialNetWork
        restSocialNetWorkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSocialNetWork() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();

        // Update the socialNetWork
        SocialNetWork updatedSocialNetWork = socialNetWorkRepository.findById(socialNetWork.getId()).get();
        // Disconnect from session so that the updates on updatedSocialNetWork are not directly saved in db
        em.detach(updatedSocialNetWork);
        updatedSocialNetWork
            .teacherId(UPDATED_TEACHER_ID)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .expiredTime(UPDATED_EXPIRED_TIME)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSocialNetWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSocialNetWork.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSocialNetWork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetWork testSocialNetWork = socialNetWorkList.get(socialNetWorkList.size() - 1);
        assertThat(testSocialNetWork.getTeacherId()).isEqualTo(UPDATED_TEACHER_ID);
        assertThat(testSocialNetWork.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testSocialNetWork.getExpiredTime()).isEqualTo(UPDATED_EXPIRED_TIME);
        assertThat(testSocialNetWork.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSocialNetWork.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSocialNetWork.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialNetWork.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialNetWork))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialNetWork))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialNetWork)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialNetWorkWithPatch() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();

        // Update the socialNetWork using partial update
        SocialNetWork partialUpdatedSocialNetWork = new SocialNetWork();
        partialUpdatedSocialNetWork.setId(socialNetWork.getId());

        partialUpdatedSocialNetWork.updatedAt(UPDATED_UPDATED_AT);

        restSocialNetWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetWork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetWork testSocialNetWork = socialNetWorkList.get(socialNetWorkList.size() - 1);
        assertThat(testSocialNetWork.getTeacherId()).isEqualTo(DEFAULT_TEACHER_ID);
        assertThat(testSocialNetWork.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testSocialNetWork.getExpiredTime()).isEqualTo(DEFAULT_EXPIRED_TIME);
        assertThat(testSocialNetWork.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSocialNetWork.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSocialNetWork.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSocialNetWorkWithPatch() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();

        // Update the socialNetWork using partial update
        SocialNetWork partialUpdatedSocialNetWork = new SocialNetWork();
        partialUpdatedSocialNetWork.setId(socialNetWork.getId());

        partialUpdatedSocialNetWork
            .teacherId(UPDATED_TEACHER_ID)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .expiredTime(UPDATED_EXPIRED_TIME)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSocialNetWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialNetWork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
        SocialNetWork testSocialNetWork = socialNetWorkList.get(socialNetWorkList.size() - 1);
        assertThat(testSocialNetWork.getTeacherId()).isEqualTo(UPDATED_TEACHER_ID);
        assertThat(testSocialNetWork.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testSocialNetWork.getExpiredTime()).isEqualTo(UPDATED_EXPIRED_TIME);
        assertThat(testSocialNetWork.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSocialNetWork.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSocialNetWork.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialNetWork.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialNetWork))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialNetWork))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialNetWork() throws Exception {
        int databaseSizeBeforeUpdate = socialNetWorkRepository.findAll().size();
        socialNetWork.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetWorkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(socialNetWork))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetWork in the database
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialNetWork() throws Exception {
        // Initialize the database
        socialNetWorkRepository.saveAndFlush(socialNetWork);

        int databaseSizeBeforeDelete = socialNetWorkRepository.findAll().size();

        // Delete the socialNetWork
        restSocialNetWorkMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialNetWork.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialNetWork> socialNetWorkList = socialNetWorkRepository.findAll();
        assertThat(socialNetWorkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
