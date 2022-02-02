package org.izce.mongodb_recipe.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	
	/**
	 * Saves the file to DB with no relation to any recipe. 
	 * 
	 * @param file the image file to be saved. 
	 */
	void save(MultipartFile file);
	
	/**
	 * Saves the file to DB in relation with Recipe 
	 * 
	 * @param recipeId The id of the recipe. 
	 * @param file the image file to be saved. 
	 */
	void save(String recipeId, MultipartFile file);
	
}
