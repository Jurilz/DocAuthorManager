package org.example.docauthormanager.document.entities;

import jakarta.validation.constraints.NotBlank;
import org.example.docauthormanager.author.entities.Author;

import java.util.Set;

public record DocumentDTO(
        @NotBlank(message = "Documents must have a title") String title,
        @NotBlank(message = "Documents must have a body") String body,
        Set<Author> authors,
        Set<Document> references) {
}
