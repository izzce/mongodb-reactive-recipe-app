package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.model.Note;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteCommand implements Converter<Note, NoteCommand>{

    @Nullable
    @Override
    public NoteCommand convert(Note source) {
        if (source == null) {
            return null;
        }

        final NoteCommand notesCommand = new NoteCommand();
        notesCommand.setId(source.getId());
        notesCommand.setNote(source.getNote());
        return notesCommand;
    }
}