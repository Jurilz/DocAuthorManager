package org.example.docauthormanager.document.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.example.docauthormanager.document.converter.DocumentDTOConverter;
import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.entities.DocumentDTO;
import org.example.docauthormanager.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorRepository authorRepository;
    private final DocumentDTOConverter documentDTOConverter;

    public DocumentServiceImpl(DocumentRepository documentRepository, AuthorRepository authorRepository, DocumentDTOConverter documentDTOConverter) {
        this.documentRepository = documentRepository;
        this.authorRepository = authorRepository;
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
    @Transactional
    public Document createDocument(final DocumentDTO newDocument) {
        // costly but there will be not so many authors in one document
        if (newDocument.authors() != null) {
            for (final Author author: newDocument.authors()) {
                final Optional<Author> existingAuthor = authorRepository.findAuthorByFirstNameAndLastName(author.getFirstName(), author.getLastName());
                if (existingAuthor.isEmpty()) {
                    authorRepository.save(author);
                }
            }
        }


        // costly but there will be not so references in one document
        // we do this for the first layer of references only, as wo do not want to stack transactions into transactions
       if (newDocument.references() != null) {
           for (final Document document: newDocument.references()) {
               Optional<Document> existingDocument = documentRepository.findDocumentByTitleAndAuthors(document.getTitle(), document.getAuthors());
               if (existingDocument.isEmpty()) {
                   documentRepository.save(document);
               }
           }
       }

        return documentRepository.save(documentDTOConverter.convert(newDocument));
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