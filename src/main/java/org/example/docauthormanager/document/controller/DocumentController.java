package org.example.docauthormanager.document.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = DocumentController.DOCUMENTS_PATH)
public class DocumentController {
    protected static final String DOCUMENTS_PATH = "/documents";
    protected static final String ID_PATH = "/{id}";

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Get a document by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The looked up document",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @GetMapping(ID_PATH)
    public Document findDocumentById(@PathVariable Long id) {
        return documentService.findById(id);
    }

    @Operation(summary = "Returns all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All documents",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Document.class)))
                    }),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @GetMapping
    public List<Document> findAll() {
        return documentService.findAllDocuments();
    }

    @Operation(summary = "Saves a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document saved",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(name = "documentSaved", value = "{\"id\": 42, \"title\": \"Important Document\", \"body\": \"This document contains top-secret information about the secret recipe for chocolate cake.\", \"authors\": [{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}], \"references\": [{\"id\": 23, \"title\": \"Cookbook\", \"body\": \"A cookbook containing delicious recipes.\"}]}")
                                    },
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @PostMapping
    public Document createDocument(@RequestBody Document document) {
        return documentService.createDocument(document);
    }

    @Operation(summary = "Updates a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Document.class))}),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @PutMapping(ID_PATH)
    public Document updateDocument(@PathVariable Long id, @RequestBody Document document) {
        return documentService.updateDocument(id, document);
    }

    @Operation(summary = "Deletes a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document deleted"),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @DeleteMapping(ID_PATH)
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }
}