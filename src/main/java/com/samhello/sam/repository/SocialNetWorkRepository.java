package com.samhello.sam.repository;

import com.samhello.sam.domain.SocialNetWork;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SocialNetWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialNetWorkRepository extends JpaRepository<SocialNetWork, Long> {}
