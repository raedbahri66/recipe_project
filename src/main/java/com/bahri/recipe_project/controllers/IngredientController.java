package com.bahri.recipe_project.controllers;

import com.bahri.recipe_project.commands.IngredientCommand;
import com.bahri.recipe_project.commands.RecipeCommand;
import com.bahri.recipe_project.commands.UnitOfMeasureCommand;
import com.bahri.recipe_project.services.IngredientService;
import com.bahri.recipe_project.services.RecipeService;
import com.bahri.recipe_project.services.UnitOfMesureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@Slf4j
@Controller
public class IngredientController {
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMesureService unitOfMesureService;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMesureService unitOfMesureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMesureService = unitOfMesureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {

        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));
        if(recipeCommand ==null) {
            throw new RuntimeException("Invalid Recipe Id");
        } else {
            IngredientCommand ingredientCommand = new IngredientCommand();
            ingredientCommand.setRecipeId(Long.valueOf(recipeId));
            model.addAttribute("ingredient",ingredientCommand);
            ingredientCommand.setUom(new UnitOfMeasureCommand());

            model.addAttribute("uomList", unitOfMesureService.listAllUoms());
        }
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showIngredient(@PathVariable String recipeId,@PathVariable String id, Model model){
        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),Long.valueOf(id)));

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

        model.addAttribute("uomList", unitOfMesureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {

        ingredientService.deleteIngredient(Long.valueOf(recipeId),Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";

    }
}
