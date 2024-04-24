package org.example.docauthormanager.documents;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.docauthormanager.document.entities.Document;
import org.example.docauthormanager.document.entities.DocumentDTO;
import org.example.docauthormanager.document.repository.DocumentRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {

    protected static final String DOCUMENTS_ENDPOINT = "/documents";
    protected static final String PASSWORD = "password";
    protected static final String ADMIN_ROLE = "ADMIN";
    protected static final String USERNAME = "admin";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DocumentRepository repository;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testCreate() throws Exception {

        final DocumentDTO documentDTO = new DocumentDTO(
                "Brothers Karamazow",
                "Dostojewski",
                null,
                null
        );

        Mockito.when(repository.save(any(Document.class)))
                .thenAnswer(invocationOnMock -> Arrays.stream(invocationOnMock.getArguments()).toList().get(0));

        mockMvc.perform(post(DOCUMENTS_ENDPOINT)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE))
                        .content(objectMapper.writeValueAsBytes(documentDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(documentDTO.title()))
                .andExpect(jsonPath("$.body").value(documentDTO.body()));
    }

    @Test
    public void testFindById() throws Exception {
        final Document document = new Document(
                "Brothers Karamazow",
                "Dostojewski",
                null,
                null
        );

        long documentId = 23L;
        Mockito.when(repository.findById(documentId))
                .thenAnswer(invocationOnMock -> Optional.of(document));

        mockMvc.perform(get(DOCUMENTS_ENDPOINT +  "/" + documentId)
                        .with(user(USERNAME).password(PASSWORD).roles(ADMIN_ROLE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(document.getTitle()))
                .andExpect(jsonPath("$.body").value(document.getBody()));
    }
}