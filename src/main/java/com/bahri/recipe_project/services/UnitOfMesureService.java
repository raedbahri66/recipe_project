package com.bahri.recipe_project.services;

import com.bahri.recipe_project.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMesureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
