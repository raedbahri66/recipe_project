package com.bahri.recipe_project.repositories;

import com.bahri.recipe_project.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UnitOfMesureRepositoryTest {

    @Autowired
    UnitOfMesureRepository unitOfMesureRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByUom() {
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMesureRepository.findByUom("Tablespoon");
        assertEquals("Tablespoon",unitOfMeasure.get().getUom());
    }
}