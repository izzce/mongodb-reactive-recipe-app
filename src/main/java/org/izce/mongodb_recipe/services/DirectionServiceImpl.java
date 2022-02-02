package org.izce.mongodb_recipe.services;

import java.util.Optional;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.converters.DirectionCommandToDirection;
import org.izce.mongodb_recipe.converters.DirectionToDirectionCommand;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.repositories.DirectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DirectionServiceImpl implements DirectionService {
    private final DirectionRepository directionRepo;
    private final DirectionCommandToDirection dc2d;
    private final DirectionToDirectionCommand d2dc;

	@Autowired
	public DirectionServiceImpl(
			DirectionRepository ir,
			DirectionCommandToDirection dc2d,
			DirectionToDirectionCommand d2dc) {
		
		log.debug("Initializing DirectionServiceImpl...");
		this.directionRepo = ir;
		this.dc2d = dc2d;
		this.d2dc = d2dc;
	}
	
	@Override
	public Direction findById(String id) {
		Optional<Direction> directionOptional = directionRepo.findById(id);
		return directionOptional.orElseThrow(() -> new RuntimeException("Direction not found: " + id));
	}

	@Override
	public DirectionCommand findDirectionCommandById(String id) {
		return d2dc.convert(findById(id));
	}
	
	@Override
	@Transactional
	public DirectionCommand saveDirectionCommand(DirectionCommand directionCommand) {
		Direction direction = dc2d.convert(directionCommand);
		direction = directionRepo.save(direction);
		
		log.info("Saved Direction: {}", direction);
		
		return d2dc.convert(direction);
	}
	
	@Override
	@Transactional
	public void delete(String directionId) {
		directionRepo.deleteById(directionId);
		 
		log.info("Deleted Direction: {}", directionId);		 
	}
}

