package org.izce.mongodb_recipe.services;

import java.io.IOException;

import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {	
    private final RecipeRepository recipeRepo;

    public ImageServiceImpl(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }
    
    @Override
    @Transactional
    public void save(String recipeId, MultipartFile file) {

        try {
            Recipe recipe = recipeRepo.findById(recipeId).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);

            recipeRepo.save(recipe);
        } catch (IOException e) {
        	log.error("Error occurred", e);
            e.printStackTrace();
        }
    }

	@Override
	public void save(MultipartFile file) {
		// TODO Auto-generated method stub
	}

}

