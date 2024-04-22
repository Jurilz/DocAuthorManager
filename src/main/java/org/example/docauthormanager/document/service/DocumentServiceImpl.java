package org.example.docauthormanager.document.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.document.converter.DocumentDTOConverter;
import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.entities.DocumentDTO;
import org.example.docauthormanager.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentDTOConverter documentDTOConverter;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentDTOConverter documentDTOConverter) {
        this.documentRepository = documentRepository;
        this.documentDTOConverter = documentDTOConverter;
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
    public Document createDocument(final DocumentDTO document) {
        return documentRepository.save(documentDTOConverter.convert(document));
    }

    @Override
    public Document updateDocument(final Long documentId, final DocumentDTO newDocument) {
        return documentRepository.findById(documentId)
                .map(document -> {
                    document.setTitle(newDocument.title());
                    document.setAuthors(newDocument.authors());
                    document.setBody(newDocument.body());
                    document.setReferences(newDocument.references());
                    return documentRepository.save(document);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }
}