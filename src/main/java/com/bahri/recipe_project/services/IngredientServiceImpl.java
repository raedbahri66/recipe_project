package com.bahri.recipe_project.services;

import com.bahri.recipe_project.commands.IngredientCommand;
import com.bahri.recipe_project.converters.IngredientCommandToIngredient;
import com.bahri.recipe_project.converters.IngredientToIngredientCommand;
import com.bahri.recipe_project.domain.Ingredient;
import com.bahri.recipe_project.domain.Recipe;
import com.bahri.recipe_project.repositories.RecipeRepository;
import com.bahri.recipe_project.repositories.UnitOfMesureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMesureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 RecipeRepository recipeRepository, UnitOfMesureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()){
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
            return new IngredientCommand();
        } else {

            Recipe recipe = recipeOptional.get();

            Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .map(ingredientToIngredientCommand::convert).findFirst();


            if (!ingredientCommandOptional.isPresent()) {
                //todo impl error handling
                log.error("Ingredient id not found: " + ingredientId);
                return new IngredientCommand();
            } else {
                return ingredientCommandOptional.get();
            }
        }
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                if (ingredient != null) {
                    ingredient.setRecipe(recipe);
                    recipe.addIngredient(ingredient);
                }
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if(!savedIngredientOptional.isPresent()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            //to do check for fail
            if(savedIngredientOptional.isPresent())
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            else return new IngredientCommand();
        }

    }

    @Override
    public void deleteIngredient(Long recipeId, Long id) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredient = recipe.getIngredients()
                    .stream()
                    .filter(ingredient1 ->ingredient1.getId().equals(id))
                    .findFirst();

            if (ingredient.isPresent()) {
                Ingredient ingredient1 = ingredient.get();
                ingredient1.setRecipe(null);
                recipe.getIngredients().remove(ingredient.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }

    }
}
