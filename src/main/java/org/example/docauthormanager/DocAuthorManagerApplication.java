package org.example.docauthormanager;

import org.example.docauthormanager.author.entities.AuthorDO;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class DocAuthorManagerApplication {

    @Autowired
    AuthorRepository authorRepository;

    public static void main(String[] args) {
        SpringApplication.run(DocAuthorManagerApplication.class, args);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        List<AuthorDO> authorDOS = authorRepository.findAll();
        System.out.println(authorDOS.size());

        AuthorDO authorDO = new AuthorDO(
                "test-first",
                "test-second"
        );

        authorRepository.save(authorDO);

        authorDOS = authorRepository.findAll();
        System.out.println(authorDOS.size());

    }

}
