package org.izce.mongodb_recipe.repositories;

import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
