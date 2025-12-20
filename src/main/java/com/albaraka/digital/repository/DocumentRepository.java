package com.albaraka.digital.repository;

import com.albaraka.digital.model.entity.Document;
import com.albaraka.digital.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByOperation(Operation operation);
}