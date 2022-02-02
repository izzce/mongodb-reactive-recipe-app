package org.izce.mongodb_recipe.converters;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.model.Note;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteCommandToNote implements Converter<NoteCommand, Note> {
    @Nullable
    @Override
    public Note convert(NoteCommand source) {
        if(source == null) {
            return null;
        }

        final Note notes = new Note();
        notes.setId(source.getId());
        notes.setNote(source.getNote());
        return notes;
    }
}