package org.izce.mongodb_recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.bootstrap.BootStrap_MongoDB;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.converters.RecipeCommandToRecipe;
import org.izce.mongodb_recipe.converters.RecipeToRecipeCommand;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.repositories.reactive.CategoryReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.IngredientReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.UomReactiveRepository;
import org.izce.mongodb_recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@AutoConfigureDataMongo
@SpringBootTest()
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeReactiveRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;
    
    @Autowired
    CategoryReactiveRepository categoryRepo;
    
    @Autowired
    UomReactiveRepository uomRepo;
    
    @Autowired
    IngredientReactiveRepository ingredientRepo;

	
    @BeforeEach
    public void setUp() {
    	categoryRepo.deleteAll();
    	uomRepo.deleteAll();
    	
    	BootStrap_MongoDB bootstrap = new BootStrap_MongoDB();
    	bootstrap.onApplicationEvent(null);
    }
    
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        for (Recipe recipe : recipeRepository.findAll().collectList().block()) {
	        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
	
	        //when
	        recipeCommand.setDescription(NEW_DESCRIPTION);
	        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand).block();
	
	        //then
	        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
	        assertEquals(recipe.getId(), savedRecipeCommand.getId());
	        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
	        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
        }
    }
}