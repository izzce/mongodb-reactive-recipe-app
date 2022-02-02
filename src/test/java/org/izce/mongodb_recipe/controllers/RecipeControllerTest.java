package org.izce.mongodb_recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.exceptions.NotFoundException;
import org.izce.mongodb_recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

public class RecipeControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	RecipeController recipeController;
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		recipeController = new RecipeController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
				.setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	public void testGetRecipe() throws Exception {
		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isOk()).andExpect(view().name("recipe/show"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		mockMvc.perform(get("/recipe/new")).andExpect(status().isOk()).andExpect(view().name("recipe/form"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("2");

		when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

		mockMvc.perform(post("/recipe").sessionAttr("recipe", recipeCommand)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).param("description", "some string"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/show"));
	}

	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		when(recipeService.findRecipeCommandById(anyString())).thenReturn(command);

		mockMvc.perform(get("/recipe/1/update")).andExpect(status().isOk()).andExpect(view().name("recipe/form"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testGetRecipeNotFound() throws Exception {
		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);
		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isNotFound()).andExpect(view().name("error/404"));
	}
}
