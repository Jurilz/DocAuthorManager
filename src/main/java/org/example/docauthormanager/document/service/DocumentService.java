package org.example.docauthormanager.document.service;

import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.entities.DocumentDTO;

import java.util.List;

public interface DocumentService {

    Document findById(Long documentId);

    List<Document> findAllDocuments();

    Document createDocument(DocumentDTO document);

    Document updateDocument(Long documentId, DocumentDTO document);

    void deleteDocument(Long documentId);
}