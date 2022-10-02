package org.izce.mongodb_recipe.services;

import org.apache.commons.lang3.ArrayUtils;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class ImageServiceImpl implements ImageService {	
    private final RecipeReactiveRepository recipeRepo;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }
    
    @Override
    public Mono<Void> save(final String recipeId, final FilePart file) {
    	recipeRepo.findById(recipeId).map(recipe -> {
    		 Byte[] byteObjects;
			byte[] tmp = DataBufferUtils.join(file.content())
			.map(dataBuffer -> dataBuffer.asByteBuffer().array()).block();
			byteObjects = ArrayUtils.toObject(tmp);
			recipe.setImage(byteObjects);
			return recipe;
    	}).publish(recipeMono -> recipeRepo.save(recipeMono.block())) ;
        
        return Mono.empty();
    }

	@Override
	public Mono<Void> save(FilePart file) {
		return Mono.empty();
	}

}

