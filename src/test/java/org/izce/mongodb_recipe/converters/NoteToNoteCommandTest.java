package org.izce.mongodb_recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.converters.NoteToNoteCommand;
import org.izce.mongodb_recipe.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoteToNoteCommandTest {

	public static final String ID_VALUE = "1";
	public static final String RECIPE_NOTES = "Notes";
	NoteToNoteCommand converter;

	@BeforeEach
	public void setUp() throws Exception {
		converter = new NoteToNoteCommand();
	}

	@Test
	public void convert() throws Exception {
		// given
		Note notes = new Note();
		notes.setId(ID_VALUE);
		notes.setNote(RECIPE_NOTES);

		// when
		NoteCommand notesCommand = converter.convert(notes);

		// then
		assertEquals(ID_VALUE, notesCommand.getId());
		assertEquals(RECIPE_NOTES, notesCommand.getNote());
	}

	@Test
	public void testNull() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new Note()));
	}
}
