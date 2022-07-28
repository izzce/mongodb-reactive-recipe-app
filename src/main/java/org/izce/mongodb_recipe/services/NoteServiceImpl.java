package org.izce.mongodb_recipe.services;

import java.util.NoSuchElementException;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.converters.NoteCommandToNote;
import org.izce.mongodb_recipe.converters.NoteToNoteCommand;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.repositories.reactive.NoteReactiveRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {
    private final NoteReactiveRepository noteRepo;
    private final NoteCommandToNote nc2n;
    private final NoteToNoteCommand n2nc;

	public NoteServiceImpl(
			NoteReactiveRepository ir,
			NoteCommandToNote nc2n,
			NoteToNoteCommand n2nc) {
		
		log.debug("Initializing NoteServiceImpl...");
		this.noteRepo = ir;
		this.nc2n = nc2n;
		this.n2nc = n2nc;
	}
	
	@Override
	public Mono<Note> findById(String id) {
		return noteRepo.findById(id).switchIfEmpty(Mono.defer(() -> {
			throw new NoSuchElementException("Note not found: " + id);
		}));

	}

	@Override
	public Mono<NoteCommand> findNoteCommandById(String id) {
		return this.findById(id).map(n2nc::convert);
	}
	
	@Override
	public Mono<NoteCommand> saveNoteCommand(NoteCommand noteCommand) {
		Note note = nc2n.convert(noteCommand);
		return noteRepo.save(note).map(n2nc::convert);
	}
	
	@Override
	public Mono<Void> delete(String noteId) {
		return noteRepo.deleteById(noteId);
	}
}

