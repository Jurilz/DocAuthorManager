package org.example.docauthormanager.document.repository;

import org.example.docauthormanager.document.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}