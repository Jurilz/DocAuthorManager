package org.example.docauthormanager.author.entities;

import jakarta.validation.constraints.NotBlank;

public record AuthorDTO(
        @NotBlank(message = "Authors must have a first name") String firstName,
        @NotBlank(message = "Authors must have a last name") String lastName) {
}