package com.samhello.sam.web.rest;

import com.samhello.sam.domain.Report;
import com.samhello.sam.repository.ReportRepository;
import com.samhello.sam.service.ReportService;
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
 * REST controller for managing {@link com.samhello.sam.domain.Report}.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private static final String ENTITY_NAME = "samReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportService reportService;

    private final ReportRepository reportRepository;

    public ReportResource(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    /**
     * {@code POST  /reports} : Create a new report.
     *
     * @param report the report to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new report, or with status {@code 400 (Bad Request)} if the report has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@Valid @RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to save Report : {}", report);
        if (report.getId() != null) {
            throw new BadRequestAlertException("A new report cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Report result = reportService.save(report);
        return ResponseEntity
            .created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reports/:id} : Updates an existing report.
     *
     * @param id the id of the report to save.
     * @param report the report to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated report,
     * or with status {@code 400 (Bad Request)} if the report is not valid,
     * or with status {@code 500 (Internal Server Error)} if the report couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reports/{id}")
    public ResponseEntity<Report> updateReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Report report
    ) throws URISyntaxException {
        log.debug("REST request to update Report : {}, {}", id, report);
        if (report.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, report.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Report result = reportService.update(report);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, report.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reports/:id} : Partial updates given fields of an existing report, field will ignore if it is null
     *
     * @param id the id of the report to save.
     * @param report the report to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated report,
     * or with status {@code 400 (Bad Request)} if the report is not valid,
     * or with status {@code 404 (Not Found)} if the report is not found,
     * or with status {@code 500 (Internal Server Error)} if the report couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Report> partialUpdateReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Report report
    ) throws URISyntaxException {
        log.debug("REST request to partial update Report partially : {}, {}", id, report);
        if (report.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, report.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Report> result = reportService.partialUpdate(report);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, report.getId().toString())
        );
    }

    /**
     * {@code GET  /reports} : get all the reports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reports in body.
     */
    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Reports");
        Page<Report> page = reportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reports/:id} : get the "id" report.
     *
     * @param id the id of the report to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the report, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reports/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Optional<Report> report = reportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(report);
    }

    /**
     * {@code DELETE  /reports/:id} : delete the "id" report.
     *
     * @param id the id of the report to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
