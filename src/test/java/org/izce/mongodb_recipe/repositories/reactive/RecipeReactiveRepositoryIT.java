package org.izce.mongodb_recipe.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataMongoTest
public class RecipeReactiveRepositoryIT {
	private static final String RECIPE_NAME = "Minestrone Soup";

	@MockBean
	private StorageService storageService;

	@Autowired
	RecipeReactiveRepository repository;

	@BeforeEach
	public void setUp() throws Exception {
		repository.deleteAll().block();

		var recipe = new Recipe();
		recipe.setDescription(RECIPE_NAME);
		repository.save(recipe).block();
	}

	@Test
	public void testSave() throws Exception {
		Long count = repository.count().block();

		assertEquals(Long.valueOf(1L), count);
	}

	@Test
	public void testFindAll() throws Exception {
		var fetchedRecipe = repository.findAll().blockFirst();

		assertEquals(RECIPE_NAME, fetchedRecipe.getDescription());
	}
	
	@Test
	public void testFindByDescription() throws Exception {
		var fetchedRecipe = repository.findByDescription(RECIPE_NAME).block();

		assertEquals(RECIPE_NAME, fetchedRecipe.getDescription());
	}
	
	
}

