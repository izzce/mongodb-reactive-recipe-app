package org.izce.mongodb_recipe.services;

import java.util.Optional;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.converters.NoteCommandToNote;
import org.izce.mongodb_recipe.converters.NoteToNoteCommand;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepo;
    private final NoteCommandToNote nc2n;
    private final NoteToNoteCommand n2nc;

	@Autowired
	public NoteServiceImpl(
			NoteRepository ir,
			NoteCommandToNote nc2n,
			NoteToNoteCommand n2nc) {
		
		log.debug("Initializing NoteServiceImpl...");
		this.noteRepo = ir;
		this.nc2n = nc2n;
		this.n2nc = n2nc;
	}
	
	@Override
	public Note findById(String id) {
		Optional<Note> noteOptional = noteRepo.findById(id);
		return noteOptional.orElseThrow(() -> new RuntimeException("Note not found: " + id));
	}

	@Override
	public NoteCommand findNoteCommandById(String id) {
		return n2nc.convert(findById(id));
	}
	
	@Override
	@Transactional
	public NoteCommand saveNoteCommand(NoteCommand noteCommand) {
		Note note = nc2n.convert(noteCommand);
		note = noteRepo.save(note);
		
		log.info("Saved Note: {}", note);
		
		return n2nc.convert(note);
	}
	
	@Override
	@Transactional
	public void delete(String noteId) {
		noteRepo.deleteById(noteId);
		 
		log.info("Deleted Note: {}", noteId);
	}
}

