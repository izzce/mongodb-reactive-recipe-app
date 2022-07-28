package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.model.Direction;

import reactor.core.publisher.Mono;

public interface DirectionService {
	Mono<Direction> findById(String id);
	Mono<DirectionCommand> findDirectionCommandById(String id);
	Mono<DirectionCommand> saveDirectionCommand(DirectionCommand command);
	Mono<Void> delete(String directionId);
}
