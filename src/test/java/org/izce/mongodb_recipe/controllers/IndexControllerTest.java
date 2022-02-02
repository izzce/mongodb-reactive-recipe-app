package org.izce.mongodb_recipe.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.izce.mongodb_recipe.controllers.IndexController;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

public class IndexControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	IndexController indexController;
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		indexController = new IndexController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
	}

	@Test
	public void testGetIndexPage() {
		
		Set<Recipe> recipes = new HashSet<Recipe>();
		Recipe recipe1 = new Recipe();
		recipe1.setId("1");
		recipes.add(recipe1);
		Recipe recipe2 = new Recipe();
		recipe2.setId("2");
		recipes.add(recipe2);
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Iterable<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
		
		when(recipeService.getRecipes()).thenReturn(recipes);
		
		String viewName = indexController.getIndexPage(model);
		assertEquals("index", viewName);
		
		verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		
		Iterable<Recipe> recipesFromController = argumentCaptor.getValue();
		AtomicInteger recipeCount = new AtomicInteger();
		recipesFromController.forEach(e -> recipeCount.incrementAndGet());
		
		assertEquals(2, recipeCount.get());
	}
	
	@Test
	public void testMockMVC() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}
	
//	@Test
//	public void testMockMVC2() throws Exception {
//		mockMvc.perform(get("")).andExpect(status().isOk()).andExpect(view().name("index"));
//	}

	@Test
	public void testMockMVC_HTTP404() throws Exception {
		mockMvc.perform(get("/index_404")).andExpect(status().isNotFound());
	}
}
