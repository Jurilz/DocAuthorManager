package org.example.docauthormanager.document.service;

import org.example.docauthormanager.document.entities.Document;

import java.util.List;

public interface DocumentService {

    Document findById(Long documentId);

    List<Document> findAllDocuments();

    Document createDocument(Document document);

    Document updateDocument(Long documentId, Document document);

    void deleteDocument(Long documentId);
}