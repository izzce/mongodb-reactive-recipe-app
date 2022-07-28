package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.Direction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DirectionReactiveRepository extends ReactiveMongoRepository<Direction, String> {

}
