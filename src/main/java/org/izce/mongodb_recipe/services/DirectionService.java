package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.model.Direction;

public interface DirectionService {
	Direction findById(String id);
	DirectionCommand findDirectionCommandById(String id);
	DirectionCommand saveDirectionCommand(DirectionCommand command);
	void delete(String directionId);
}
