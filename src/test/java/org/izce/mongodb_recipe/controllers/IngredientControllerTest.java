package org.izce.mongodb_recipe.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.controllers.IngredientController;
import org.izce.mongodb_recipe.services.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IngredientControllerTest {
	@Mock
	IngredientService ingredientService;
	@Mock
	Model model;
	IngredientController ingredientController;
	MockMvc mockMvc;
	RecipeCommand recipe;
	UnitOfMeasureCommand piece;
	List<UnitOfMeasureCommand> uomList;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		ingredientController = new IngredientController(ingredientService);
		mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
		recipe = new RecipeCommand("2");
		piece = new UnitOfMeasureCommand("1", "Piece");
		uomList = new ArrayList<UnitOfMeasureCommand>(); 
		uomList.add(piece);
	}

	@Test
	public void testAddIngredient() throws Exception {
		IngredientCommand dc = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(dc);

		when(ingredientService.saveIngredientCommand(any())).thenReturn(dc);

		mockMvc.perform(post("/recipe/2/ingredient/add").sessionAttr("recipe", recipe).sessionAttr("uomList", uomList)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(dc))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1"))).andExpect(jsonPath("$.description", is("Salt")));
	}

	@Test
	public void testUpdateIngredient() throws Exception {
		IngredientCommand dc = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(dc);

		IngredientCommand dcUpdated = new IngredientCommand("1", recipe.getId(), "Sugar", new BigDecimal(0.5f), piece);

		when(ingredientService.saveIngredientCommand(any())).thenReturn(dcUpdated);

		mockMvc.perform(post("/recipe/2/ingredient/1/update").sessionAttr("recipe", recipe).sessionAttr("uomList", uomList)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(dcUpdated))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1"))).andExpect(jsonPath("$.description", is("Sugar")));

	}

	@Test
	public void testDeleteExistingIngredient() throws Exception {
		IngredientCommand dc = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(dc);

		mockMvc.perform(delete("/recipe/2/ingredient/1/delete").sessionAttr("recipe", recipe).sessionAttr("uomList", uomList)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")));
	}

	@Test
	public void testDeleteMissingIngredient() throws Exception {
		
		mockMvc.perform(delete("/recipe/2/ingredient/13/delete").sessionAttr("recipe", recipe).sessionAttr("uomList", uomList))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.id", is("13")));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
