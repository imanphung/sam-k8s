package com.samhello.sam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samhello.sam.IntegrationTest;
import com.samhello.sam.domain.Invoice;
import com.samhello.sam.domain.enumeration.DepositStatus;
import com.samhello.sam.repository.InvoiceRepository;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;

    private static final Long DEFAULT_LESSON_ID = 1L;
    private static final Long UPDATED_LESSON_ID = 2L;

    private static final Long DEFAULT_TRANSFER_ID = 1L;
    private static final Long UPDATED_TRANSFER_ID = 2L;

    private static final DepositStatus DEFAULT_STATUS = DepositStatus.CREATED;
    private static final DepositStatus UPDATED_STATUS = DepositStatus.PENDDING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .studentId(DEFAULT_STUDENT_ID)
            .lessonId(DEFAULT_LESSON_ID)
            .transferId(DEFAULT_TRANSFER_ID)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testInvoice.getLessonId()).isEqualTo(DEFAULT_LESSON_ID);
        assertThat(testInvoice.getTransferId()).isEqualTo(DEFAULT_TRANSFER_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInvoice.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setStudentId(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLessonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setLessonId(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransferIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTransferId(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setStatus(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCreatedAt(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setUpdatedAt(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].lessonId").value(hasItem(DEFAULT_LESSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].transferId").value(hasItem(DEFAULT_TRANSFER_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.lessonId").value(DEFAULT_LESSON_ID.intValue()))
            .andExpect(jsonPath("$.transferId").value(DEFAULT_TRANSFER_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testInvoice.getLessonId()).isEqualTo(UPDATED_LESSON_ID);
        assertThat(testInvoice.getTransferId()).isEqualTo(UPDATED_TRANSFER_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice.transferId(UPDATED_TRANSFER_ID).status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testInvoice.getLessonId()).isEqualTo(DEFAULT_LESSON_ID);
        assertThat(testInvoice.getTransferId()).isEqualTo(UPDATED_TRANSFER_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .studentId(UPDATED_STUDENT_ID)
            .lessonId(UPDATED_LESSON_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testInvoice.getLessonId()).isEqualTo(UPDATED_LESSON_ID);
        assertThat(testInvoice.getTransferId()).isEqualTo(UPDATED_TRANSFER_ID);
        assertThat(testInvoice.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoice.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
