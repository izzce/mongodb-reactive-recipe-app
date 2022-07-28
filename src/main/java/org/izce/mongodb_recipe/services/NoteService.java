package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.model.Note;

import reactor.core.publisher.Mono;

public interface NoteService {
	Mono<Note> findById(String id);
	Mono<NoteCommand> findNoteCommandById(String id);
	Mono<NoteCommand> saveNoteCommand(NoteCommand command);
	Mono<Void> delete(String noteId);
}
