package org.example.docauthormanager.author.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.docauthormanager.author.converter.AuthorDTOConverter;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;
import org.example.docauthormanager.author.service.AuthorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: think about pagination

@RestController
@RequestMapping(path = AuthorController.AUTHORS_PATH)
public class AuthorController {

    protected static final String AUTHORS_PATH = "/authors";
    protected static final String ID_PATH = "/{id}";

    private final AuthorService authorService;
    private final AuthorDTOConverter authorDTOConverter;

    public AuthorController(AuthorService authorService, AuthorDTOConverter authorDTOConverter) {
        this.authorService = authorService;
        this.authorDTOConverter = authorDTOConverter;
    }

    @Operation(summary = "Get an author by his/her id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The looked up author",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @GetMapping(ID_PATH)
    public Author findAuthorById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @Operation(summary = "Returns all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All authors",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Author.class)))
                    }),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @GetMapping
    public List<Author> findAll() {
        return authorService.findAllAuthors();
    }

    @Operation(summary = "Saves a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author saved",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @PostMapping
    public Author createAuthor(@Valid @RequestBody AuthorDTO author) {
        return authorService.createAuthor(authorDTOConverter.convert(author));
    }

    @Operation(summary = "Updates an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @PutMapping(ID_PATH)
    public Author updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO author) {
        return authorService.updateAuthor(id, authorDTOConverter.convert(author));
    }

    @Operation(summary = "Deletes an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted"),
            @ApiResponse(responseCode = "401", description = "The user is not authenticated")
    })
    @DeleteMapping(ID_PATH)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }
}
