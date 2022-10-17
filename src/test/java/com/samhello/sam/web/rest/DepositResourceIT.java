package com.samhello.sam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samhello.sam.IntegrationTest;
import com.samhello.sam.domain.Deposit;
import com.samhello.sam.domain.enumeration.DepositStatus;
import com.samhello.sam.repository.DepositRepository;
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
 * Integration tests for the {@link DepositResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepositResourceIT {

    private static final Long DEFAULT_STUDENT_ID = 1L;
    private static final Long UPDATED_STUDENT_ID = 2L;

    private static final Long DEFAULT_TRANSFER_ID = 1L;
    private static final Long UPDATED_TRANSFER_ID = 2L;

    private static final DepositStatus DEFAULT_STATUS = DepositStatus.CREATED;
    private static final DepositStatus UPDATED_STATUS = DepositStatus.PENDDING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/deposits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositMockMvc;

    private Deposit deposit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposit createEntity(EntityManager em) {
        Deposit deposit = new Deposit()
            .studentId(DEFAULT_STUDENT_ID)
            .transferId(DEFAULT_TRANSFER_ID)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return deposit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposit createUpdatedEntity(EntityManager em) {
        Deposit deposit = new Deposit()
            .studentId(UPDATED_STUDENT_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return deposit;
    }

    @BeforeEach
    public void initTest() {
        deposit = createEntity(em);
    }

    @Test
    @Transactional
    void createDeposit() throws Exception {
        int databaseSizeBeforeCreate = depositRepository.findAll().size();
        // Create the Deposit
        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isCreated());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate + 1);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testDeposit.getTransferId()).isEqualTo(DEFAULT_TRANSFER_ID);
        assertThat(testDeposit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeposit.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDeposit.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createDepositWithExistingId() throws Exception {
        // Create the Deposit with an existing ID
        deposit.setId(1L);

        int databaseSizeBeforeCreate = depositRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setStudentId(null);

        // Create the Deposit, which fails.

        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransferIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setTransferId(null);

        // Create the Deposit, which fails.

        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setStatus(null);

        // Create the Deposit, which fails.

        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setCreatedAt(null);

        // Create the Deposit, which fails.

        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositRepository.findAll().size();
        // set the field null
        deposit.setUpdatedAt(null);

        // Create the Deposit, which fails.

        restDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isBadRequest());

        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeposits() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get all the depositList
        restDepositMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposit.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transferId").value(hasItem(DEFAULT_TRANSFER_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        // Get the deposit
        restDepositMockMvc
            .perform(get(ENTITY_API_URL_ID, deposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deposit.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID.intValue()))
            .andExpect(jsonPath("$.transferId").value(DEFAULT_TRANSFER_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeposit() throws Exception {
        // Get the deposit
        restDepositMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Update the deposit
        Deposit updatedDeposit = depositRepository.findById(deposit.getId()).get();
        // Disconnect from session so that the updates on updatedDeposit are not directly saved in db
        em.detach(updatedDeposit);
        updatedDeposit
            .studentId(UPDATED_STUDENT_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeposit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeposit))
            )
            .andExpect(status().isOk());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testDeposit.getTransferId()).isEqualTo(UPDATED_TRANSFER_ID);
        assertThat(testDeposit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeposit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDeposit.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deposit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepositWithPatch() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Update the deposit using partial update
        Deposit partialUpdatedDeposit = new Deposit();
        partialUpdatedDeposit.setId(deposit.getId());

        partialUpdatedDeposit.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeposit))
            )
            .andExpect(status().isOk());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testDeposit.getTransferId()).isEqualTo(DEFAULT_TRANSFER_ID);
        assertThat(testDeposit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeposit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDeposit.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDepositWithPatch() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeUpdate = depositRepository.findAll().size();

        // Update the deposit using partial update
        Deposit partialUpdatedDeposit = new Deposit();
        partialUpdatedDeposit.setId(deposit.getId());

        partialUpdatedDeposit
            .studentId(UPDATED_STUDENT_ID)
            .transferId(UPDATED_TRANSFER_ID)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeposit))
            )
            .andExpect(status().isOk());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
        Deposit testDeposit = depositList.get(depositList.size() - 1);
        assertThat(testDeposit.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testDeposit.getTransferId()).isEqualTo(UPDATED_TRANSFER_ID);
        assertThat(testDeposit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeposit.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDeposit.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeposit() throws Exception {
        int databaseSizeBeforeUpdate = depositRepository.findAll().size();
        deposit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deposit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposit in the database
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeposit() throws Exception {
        // Initialize the database
        depositRepository.saveAndFlush(deposit);

        int databaseSizeBeforeDelete = depositRepository.findAll().size();

        // Delete the deposit
        restDepositMockMvc
            .perform(delete(ENTITY_API_URL_ID, deposit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deposit> depositList = depositRepository.findAll();
        assertThat(depositList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
