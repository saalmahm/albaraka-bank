package com.albaraka.digital.repository;

import com.albaraka.digital.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}