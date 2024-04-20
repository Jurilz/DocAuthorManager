package org.example.docauthormanager.author.service;

import org.example.docauthormanager.author.entities.Author;

import java.util.List;

public interface AuthorService {

    Author findById(Long authorId);

    List<Author> findAllAuthors();

    Author createAuthor(Author author);

    Author updateAuthor(Author author, Long authorId);

    void deleteAuthorById(Long authorId);
}
