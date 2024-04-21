package org.example.docauthormanager.author.service;

import org.example.docauthormanager.author.entities.Author;

import java.util.List;

public interface AuthorService {

    Author findById(Long authorId);

    List<Author> findAllAuthors();

    Author createAuthor(Author author);

    Author updateAuthor(Long authorId, Author author);

    void deleteAuthorById(Long authorId);
}
