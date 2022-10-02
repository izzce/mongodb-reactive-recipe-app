package org.izce.mongodb_recipe.services;

import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Mono;

public interface ImageService {

	/**
	 * Saves the file to DB with no relation to any recipe.
	 * 
	 * @param file the image file to be saved.
	 */
	Mono<Void> save(FilePart file);

	/**
	 * Saves the file to DB in relation with Recipe
	 * 
	 * @param recipeId The id of the recipe.
	 * @param file     the image file to be saved.
	 */
	Mono<Void> save(String recipeId, FilePart file);

}
