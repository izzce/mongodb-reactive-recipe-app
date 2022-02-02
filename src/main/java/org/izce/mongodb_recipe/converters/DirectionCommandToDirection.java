package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DirectionCommandToDirection implements Converter<DirectionCommand, Direction> {


    public DirectionCommandToDirection() {
    }

    @Nullable
    @Override
    public Direction convert(DirectionCommand source) {
        if (source == null) {
            return null;
        }

        final var direction = new Direction();
        direction.setId(source.getId());
        direction.setDirection(source.getDirection());
        if (source.getRecipeId() != null) {
        	Recipe recipe = new Recipe();
        	recipe.setId(source.getRecipeId());
        	direction.setRecipe(recipe);
        }
        return direction;
    }
}