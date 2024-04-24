package org.example.docauthormanager.author.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.author.converter.AuthorDTOConverter;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;
import org.example.docauthormanager.author.events.AuthorEvent;
import org.example.docauthormanager.author.events.EventType;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.example.docauthormanager.messages.rabbitmq.AuthorMessageSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Author createdAuthor = authorRepository.save(authorDTOConverter.convert(authorDTO));

        final AuthorEvent event = new AuthorEvent(EventType.CREATED, createdAuthor);
        messageSender.sendAuthorMessage(event);

        return createdAuthor;
    }

    @Override
    public Author updateAuthor(final Long authorId, final AuthorDTO newAuthor) {
        Optional<Author> author = authorRepository.findById(authorId);

        return author.map(authorToUpdate -> {
            authorToUpdate.setFirstName(newAuthor.firstName());
            authorToUpdate.setLastName(newAuthor.lastName());

            final AuthorEvent event = new AuthorEvent(EventType.UPDATED, authorToUpdate);
            messageSender.sendAuthorMessage(event);

            return authorRepository.save(authorToUpdate);
        }).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }
}
