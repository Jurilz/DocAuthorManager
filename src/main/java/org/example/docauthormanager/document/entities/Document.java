package org.example.docauthormanager.document.entities;

import jakarta.persistence.*;
import org.example.docauthormanager.author.entities.Author;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToMany
    @JoinTable(
            name = "document_authors",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "document_references",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "referenced_document_id")
    )
    private Set<Document> references = new HashSet<>();

    public Document() {}

    public Document(final String title, final String body, final Set<Author> authors, final Set<Document> references) {
        this.title = title;
        this.body = body;
        this.authors = authors;
        this.references = references;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Document> getReferences() {
        return references;
    }

    public void setReferences(Set<Document> references) {
        this.references = references;
    }
}