package org.izce.mongodb_recipe.controllers;

import java.util.Map;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({ "recipe" })
public class NoteController {
	private final NoteService noteService;

	public NoteController(NoteService noteService) {
		log.debug("Initializing NoteController ...");
		this.noteService = noteService;
	}

	@PostMapping(value = "/recipe/{recipeId}/note/add", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> addNote(
			@PathVariable String recipeId, 
			@RequestBody NoteCommand note,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		note.setRecipeId(recipeId);
		NoteCommand savedNote = noteService.saveNoteCommand(note).block();
		recipe.getNotes().add(savedNote);
		model.addAttribute("recipe", recipe);

		return Map.of("id", savedNote.getId().toString(), "note", savedNote.getNote());
	}
	
	@PostMapping(value = "/recipe/{recipeId}/note/{noteId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> updateNote(
			@PathVariable String recipeId, 
			@RequestBody NoteCommand note,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		NoteCommand savedNote = noteService.saveNoteCommand(note).block();
		recipe.getNotes().remove(note);
		recipe.getNotes().add(savedNote);
		model.addAttribute("recipe", recipe);

		return Map.of("id", savedNote.getId().toString(), "note", savedNote.getNote());
		
	}

	@DeleteMapping(value = "/recipe/{recipeId}/note/{noteId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> deleteNote(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String noteId, 
			ServerHttpResponse resp) throws Exception {

		boolean elementRemoved = recipe.getNotes().removeIf(e -> e.getId().equals(noteId));
		if (!elementRemoved) {
			resp.setStatusCode(HttpStatus.NOT_FOUND);
		}
		noteService.delete(noteId).block();
		return Map.of("id", noteId);
	}

}
