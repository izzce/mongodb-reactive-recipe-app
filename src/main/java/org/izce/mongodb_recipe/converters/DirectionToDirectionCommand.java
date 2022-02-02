package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.model.Direction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DirectionToDirectionCommand implements Converter<Direction, DirectionCommand>{

    @Nullable
    @Override
    public DirectionCommand convert(Direction source) {
        if (source == null) {
            return null;
        }

        final var directionCommand = new DirectionCommand();
        directionCommand.setId(source.getId());
        directionCommand.setDirection(source.getDirection());
        return directionCommand;
    }
}