package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UomReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String>{

}
