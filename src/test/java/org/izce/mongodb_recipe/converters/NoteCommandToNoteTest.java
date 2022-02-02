package org.izce.mongodb_recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.converters.NoteCommandToNote;
import org.izce.mongodb_recipe.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoteCommandToNoteTest {

    public static final String ID_VALUE = "1";
    public static final String RECIPE_NOTE = "Notes";
    NoteCommandToNote converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new NoteCommandToNote();

    }

    @Test
    public void testNullParameter() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void convert() throws Exception {
        //given
        NoteCommand notesCommand = new NoteCommand();
        notesCommand.setId(ID_VALUE);
        notesCommand.setNote(RECIPE_NOTE);

        //when
        Note note = converter.convert(notesCommand);

        //then
        assertNotNull(note);
        assertEquals(ID_VALUE, note.getId());
        assertEquals(RECIPE_NOTE, note.getNote());
    }

}