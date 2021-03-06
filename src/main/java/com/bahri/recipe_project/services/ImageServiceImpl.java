package com.bahri.recipe_project.services;

import com.bahri.recipe_project.domain.Recipe;
import com.bahri.recipe_project.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl( RecipeRepository recipeService) {

        this.recipeRepository = recipeService;
    }

    @Override
    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        try {
            if(recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();

                Byte[] byteObjects = new Byte[file.getBytes().length];

                int i = 0;

                for (byte b : file.getBytes()){
                    byteObjects[i++] = b;
                }

                recipe.setImage(byteObjects);

                recipeRepository.save(recipe);
            }

        } catch (IOException e) {
            //todo handle better
            log.error("Error occurred", e);
        }
    }
}
