package org.example.docauthormanager;

import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = AuthorController.AUTHORS_PATH)
public class AuthorController {

    public static final String AUTHORS_PATH = "/authors";
    public static final String ID_PATH = "/{id}";

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(ID_PATH)
    public Author findAuthorById(@PathVariable Long id) {
        return authorService.findById(id);
    }


}
