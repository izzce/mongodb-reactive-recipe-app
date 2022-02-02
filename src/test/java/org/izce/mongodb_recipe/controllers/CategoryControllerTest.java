package org.izce.mongodb_recipe.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.controllers.CategoryController;
import org.izce.mongodb_recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CategoryControllerTest {
	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	CategoryController categoryController;
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		categoryController = new CategoryController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	public void testAddExistingCategory() throws Exception {
		RecipeCommand rc = new RecipeCommand();
		rc.setId("2");
		CategoryCommand cc = new CategoryCommand("Turkish");
		cc.setId("1");
		rc.getCategories().add(cc);
		
		when(recipeService.findCategoryByDescription(any())).thenReturn(cc);
		
		mockMvc.perform(post("/recipe/2/category/add")
						.sessionAttr("recipe", rc)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(cc)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("PRESENT")));
	}
	
	@Test
	public void testAddNewCategory() throws Exception {
		RecipeCommand rc = new RecipeCommand();
		rc.setId("2");
		CategoryCommand cc = new CategoryCommand("Italian");
		cc.setId("2");
		//rc.getCategories().add(cc);
		
		when(recipeService.findCategoryByDescription(any())).thenReturn(cc);
		
		mockMvc.perform(post("/recipe/2/category/add")
						.sessionAttr("recipe", rc)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(cc)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("2")))
				.andExpect(jsonPath("$.description", is("Italian")));
	}
	
	@Test
    public void testDeleteExistingCategory() throws Exception {
        RecipeCommand rc = new RecipeCommand();
        rc.setId("2");
		CategoryCommand cc = new CategoryCommand("Turkish");
		cc.setId("1");
		rc.getCategories().add(cc);

        mockMvc.perform(delete("/recipe/2/category/1/delete")
        				.sessionAttr("recipe", rc))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is("1")));
    }
	
	@Test
    public void testDeleteMissingCategory() throws Exception {
		 RecipeCommand rc = new RecipeCommand();
		 rc.setId("2");
	        
        CategoryCommand cc = new CategoryCommand("Italian");
		cc.setId("2");
		
		 mockMvc.perform(delete("/recipe/2/category/2/delete")
 				.sessionAttr("recipe", rc))
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.id", is("2")));
    }
	
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
