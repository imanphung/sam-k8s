package com.samhello.sam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samhello.sam.IntegrationTest;
import com.samhello.sam.domain.Report;
import com.samhello.sam.domain.enumeration.TypeOfStar;
import com.samhello.sam.repository.ReportRepository;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;

    private static final Long DEFAULT_LESSON_ID = 1L;
    private static final Long UPDATED_LESSON_ID = 2L;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final TypeOfStar DEFAULT_STAR = TypeOfStar.ONE;
    private static final TypeOfStar UPDATED_STAR = TypeOfStar.TWO;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMockMvc;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .studentId(DEFAULT_STUDENT_ID)
            .lessonId(DEFAULT_LESSON_ID)
            .comment(DEFAULT_COMMENT)
            .star(DEFAULT_STAR)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .comment(UPDATED_COMMENT)
            .star(UPDATED_STAR)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();
        // Create the Report
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testReport.getLessonId()).isEqualTo(DEFAULT_LESSON_ID);
        assertThat(testReport.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testReport.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReport.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);

        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setStudentId(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setLessonId(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStarIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setStar(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setCreatedAt(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setUpdatedAt(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].lessonId").value(hasItem(DEFAULT_LESSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc
            .perform(get(ENTITY_API_URL_ID, report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.lessonId").value(DEFAULT_LESSON_ID.intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .comment(UPDATED_COMMENT)
            .star(UPDATED_STAR)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testReport.getLessonId()).isEqualTo(UPDATED_LESSON_ID);
        assertThat(testReport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReport.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, report.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.lessonId(UPDATED_LESSON_ID).comment(UPDATED_COMMENT).star(UPDATED_STAR);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testReport.getLessonId()).isEqualTo(UPDATED_LESSON_ID);
        assertThat(testReport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReport.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReport.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .comment(UPDATED_COMMENT)
            .star(UPDATED_STAR)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testReport.getLessonId()).isEqualTo(UPDATED_LESSON_ID);
        assertThat(testReport.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReport.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReport.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, report.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(report)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, report.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
