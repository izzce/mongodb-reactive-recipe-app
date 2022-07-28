package org.izce.mongodb_recipe.services;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

@Service
public class ImageServiceImpl implements ImageService {	
    private final RecipeReactiveRepository recipeRepo;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }
    
    @Override
    public Mono<Void> save(final String recipeId, final MultipartFile file) {
    	recipeRepo.findById(recipeId).map(recipe -> {
    		 Byte[] byteObjects;
			try {
				byteObjects = ArrayUtils.toObject(file.getBytes());
				recipe.setImage(byteObjects);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return recipe;
    	}).publish(recipeMono -> recipeRepo.save(recipeMono.block())) ;
        
        return Mono.empty();
    }

	@Override
	public Mono<Void> save(MultipartFile file) {
		// TODO Auto-generated method stub
		return Mono.empty();
	}

}

