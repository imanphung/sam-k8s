package com.samhello.sam.repository;

import com.samhello.sam.domain.Deposit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deposit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {}
