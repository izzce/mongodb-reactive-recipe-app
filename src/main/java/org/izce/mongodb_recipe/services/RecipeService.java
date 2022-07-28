package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Recipe;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
	Flux<Recipe> getRecipes();
	Mono<Long> getRecipesCount();
	Mono<Recipe> findById(String id);
	Mono<RecipeCommand> findRecipeCommandById(String id);
	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
	Mono<CategoryCommand> findCategoryByDescription(String description);
	Mono<UnitOfMeasureCommand> findUom(String uom);
	Mono<UnitOfMeasureCommand> findUom(String uomId, boolean flag);
	Flux<UnitOfMeasureCommand> findAllUoms();
	Mono<Void> delete(String recipeId);
}
