package com.samhello.sam.web.rest;

import com.samhello.sam.domain.Lesson;
import com.samhello.sam.repository.LessonRepository;
import com.samhello.sam.service.LessonService;
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
 * REST controller for managing {@link com.samhello.sam.domain.Lesson}.
 */
@RestController
@RequestMapping("/api")
public class LessonResource {

    private final Logger log = LoggerFactory.getLogger(LessonResource.class);

    private static final String ENTITY_NAME = "samLesson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonService lessonService;

    private final LessonRepository lessonRepository;

    public LessonResource(LessonService lessonService, LessonRepository lessonRepository) {
        this.lessonService = lessonService;
        this.lessonRepository = lessonRepository;
    }

    /**
     * {@code POST  /lessons} : Create a new lesson.
     *
     * @param lesson the lesson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lesson, or with status {@code 400 (Bad Request)} if the lesson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lessons")
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody Lesson lesson) throws URISyntaxException {
        log.debug("REST request to save Lesson : {}", lesson);
        if (lesson.getId() != null) {
            throw new BadRequestAlertException("A new lesson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lesson result = lessonService.save(lesson);
        return ResponseEntity
            .created(new URI("/api/lessons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lessons/:id} : Updates an existing lesson.
     *
     * @param id the id of the lesson to save.
     * @param lesson the lesson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lesson,
     * or with status {@code 400 (Bad Request)} if the lesson is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lesson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lessons/{id}")
    public ResponseEntity<Lesson> updateLesson(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Lesson lesson
    ) throws URISyntaxException {
        log.debug("REST request to update Lesson : {}, {}", id, lesson);
        if (lesson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lesson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lesson result = lessonService.update(lesson);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lesson.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lessons/:id} : Partial updates given fields of an existing lesson, field will ignore if it is null
     *
     * @param id the id of the lesson to save.
     * @param lesson the lesson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lesson,
     * or with status {@code 400 (Bad Request)} if the lesson is not valid,
     * or with status {@code 404 (Not Found)} if the lesson is not found,
     * or with status {@code 500 (Internal Server Error)} if the lesson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lessons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lesson> partialUpdateLesson(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lesson lesson
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lesson partially : {}, {}", id, lesson);
        if (lesson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lesson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lesson> result = lessonService.partialUpdate(lesson);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lesson.getId().toString())
        );
    }

    /**
     * {@code GET  /lessons} : get all the lessons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessons in body.
     */
    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getAllLessons(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Lessons");
        Page<Lesson> page = lessonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lessons/:id} : get the "id" lesson.
     *
     * @param id the id of the lesson to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lesson, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lessons/{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable Long id) {
        log.debug("REST request to get Lesson : {}", id);
        Optional<Lesson> lesson = lessonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lesson);
    }

    /**
     * {@code DELETE  /lessons/:id} : delete the "id" lesson.
     *
     * @param id the id of the lesson to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        log.debug("REST request to delete Lesson : {}", id);
        lessonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
