package com.samhello.sam.web.rest;

import static com.samhello.sam.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samhello.sam.IntegrationTest;
import com.samhello.sam.domain.Lesson;
import com.samhello.sam.repository.LessonRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LessonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LessonResourceIT {

    private static final Long DEFAULT_TEACHER_ID = 1L;
    private static final Long UPDATED_TEACHER_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_TIME = 1L;
    private static final Long UPDATED_TIME = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/lessons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonMockMvc;

    private Lesson lesson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .teacherId(DEFAULT_TEACHER_ID)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .price(DEFAULT_PRICE)
            .time(DEFAULT_TIME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return lesson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createUpdatedEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .teacherId(UPDATED_TEACHER_ID)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .price(UPDATED_PRICE)
            .time(UPDATED_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return lesson;
    }

    @BeforeEach
    public void initTest() {
        lesson = createEntity(em);
    }

    @Test
    @Transactional
    void createLesson() throws Exception {
        int databaseSizeBeforeCreate = lessonRepository.findAll().size();
        // Create the Lesson
        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isCreated());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate + 1);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTeacherId()).isEqualTo(DEFAULT_TEACHER_ID);
        assertThat(testLesson.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLesson.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testLesson.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testLesson.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testLesson.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLesson.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createLessonWithExistingId() throws Exception {
        // Create the Lesson with an existing ID
        lesson.setId(1L);

        int databaseSizeBeforeCreate = lessonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeacherIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setTeacherId(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setTitle(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setContent(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setPrice(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setTime(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setCreatedAt(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonRepository.findAll().size();
        // set the field null
        lesson.setUpdatedAt(null);

        // Create the Lesson, which fails.

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isBadRequest());

        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessons() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].teacherId").value(hasItem(DEFAULT_TEACHER_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        // Get the lesson
        restLessonMockMvc
            .perform(get(ENTITY_API_URL_ID, lesson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lesson.getId().intValue()))
            .andExpect(jsonPath("$.teacherId").value(DEFAULT_TEACHER_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLesson() throws Exception {
        // Get the lesson
        restLessonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson
        Lesson updatedLesson = lessonRepository.findById(lesson.getId()).get();
        // Disconnect from session so that the updates on updatedLesson are not directly saved in db
        em.detach(updatedLesson);
        updatedLesson
            .teacherId(UPDATED_TEACHER_ID)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .price(UPDATED_PRICE)
            .time(UPDATED_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLesson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTeacherId()).isEqualTo(UPDATED_TEACHER_ID);
        assertThat(testLesson.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLesson.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testLesson.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testLesson.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testLesson.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLesson.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lesson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        partialUpdatedLesson.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTeacherId()).isEqualTo(DEFAULT_TEACHER_ID);
        assertThat(testLesson.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLesson.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testLesson.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testLesson.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testLesson.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLesson.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        partialUpdatedLesson
            .teacherId(UPDATED_TEACHER_ID)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .price(UPDATED_PRICE)
            .time(UPDATED_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
        Lesson testLesson = lessonList.get(lessonList.size() - 1);
        assertThat(testLesson.getTeacherId()).isEqualTo(UPDATED_TEACHER_ID);
        assertThat(testLesson.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLesson.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testLesson.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testLesson.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testLesson.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLesson.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lesson))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLesson() throws Exception {
        int databaseSizeBeforeUpdate = lessonRepository.findAll().size();
        lesson.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lesson)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLesson() throws Exception {
        // Initialize the database
        lessonRepository.saveAndFlush(lesson);

        int databaseSizeBeforeDelete = lessonRepository.findAll().size();

        // Delete the lesson
        restLessonMockMvc
            .perform(delete(ENTITY_API_URL_ID, lesson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lesson> lessonList = lessonRepository.findAll();
        assertThat(lessonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
