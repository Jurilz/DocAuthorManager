package org.example.docauthormanager.author.service;

import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;

import java.util.List;

public interface AuthorService {

    Author findById(Long authorId);

    List<Author> findAllAuthors();

    Author createAuthor(AuthorDTO author);

    Author updateAuthor(Long authorId, AuthorDTO author);

    void deleteAuthorById(Long authorId);
}
