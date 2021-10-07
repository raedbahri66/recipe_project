package com.bahri.recipe_project.services;

import com.bahri.recipe_project.commands.RecipeCommand;
import com.bahri.recipe_project.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long id);


    RecipeCommand findCommandById(Long l);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    void deleteById(Long id);
}
