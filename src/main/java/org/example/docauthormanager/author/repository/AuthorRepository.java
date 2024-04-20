package org.example.docauthormanager.author.repository;

import org.example.docauthormanager.author.entities.AuthorDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorDO, Long> { }