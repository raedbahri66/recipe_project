package com.bahri.recipe_project.services;


import com.bahri.recipe_project.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteIngredient(Long recipeId, Long id);
}
