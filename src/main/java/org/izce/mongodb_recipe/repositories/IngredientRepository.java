package org.izce.mongodb_recipe.repositories;

import org.izce.mongodb_recipe.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
