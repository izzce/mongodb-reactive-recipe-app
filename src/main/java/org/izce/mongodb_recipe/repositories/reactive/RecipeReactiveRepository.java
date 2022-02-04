package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
	Mono<Recipe> findByDescription(String description);
}
