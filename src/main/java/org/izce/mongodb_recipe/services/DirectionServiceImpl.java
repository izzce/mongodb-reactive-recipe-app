package org.izce.mongodb_recipe.services;

import java.util.NoSuchElementException;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.converters.DirectionCommandToDirection;
import org.izce.mongodb_recipe.converters.DirectionToDirectionCommand;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.repositories.reactive.DirectionReactiveRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DirectionServiceImpl implements DirectionService {
    private final DirectionReactiveRepository directionRepo;
    private final DirectionCommandToDirection dc2d;
    private final DirectionToDirectionCommand d2dc;

	public DirectionServiceImpl(
			DirectionReactiveRepository ir,
			DirectionCommandToDirection dc2d,
			DirectionToDirectionCommand d2dc) {
		
		log.debug("Initializing DirectionServiceImpl...");
		this.directionRepo = ir;
		this.dc2d = dc2d;
		this.d2dc = d2dc;
	}
	
	@Override
	public Mono<Direction> findById(String id) {
		return directionRepo.findById(id).switchIfEmpty(Mono.defer(() -> {
			throw new NoSuchElementException("Direction not found: " + id);
		}));
	}

	@Override
	public Mono<DirectionCommand> findDirectionCommandById(String id) {
		return this.findById(id).map(d2dc::convert);
	}
	
	@Override
	public Mono<DirectionCommand> saveDirectionCommand(DirectionCommand directionCommand) {
		Direction direction = dc2d.convert(directionCommand);
		return directionRepo.save(direction).map(d2dc::convert);
	}
	
	@Override
	public Mono<Void> delete(String directionId) {
		return directionRepo.deleteById(directionId);
	}
}

