package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.Ingredient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient, String> {

}
