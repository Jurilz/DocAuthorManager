package org.example.docauthormanager.author.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.author.converter.AuthorDTOConverter;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.example.docauthormanager.messages.rabbitmq.AuthorMessageSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;
    private final AuthorDTOConverter authorDTOConverter;
    private final AuthorMessageSender messageSender;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorDTOConverter authorDTOConverter, AuthorMessageSender messageSender) {
        this.authorRepository = authorRepository;
        this.authorDTOConverter = authorDTOConverter;
        this.messageSender = messageSender;
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
    public Author createAuthor(final AuthorDTO authorDTO) {
        return authorRepository.save(authorDTOConverter.convert(authorDTO));
    }

    @Override
    public Author updateAuthor(final Long authorId, final AuthorDTO newAuthor) {
        messageSender.sendAuthorMessage(newAuthor.toString());

        return authorRepository.findById(authorId)
                .map(authorToUpdate -> {
                    authorToUpdate.setFirstName(newAuthor.firstName());
                    authorToUpdate.setLastName(newAuthor.latName());
                    return authorRepository.save(authorToUpdate);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }
}
