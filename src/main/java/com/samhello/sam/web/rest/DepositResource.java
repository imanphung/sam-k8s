package com.samhello.sam.web.rest;

import com.samhello.sam.domain.Deposit;
import com.samhello.sam.repository.DepositRepository;
import com.samhello.sam.service.DepositService;
import com.samhello.sam.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.samhello.sam.domain.Deposit}.
 */
@RestController
@RequestMapping("/api")
public class DepositResource {

    private final Logger log = LoggerFactory.getLogger(DepositResource.class);

    private static final String ENTITY_NAME = "samDeposit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositService depositService;

    private final DepositRepository depositRepository;

    public DepositResource(DepositService depositService, DepositRepository depositRepository) {
        this.depositService = depositService;
        this.depositRepository = depositRepository;
    }

    /**
     * {@code POST  /deposits} : Create a new deposit.
     *
     * @param deposit the deposit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deposit, or with status {@code 400 (Bad Request)} if the deposit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deposits")
    public ResponseEntity<Deposit> createDeposit(@Valid @RequestBody Deposit deposit) throws URISyntaxException {
        log.debug("REST request to save Deposit : {}", deposit);
        if (deposit.getId() != null) {
            throw new BadRequestAlertException("A new deposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deposit result = depositService.save(deposit);
        return ResponseEntity
            .created(new URI("/api/deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deposits/:id} : Updates an existing deposit.
     *
     * @param id the id of the deposit to save.
     * @param deposit the deposit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deposit,
     * or with status {@code 400 (Bad Request)} if the deposit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deposit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deposits/{id}")
    public ResponseEntity<Deposit> updateDeposit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Deposit deposit
    ) throws URISyntaxException {
        log.debug("REST request to update Deposit : {}, {}", id, deposit);
        if (deposit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deposit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deposit result = depositService.update(deposit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deposit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deposits/:id} : Partial updates given fields of an existing deposit, field will ignore if it is null
     *
     * @param id the id of the deposit to save.
     * @param deposit the deposit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deposit,
     * or with status {@code 400 (Bad Request)} if the deposit is not valid,
     * or with status {@code 404 (Not Found)} if the deposit is not found,
     * or with status {@code 500 (Internal Server Error)} if the deposit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deposits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deposit> partialUpdateDeposit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deposit deposit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deposit partially : {}, {}", id, deposit);
        if (deposit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deposit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deposit> result = depositService.partialUpdate(deposit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deposit.getId().toString())
        );
    }

    /**
     * {@code GET  /deposits} : get all the deposits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deposits in body.
     */
    @GetMapping("/deposits")
    public ResponseEntity<List<Deposit>> getAllDeposits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Deposits");
        Page<Deposit> page = depositService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deposits/:id} : get the "id" deposit.
     *
     * @param id the id of the deposit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deposit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deposits/{id}")
    public ResponseEntity<Deposit> getDeposit(@PathVariable Long id) {
        log.debug("REST request to get Deposit : {}", id);
        Optional<Deposit> deposit = depositService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deposit);
    }

    /**
     * {@code DELETE  /deposits/:id} : delete the "id" deposit.
     *
     * @param id the id of the deposit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deposits/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id) {
        log.debug("REST request to delete Deposit : {}", id);
        depositService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
