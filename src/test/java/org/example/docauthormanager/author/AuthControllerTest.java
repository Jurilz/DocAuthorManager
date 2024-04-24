package org.example.docauthormanager.author;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.docauthormanager.author.entities.Author;
import org.example.docauthormanager.author.entities.AuthorDTO;
import org.example.docauthormanager.author.repository.AuthorRepository;
import org.example.docauthormanager.messages.rabbitmq.AuthorMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    protected static final String AUTHORS_ENDPOINT = "/authors";
    protected static final String PASSWORD = "password";
    protected static final String ADMIN_ROLE = "ADMIN";
    protected static final String USERNAME = "admin";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthorRepository repository;

    @MockBean
    AuthorMessageSender messageSender;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testCreate() throws Exception {

        final AuthorDTO authorDTO = new AuthorDTO(
                "Fjodor",
                "Dostojewski"
        );

        Mockito.when(repository.save(any(Author.class)))
                .thenAnswer(invocationOnMock -> Arrays.stream(invocationOnMock.getArguments()).toList().get(0));

        mockMvc.perform(post(AUTHORS_ENDPOINT)
                .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE))
                .content(objectMapper.writeValueAsBytes(authorDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(authorDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(authorDTO.lastName()));
    }

    @Test
    public void testCreateWithWrongContent() throws Exception {

        mockMvc.perform(post(AUTHORS_ENDPOINT)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE))
                        .content("{\"field\": \"does not exist\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindById() throws Exception {
        final Author author = new Author(
                "Michael",
                "Bulgakow"
        );

        long authorId = 42L;
        Mockito.when(repository.findById(authorId))
                .thenAnswer(invocationOnMock -> Optional.of(author));

        mockMvc.perform(get(AUTHORS_ENDPOINT +  "/" + authorId)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(author.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(author.getLastName()));
    }

    @Test
    public void testFindAll() throws Exception {
        final Author authorOne = new Author(
                "Michael",
                "Bulgakow"
        );

        final Author authorTwo = new Author(
                "Nikolai",
                "Gogol"
        );

        Mockito.when(repository.findAll())
                .thenAnswer(invocationOnMock -> List.of(authorOne, authorTwo));

        mockMvc.perform(get(AUTHORS_ENDPOINT)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(authorOne.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(authorOne.getLastName()))
                .andExpect(jsonPath("$[1].firstName").value(authorTwo.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(authorTwo.getLastName()));
    }

    @Test
    public void testUpdate() throws Exception {
        final Author author = new Author(
                "Michael",
                "Bulgakow"
        );

        final AuthorDTO updatedAuthor = new AuthorDTO(
                "Lew",
                "Tolstoi"
        );


        long authorId = 42L;
        Mockito.when(repository.findById(authorId))
                .thenAnswer(invocationOnMock -> Optional.of(author));

        Mockito.when(repository.save(any(Author.class)))
                .thenAnswer(invocationOnMock -> Arrays.stream(invocationOnMock.getArguments()).toList().get(0));

        mockMvc.perform(put(AUTHORS_ENDPOINT + "/" + authorId)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE))
                        .content(objectMapper.writeValueAsBytes(updatedAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedAuthor.firstName()))
                .andExpect(jsonPath("$.lastName").value(updatedAuthor.lastName()));

        Mockito.verify(messageSender).sendAuthorMessage(any());
    }
    @Test
    public void testUpdateEntityNotFound() throws Exception {
        final AuthorDTO updatedAuthor = new AuthorDTO(
                "Michael",
                "Bulgakow"
        );

        long authorId = 43L;
        Mockito.when(repository.findById(authorId))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put(AUTHORS_ENDPOINT + "/" + authorId)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE))
                        .content(objectMapper.writeValueAsBytes(updatedAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        long authorId = 33L;
        mockMvc.perform(delete(AUTHORS_ENDPOINT + "/" + authorId)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE)))
                .andExpect(status().isOk());

        Mockito.verify(repository).deleteById(authorId);
    }
}
