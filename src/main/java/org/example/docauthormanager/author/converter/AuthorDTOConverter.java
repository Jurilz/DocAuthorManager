package org.example.docauthormanager.author.converter;

import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDTOConverter implements Converter<AuthorDTO, Author> {
    @Override
    public Author convert(AuthorDTO source) {
        return new Author(
            source.firstName(),
            source.latName()
        );
    }
}