package com.samhello.sam.service;

import com.samhello.sam.domain.SocialNetWork;
import com.samhello.sam.repository.SocialNetWorkRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SocialNetWork}.
 */
@Service
@Transactional
public class SocialNetWorkService {

    private final Logger log = LoggerFactory.getLogger(SocialNetWorkService.class);

    private final SocialNetWorkRepository socialNetWorkRepository;

    public SocialNetWorkService(SocialNetWorkRepository socialNetWorkRepository) {
        this.socialNetWorkRepository = socialNetWorkRepository;
    }

    /**
     * Save a socialNetWork.
     *
     * @param socialNetWork the entity to save.
     * @return the persisted entity.
     */
    public SocialNetWork save(SocialNetWork socialNetWork) {
        log.debug("Request to save SocialNetWork : {}", socialNetWork);
        return socialNetWorkRepository.save(socialNetWork);
    }

    /**
     * Update a socialNetWork.
     *
     * @param socialNetWork the entity to save.
     * @return the persisted entity.
     */
    public SocialNetWork update(SocialNetWork socialNetWork) {
        log.debug("Request to save SocialNetWork : {}", socialNetWork);
        return socialNetWorkRepository.save(socialNetWork);
    }

    /**
     * Partially update a socialNetWork.
     *
     * @param socialNetWork the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SocialNetWork> partialUpdate(SocialNetWork socialNetWork) {
        log.debug("Request to partially update SocialNetWork : {}", socialNetWork);

        return socialNetWorkRepository
            .findById(socialNetWork.getId())
            .map(existingSocialNetWork -> {
                if (socialNetWork.getTeacherId() != null) {
                    existingSocialNetWork.setTeacherId(socialNetWork.getTeacherId());
                }
                if (socialNetWork.getAccessToken() != null) {
                    existingSocialNetWork.setAccessToken(socialNetWork.getAccessToken());
                }
                if (socialNetWork.getExpiredTime() != null) {
                    existingSocialNetWork.setExpiredTime(socialNetWork.getExpiredTime());
                }
                if (socialNetWork.getType() != null) {
                    existingSocialNetWork.setType(socialNetWork.getType());
                }
                if (socialNetWork.getCreatedAt() != null) {
                    existingSocialNetWork.setCreatedAt(socialNetWork.getCreatedAt());
                }
                if (socialNetWork.getUpdatedAt() != null) {
                    existingSocialNetWork.setUpdatedAt(socialNetWork.getUpdatedAt());
                }

                return existingSocialNetWork;
            })
            .map(socialNetWorkRepository::save);
    }

    /**
     * Get all the socialNetWorks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SocialNetWork> findAll() {
        log.debug("Request to get all SocialNetWorks");
        return socialNetWorkRepository.findAll();
    }

    /**
     * Get one socialNetWork by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialNetWork> findOne(Long id) {
        log.debug("Request to get SocialNetWork : {}", id);
        return socialNetWorkRepository.findById(id);
    }

    /**
     * Delete the socialNetWork by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialNetWork : {}", id);
        socialNetWorkRepository.deleteById(id);
    }
}
