package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Ingredient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientService {
	Mono<Ingredient> findById(String id);
	Mono<IngredientCommand> findIngredientCommandById(String id);
	Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);
	Mono<UnitOfMeasureCommand> findUom(String uom);
	Mono<UnitOfMeasureCommand> findUom(String uomId, boolean flag);
	Flux<UnitOfMeasureCommand> findAllUoms();
	Mono<Void> delete(String ingredientId);
}
