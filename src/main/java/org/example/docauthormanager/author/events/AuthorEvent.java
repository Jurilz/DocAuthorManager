package org.example.docauthormanager.author.events;


import org.example.docauthormanager.author.entities.Author;

public record AuthorEvent(
    EventType type,
    Author author) {
}