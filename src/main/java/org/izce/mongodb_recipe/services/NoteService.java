package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.model.Note;

public interface NoteService {
	Note findById(String id);
	NoteCommand findNoteCommandById(String id);
	NoteCommand saveNoteCommand(NoteCommand command);
	void delete(String noteId);
}
