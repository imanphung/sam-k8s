package com.samhello.sam.service;

import com.samhello.sam.domain.Deposit;
import com.samhello.sam.repository.DepositRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deposit}.
 */
@Service
@Transactional
public class DepositService {

    private final Logger log = LoggerFactory.getLogger(DepositService.class);

    private final DepositRepository depositRepository;

    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    /**
     * Save a deposit.
     *
     * @param deposit the entity to save.
     * @return the persisted entity.
     */
    public Deposit save(Deposit deposit) {
        log.debug("Request to save Deposit : {}", deposit);
        return depositRepository.save(deposit);
    }

    /**
     * Update a deposit.
     *
     * @param deposit the entity to save.
     * @return the persisted entity.
     */
    public Deposit update(Deposit deposit) {
        log.debug("Request to save Deposit : {}", deposit);
        return depositRepository.save(deposit);
    }

    /**
     * Partially update a deposit.
     *
     * @param deposit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Deposit> partialUpdate(Deposit deposit) {
        log.debug("Request to partially update Deposit : {}", deposit);

        return depositRepository
            .findById(deposit.getId())
            .map(existingDeposit -> {
                if (deposit.getStudentId() != null) {
                    existingDeposit.setStudentId(deposit.getStudentId());
                }
                if (deposit.getTransferId() != null) {
                    existingDeposit.setTransferId(deposit.getTransferId());
                }
                if (deposit.getStatus() != null) {
                    existingDeposit.setStatus(deposit.getStatus());
                }
                if (deposit.getCreatedAt() != null) {
                    existingDeposit.setCreatedAt(deposit.getCreatedAt());
                }
                if (deposit.getUpdatedAt() != null) {
                    existingDeposit.setUpdatedAt(deposit.getUpdatedAt());
                }

                return existingDeposit;
            })
            .map(depositRepository::save);
    }

    /**
     * Get all the deposits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Deposit> findAll(Pageable pageable) {
        log.debug("Request to get all Deposits");
        return depositRepository.findAll(pageable);
    }

    /**
     * Get one deposit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Deposit> findOne(Long id) {
        log.debug("Request to get Deposit : {}", id);
        return depositRepository.findById(id);
    }

    /**
     * Delete the deposit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Deposit : {}", id);
        depositRepository.deleteById(id);
    }
}
