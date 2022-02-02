package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

	private final CategoryToCategoryCommand categoryConverter;
	private final IngredientToIngredientCommand ingredientConverter;
	private final DirectionToDirectionCommand directionConverter;
	private final NoteToNoteCommand noteConverter;

	public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConveter,
			IngredientToIngredientCommand ingredientConverter, DirectionToDirectionCommand directionConverter,
			NoteToNoteCommand notesConverter) {
		this.categoryConverter = categoryConveter;
		this.ingredientConverter = ingredientConverter;
		this.directionConverter = directionConverter;
		this.noteConverter = notesConverter;
	}

	@Nullable
	@Override
	public RecipeCommand convert(Recipe source) {
		if (source == null) {
			return null;
		}

		final RecipeCommand command = new RecipeCommand();
		command.setId(source.getId());
		command.setCookTime(source.getCookTime());
		command.setPrepTime(source.getPrepTime());
		command.setDescription(source.getDescription());
		command.setDifficulty(source.getDifficulty());
		command.setServings(source.getServings());
		command.setSource(source.getSource());
		command.setUrl(source.getUrl());
		command.setImageUrl(source.getImageUrl());

		if (source.getCategories() != null && source.getCategories().size() > 0) {
			source.getCategories().forEach((Category c) -> command.getCategories().add(categoryConverter.convert(c)));
		}

		if (source.getIngredients() != null && source.getIngredients().size() > 0) {
			source.getIngredients().forEach(i -> command.getIngredients().add(ingredientConverter.convert(i)));
		}

		if (source.getDirections() != null && source.getDirections().size() > 0) {
			source.getDirections().forEach(d -> command.getDirections().add(directionConverter.convert(d)));
		}

		if (source.getNotes() != null && source.getNotes().size() > 0) {
			source.getNotes().forEach(n -> command.getNotes().add(noteConverter.convert(n)));
		}

		return command;
	}
}
