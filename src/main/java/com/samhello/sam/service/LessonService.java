package com.samhello.sam.service;

import com.samhello.sam.domain.Lesson;
import com.samhello.sam.repository.LessonRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lesson}.
 */
@Service
@Transactional
public class LessonService {

    private final Logger log = LoggerFactory.getLogger(LessonService.class);

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    /**
     * Save a lesson.
     *
     * @param lesson the entity to save.
     * @return the persisted entity.
     */
    public Lesson save(Lesson lesson) {
        log.debug("Request to save Lesson : {}", lesson);
        return lessonRepository.save(lesson);
    }

    /**
     * Update a lesson.
     *
     * @param lesson the entity to save.
     * @return the persisted entity.
     */
    public Lesson update(Lesson lesson) {
        log.debug("Request to save Lesson : {}", lesson);
        return lessonRepository.save(lesson);
    }

    /**
     * Partially update a lesson.
     *
     * @param lesson the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Lesson> partialUpdate(Lesson lesson) {
        log.debug("Request to partially update Lesson : {}", lesson);

        return lessonRepository
            .findById(lesson.getId())
            .map(existingLesson -> {
                if (lesson.getTeacherId() != null) {
                    existingLesson.setTeacherId(lesson.getTeacherId());
                }
                if (lesson.getTitle() != null) {
                    existingLesson.setTitle(lesson.getTitle());
                }
                if (lesson.getContent() != null) {
                    existingLesson.setContent(lesson.getContent());
                }
                if (lesson.getPrice() != null) {
                    existingLesson.setPrice(lesson.getPrice());
                }
                if (lesson.getTime() != null) {
                    existingLesson.setTime(lesson.getTime());
                }
                if (lesson.getCreatedAt() != null) {
                    existingLesson.setCreatedAt(lesson.getCreatedAt());
                }
                if (lesson.getUpdatedAt() != null) {
                    existingLesson.setUpdatedAt(lesson.getUpdatedAt());
                }

                return existingLesson;
            })
            .map(lessonRepository::save);
    }

    /**
     * Get all the lessons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Lesson> findAll(Pageable pageable) {
        log.debug("Request to get all Lessons");
        return lessonRepository.findAll(pageable);
    }

    /**
     * Get one lesson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Lesson> findOne(Long id) {
        log.debug("Request to get Lesson : {}", id);
        return lessonRepository.findById(id);
    }

    /**
     * Delete the lesson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lesson : {}", id);
        lessonRepository.deleteById(id);
    }
}
