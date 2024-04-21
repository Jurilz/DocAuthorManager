package org.example.docauthormanager.document.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document findById(final Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document createDocument(final Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(final Long documentId, final Document newDocument) {
        return documentRepository.findById(documentId)
                .map(document -> {
                    document.setTitle(newDocument.getTitle());
                    document.setAuthors(newDocument.getAuthors());
                    document.setBody(newDocument.getBody());
                    document.setReferences(newDocument.getReferences());
                    return documentRepository.save(document);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }
}