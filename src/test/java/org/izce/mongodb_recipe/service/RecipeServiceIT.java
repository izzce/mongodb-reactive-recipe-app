package org.izce.mongodb_recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.bootstrap.BootStrap_MongoDB;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.converters.RecipeCommandToRecipe;
import org.izce.mongodb_recipe.converters.RecipeToRecipeCommand;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.repositories.CategoryRepository;
import org.izce.mongodb_recipe.repositories.IngredientRepository;
import org.izce.mongodb_recipe.repositories.RecipeRepository;
import org.izce.mongodb_recipe.repositories.UnitOfMeasureRepository;
import org.izce.mongodb_recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

// FIXME Fix @DBRef UnitOfMeasure uom while loading Ingredient model. 
@Disabled
@AutoConfigureDataMongo
@SpringBootTest()
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;
    
    @Autowired
    CategoryRepository categoryRepo;
    
    @Autowired
    UnitOfMeasureRepository uomRepo;
    
    @Autowired
    IngredientRepository ingredientRepo;

	
    @BeforeEach
    public void setUp() {
    	categoryRepo.deleteAll();
    	uomRepo.deleteAll();
    	
    	BootStrap_MongoDB bootstrap = new BootStrap_MongoDB(categoryRepo, uomRepo);
    	bootstrap.onApplicationEvent(null);
    }
    
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        for (Recipe recipe : recipeRepository.findAll()) {
	        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
	
	        //when
	        recipeCommand.setDescription(NEW_DESCRIPTION);
	        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
	
	        //then
	        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
	        assertEquals(recipe.getId(), savedRecipeCommand.getId());
	        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
	        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
        }
    }
}