package org.izce.mongodb_recipe.services;

import java.util.List;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Ingredient;

public interface IngredientService {
	Ingredient findById(String id);
	IngredientCommand findIngredientCommandById(String id);
	IngredientCommand saveIngredientCommand(IngredientCommand command);
	UnitOfMeasureCommand findUom(String uom);
	UnitOfMeasureCommand findUom(String uomId, boolean flag);
	List<UnitOfMeasureCommand> findAllUoms();
	void delete(String ingredientId);
}
