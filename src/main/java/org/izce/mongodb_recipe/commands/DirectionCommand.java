package org.izce.mongodb_recipe.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionCommand {
	private String id;
	@NonNull
	private String direction;
	private String recipeId;
	
	public DirectionCommand(String id, String direction) {
		this.id = id;
		this.direction = direction;
	}
}