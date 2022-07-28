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
import java.util.List;
import java.util.Set;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class IndexControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	IndexController indexController;
	MockMvc mockMvc;
	ArgumentCaptor<List<Recipe>> argumentCaptor;
	
	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		indexController = new IndexController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
		Set<Recipe> recipes = new HashSet<Recipe>();
		Recipe recipe1 = new Recipe();
		recipe1.setId("1");
		recipes.add(recipe1);
		Recipe recipe2 = new Recipe();
		recipe2.setId("2");
		recipes.add(recipe2);
		
		argumentCaptor = ArgumentCaptor.forClass(List.class);
		
		when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));
		when(recipeService.getRecipesCount()).thenReturn(Mono.just(2L));
	}

	@Test
	public void testGetIndexPage() {
		
		String viewName = indexController.getIndexPage(model);
		assertEquals("index", viewName);
		
		verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		
		List<Recipe> recipeList = argumentCaptor.getValue();
		
		assertEquals(2, recipeList.size());
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
