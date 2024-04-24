package org.example.docauthormanager.document.repository;

import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.document.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findDocumentByTitleAndAuthors(String title, Set<Author> authors);
}