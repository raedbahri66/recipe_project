package com.bahri.recipe_project.services;

import com.bahri.recipe_project.commands.UnitOfMeasureCommand;
import com.bahri.recipe_project.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.bahri.recipe_project.domain.UnitOfMeasure;
import com.bahri.recipe_project.repositories.UnitOfMesureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMesureServiceImplTest {

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMesureService service;

    @Mock
    UnitOfMesureRepository unitOfMesureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new UnitOfMesureServiceImpl(unitOfMesureRepository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        unitOfMeasures.add(unitOfMeasure);

        when(unitOfMesureRepository.findAll()).thenReturn(unitOfMeasures);

        Set<UnitOfMeasureCommand> commands = service.listAllUoms();

        assertEquals(1,commands.size());
        verify(unitOfMesureRepository, times(1)).findAll();
    }
}