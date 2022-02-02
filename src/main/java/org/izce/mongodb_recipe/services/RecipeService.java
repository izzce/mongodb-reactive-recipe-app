package org.izce.mongodb_recipe.services;

import java.util.List;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Recipe;

public interface RecipeService {
	Iterable<Recipe> getRecipes();
	Long getRecipesCount();
	Recipe findById(String id);
	RecipeCommand findRecipeCommandById(String id);
	RecipeCommand saveRecipeCommand(RecipeCommand command);
	CategoryCommand findCategoryByDescription(String description);
	UnitOfMeasureCommand findUom(String uom);
	UnitOfMeasureCommand findUom(String uomId, boolean flag);
	List<UnitOfMeasureCommand> findAllUoms();
	void delete(String recipeId);
}
