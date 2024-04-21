package org.example.docauthormanager.author.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findById(final Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author createAuthor(final Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(final Long authorId, final Author newAuthor) {
        return authorRepository.findById(authorId)
                .map(authorToUpdate -> {
                    authorToUpdate.setFirstName(newAuthor.getFirstName());
                    authorToUpdate.setLastName(newAuthor.getLastName());
                    return authorRepository.save(authorToUpdate);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }
}
