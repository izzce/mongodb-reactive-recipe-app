package org.izce.mongodb_recipe.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.controllers.NoteController;
import org.izce.mongodb_recipe.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NoteControllerTest {
	@Mock
	NoteService noteService;
	@Mock
	Model model;
	NoteController noteController;
	MockMvc mockMvc;
	RecipeCommand recipe;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		noteController = new NoteController(noteService);
		mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
		recipe = new RecipeCommand("2");
	}

	@Test
	public void testAddNote() throws Exception {
		NoteCommand dc = new NoteCommand("1", "Cook", recipe.getId());
		recipe.getNotes().add(dc);

		when(noteService.saveNoteCommand(any())).thenReturn(dc);

		mockMvc.perform(post("/recipe/2/note/add").sessionAttr("recipe", recipe).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(dc))).andExpect(status().isOk()).andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.note", is("Cook")));
	}

	@Test
	public void testUpdateNote() throws Exception {
		NoteCommand dc = new NoteCommand("1", "Cook", recipe.getId());
		recipe.getNotes().add(dc);

		NoteCommand dcUpdated = new NoteCommand("1", "Cook mildly.", recipe.getId());

		when(noteService.saveNoteCommand(any())).thenReturn(dcUpdated);

		mockMvc.perform(post("/recipe/2/note/1/update").sessionAttr("recipe", recipe)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(dcUpdated))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1"))).andExpect(jsonPath("$.note", is("Cook mildly.")));

	}

	@Test
	public void testDeleteExistingNote() throws Exception {
		NoteCommand dc = new NoteCommand("2", "Cook", recipe.getId());
		recipe.getNotes().add(dc);

		mockMvc.perform(delete("/recipe/2/note/2/delete").sessionAttr("recipe", recipe)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("2")));
	}

	@Test
	public void testDeleteMissingNote() throws Exception {
		mockMvc.perform(delete("/recipe/2/note/3/delete").sessionAttr("recipe", recipe)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.id", is("3")));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
