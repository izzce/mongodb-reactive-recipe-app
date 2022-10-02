package org.izce.mongodb_recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.ImageService;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageProperties;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;


@WebFluxTest(ImageController.class)
public class ImageControllerTest {
	private static final String CONSTANT_STRING_VALUE = "Izce's Reactive Spring Boot App";
	private static final String TEST_FILE_NAME = "testing.txt";

	@MockBean
	RecipeService  recipeService;
	@MockBean
	ImageService  imageService;
	@Autowired
	ImageController imageController;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;
	
	RecipeCommand recipe;
	
	@BeforeEach
	public void setUp() throws Exception {
		recipe = new RecipeCommand("1");
		when(recipeService.findRecipeCommandById(anyString())).thenReturn(Mono.just(recipe));
		when(imageService.save(anyString(), any())).thenReturn(Mono.empty());
		when(storageService.load(anyString()))
				.thenReturn(Mono.just(Paths.get(new StorageProperties().getLocation(), TEST_FILE_NAME)));
	}
	
	@Test
	public void handleImagePostForNewRecipe() throws Exception {
		var bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("image", CONSTANT_STRING_VALUE.getBytes())
			.header("Content-Disposition", "form-data; name=image; filename=image_0.jpg")
			.contentType(MediaType.MULTIPART_FORM_DATA);
		
		webClient.post().uri("/recipe/image")
				//.contentType(MediaType.MULTIPART_FORM_DATA)
				.bodyValue(bodyBuilder.build())
				.exchange()
				.expectStatus().isOk();

        verify(storageService, times(1)).store(any());
	}

	@Test
	public void handleImagePostForExistingRecipe() throws Exception {
		var bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("image", CONSTANT_STRING_VALUE.getBytes())
			.header("Content-Disposition", "form-data; name=image; filename=image_0.jpg")
			.contentType(MediaType.MULTIPART_FORM_DATA);
        
		webClient.post().uri("/recipe/1/image")
			//.contentType(MediaType.MULTIPART_FORM_DATA)
			.bodyValue(bodyBuilder.build())
			.exchange()
			.expectStatus().isOk();

        verify(imageService, times(1)).save(anyString(), any());
	}
	
}

