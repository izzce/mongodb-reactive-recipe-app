package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface UomReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String>{
	Mono<UnitOfMeasure> findByUom(String uom);
	Mono<UnitOfMeasure> findByUomIgnoreCase(String uom);
}
