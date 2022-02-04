package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
	Mono<Category> findByDescription(String description);
	Mono<Category> findByDescriptionIgnoreCase(String description);
}
