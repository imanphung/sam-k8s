package com.samhello.sam.web.rest;

import com.samhello.sam.domain.SocialNetWork;
import com.samhello.sam.repository.SocialNetWorkRepository;
import com.samhello.sam.service.SocialNetWorkService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.samhello.sam.domain.SocialNetWork}.
 */
@RestController
@RequestMapping("/api")
public class SocialNetWorkResource {

    private final Logger log = LoggerFactory.getLogger(SocialNetWorkResource.class);

    private static final String ENTITY_NAME = "samSocialNetWork";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialNetWorkService socialNetWorkService;

    private final SocialNetWorkRepository socialNetWorkRepository;

    public SocialNetWorkResource(SocialNetWorkService socialNetWorkService, SocialNetWorkRepository socialNetWorkRepository) {
        this.socialNetWorkService = socialNetWorkService;
        this.socialNetWorkRepository = socialNetWorkRepository;
    }

    /**
     * {@code POST  /social-net-works} : Create a new socialNetWork.
     *
     * @param socialNetWork the socialNetWork to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialNetWork, or with status {@code 400 (Bad Request)} if the socialNetWork has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-net-works")
    public ResponseEntity<SocialNetWork> createSocialNetWork(@Valid @RequestBody SocialNetWork socialNetWork) throws URISyntaxException {
        log.debug("REST request to save SocialNetWork : {}", socialNetWork);
        if (socialNetWork.getId() != null) {
            throw new BadRequestAlertException("A new socialNetWork cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialNetWork result = socialNetWorkService.save(socialNetWork);
        return ResponseEntity
            .created(new URI("/api/social-net-works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-net-works/:id} : Updates an existing socialNetWork.
     *
     * @param id the id of the socialNetWork to save.
     * @param socialNetWork the socialNetWork to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetWork,
     * or with status {@code 400 (Bad Request)} if the socialNetWork is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialNetWork couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-net-works/{id}")
    public ResponseEntity<SocialNetWork> updateSocialNetWork(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialNetWork socialNetWork
    ) throws URISyntaxException {
        log.debug("REST request to update SocialNetWork : {}, {}", id, socialNetWork);
        if (socialNetWork.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetWork.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialNetWorkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialNetWork result = socialNetWorkService.update(socialNetWork);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialNetWork.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-net-works/:id} : Partial updates given fields of an existing socialNetWork, field will ignore if it is null
     *
     * @param id the id of the socialNetWork to save.
     * @param socialNetWork the socialNetWork to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetWork,
     * or with status {@code 400 (Bad Request)} if the socialNetWork is not valid,
     * or with status {@code 404 (Not Found)} if the socialNetWork is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialNetWork couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/social-net-works/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialNetWork> partialUpdateSocialNetWork(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialNetWork socialNetWork
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialNetWork partially : {}, {}", id, socialNetWork);
        if (socialNetWork.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetWork.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialNetWorkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialNetWork> result = socialNetWorkService.partialUpdate(socialNetWork);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialNetWork.getId().toString())
        );
    }

    /**
     * {@code GET  /social-net-works} : get all the socialNetWorks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialNetWorks in body.
     */
    @GetMapping("/social-net-works")
    public List<SocialNetWork> getAllSocialNetWorks() {
        log.debug("REST request to get all SocialNetWorks");
        return socialNetWorkService.findAll();
    }

    /**
     * {@code GET  /social-net-works/:id} : get the "id" socialNetWork.
     *
     * @param id the id of the socialNetWork to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialNetWork, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-net-works/{id}")
    public ResponseEntity<SocialNetWork> getSocialNetWork(@PathVariable Long id) {
        log.debug("REST request to get SocialNetWork : {}", id);
        Optional<SocialNetWork> socialNetWork = socialNetWorkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialNetWork);
    }

    /**
     * {@code DELETE  /social-net-works/:id} : delete the "id" socialNetWork.
     *
     * @param id the id of the socialNetWork to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-net-works/{id}")
    public ResponseEntity<Void> deleteSocialNetWork(@PathVariable Long id) {
        log.debug("REST request to delete SocialNetWork : {}", id);
        socialNetWorkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
