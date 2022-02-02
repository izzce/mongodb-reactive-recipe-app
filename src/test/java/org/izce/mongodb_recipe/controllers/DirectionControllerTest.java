package org.izce.mongodb_recipe.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.controllers.DirectionController;
import org.izce.mongodb_recipe.services.DirectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;


public class DirectionControllerTest {
	@Mock
	DirectionService directionService;
	@Mock
	Model model;
	DirectionController directionController;
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		directionController = new DirectionController(directionService);
		mockMvc = MockMvcBuilders.standaloneSetup(directionController).build();
	}

	@Test
	public void testAddDirection() throws Exception {
		RecipeCommand rc = new RecipeCommand("2");
		DirectionCommand dc = new DirectionCommand("1", "Cook");
		rc.getDirections().add(dc);
		
		when(directionService.saveDirectionCommand(any())).thenReturn(dc);
		
		mockMvc.perform(post("/recipe/2/direction/add")
						.sessionAttr("recipe", rc)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(dc)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.direction", is("Cook")));
	}
	
	@Test
    public void testUpdateDirection() throws Exception {
        RecipeCommand rc = new RecipeCommand("2");
        DirectionCommand dc = new DirectionCommand("1", "Cook");
		rc.getDirections().add(dc);
				
		DirectionCommand dcUpdated = new DirectionCommand("1", "Cook mildly.");
		
		when(directionService.saveDirectionCommand(any())).thenReturn(dcUpdated);

		mockMvc.perform(post("/recipe/2/direction/1/update")
				.sessionAttr("recipe", rc)
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(dcUpdated)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is("1")))
		.andExpect(jsonPath("$.direction", is("Cook mildly.")));

    }
	
	@Test
    public void testDeleteExistingDirection() throws Exception {
		 RecipeCommand rc = new RecipeCommand("2");
	        
		 DirectionCommand dc = new DirectionCommand("2", "Cook");
		rc.getDirections().add(dc);
		 
		 mockMvc.perform(delete("/recipe/2/direction/2/delete")
 				.sessionAttr("recipe", rc))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id", is("2")));
    }
	
	@Test
    public void testDeleteMissingDirection() throws Exception {
		 RecipeCommand rc = new RecipeCommand("2");
	        
		 //DirectionCommand cc = new DirectionCommand(3L, "Stir");
		
		 mockMvc.perform(delete("/recipe/2/direction/3/delete")
 				.sessionAttr("recipe", rc))
         .andExpect(status().isNotFound())
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
