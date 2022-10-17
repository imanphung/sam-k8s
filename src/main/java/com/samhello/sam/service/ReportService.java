package com.samhello.sam.service;

import com.samhello.sam.domain.Report;
import com.samhello.sam.repository.ReportRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Report}.
 */
@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Save a report.
     *
     * @param report the entity to save.
     * @return the persisted entity.
     */
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        return reportRepository.save(report);
    }

    /**
     * Update a report.
     *
     * @param report the entity to save.
     * @return the persisted entity.
     */
    public Report update(Report report) {
        log.debug("Request to save Report : {}", report);
        return reportRepository.save(report);
    }

    /**
     * Partially update a report.
     *
     * @param report the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Report> partialUpdate(Report report) {
        log.debug("Request to partially update Report : {}", report);

        return reportRepository
            .findById(report.getId())
            .map(existingReport -> {
                if (report.getStudentId() != null) {
                    existingReport.setStudentId(report.getStudentId());
                }
                if (report.getLessonId() != null) {
                    existingReport.setLessonId(report.getLessonId());
                }
                if (report.getComment() != null) {
                    existingReport.setComment(report.getComment());
                }
                if (report.getStar() != null) {
                    existingReport.setStar(report.getStar());
                }
                if (report.getCreatedAt() != null) {
                    existingReport.setCreatedAt(report.getCreatedAt());
                }
                if (report.getUpdatedAt() != null) {
                    existingReport.setUpdatedAt(report.getUpdatedAt());
                }

                return existingReport;
            })
            .map(reportRepository::save);
    }

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Report> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable);
    }

    /**
     * Get one report by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Report> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
    }
}
