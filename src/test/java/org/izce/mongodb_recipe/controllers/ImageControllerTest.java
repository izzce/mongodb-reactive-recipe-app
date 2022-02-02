package org.izce.mongodb_recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Paths;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.controllers.ControllerExceptionHandler;
import org.izce.mongodb_recipe.controllers.ImageController;
import org.izce.mongodb_recipe.services.ImageService;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageProperties;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

public class ImageControllerTest {
	static final String TEST_FILE_NAME = "testing.txt";

	
	@Mock
	RecipeService  recipeService;
	@Mock
	ImageService  imageService;
	@Mock
	StorageService  storageService;
	@Mock
	Model model;
	ImageController imageController;
	MockMvc mockMvc;
	RecipeCommand recipe;
	
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		imageController = new ImageController(recipeService, imageService, storageService);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.build();
		recipe = new RecipeCommand("1");
		when(recipeService.findRecipeCommandById(anyString())).thenReturn(recipe);
		when(storageService.load(anyString())).thenReturn(Paths.get(new StorageProperties().getLocation(), TEST_FILE_NAME));
	}
	
	@Test
	public void handleImagePostForNewRecipe() throws Exception {
		MockMultipartFile multipartFile =
                new MockMultipartFile("image[]", TEST_FILE_NAME, MediaType.TEXT_PLAIN_VALUE,
                        "Spring Framework Guru".getBytes());
		
        mockMvc.perform(multipart("/recipe/image").file(multipartFile))
                .andExpect(status().isOk());

        verify(storageService, times(1)).store(any());
	}

	@Test
	public void handleImagePostForExistingRecipe() throws Exception {
		MockMultipartFile multipartFile =
                new MockMultipartFile("image[]", TEST_FILE_NAME, MediaType.TEXT_PLAIN_VALUE,
                        "Spring Framework Guru".getBytes());
		
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().isOk());

        verify(imageService, times(1)).save(anyString(), any());
	}
	
}

