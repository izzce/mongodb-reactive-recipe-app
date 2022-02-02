package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final DirectionCommandToDirection directionConverter;
    private final NoteCommandToNote notesConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConveter, 
    							 IngredientCommandToIngredient ingredientConverter,
    							 DirectionCommandToDirection directionConverter,
                                 NoteCommandToNote notesConverter) {
        this.categoryConverter = categoryConveter;
        this.ingredientConverter = ingredientConverter;
        this.directionConverter = directionConverter;
        this.notesConverter = notesConverter;
    }

    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if (source == null) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setImageUrl(source.getImageUrl());
        
        if (source.getCategories() != null && source.getCategories().size() > 0){
            source.getCategories()
                    .forEach( category -> recipe.getCategories().add(categoryConverter.convert(category)));
        }

        if (source.getIngredients() != null && source.getIngredients().size() > 0){
            source.getIngredients()
                    .forEach(ingredient -> recipe.getIngredients().add(ingredientConverter.convert(ingredient)));
        }
        
        if (source.getDirections() != null && source.getDirections().size() > 0){
            source.getDirections()
                    .forEach(direction -> recipe.getDirections().add(directionConverter.convert(direction)));
        }

        if (source.getNotes() != null && source.getNotes().size() > 0){
            source.getNotes()
                    .forEach(note -> recipe.getNotes().add(notesConverter.convert(note)));
        }

        return recipe;
    }
    
}
