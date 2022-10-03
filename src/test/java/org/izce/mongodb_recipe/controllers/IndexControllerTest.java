package org.izce.mongodb_recipe.controllers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(IndexController.class)
public class IndexControllerTest {
	@MockBean
	RecipeService recipeService;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;
	
	@BeforeEach
	public void setUp() throws Exception {
		List<RecipeCommand> recipeList = new ArrayList<RecipeCommand>();
		recipeList.add(new RecipeCommand("1"));
		recipeList.add(new RecipeCommand("2"));
		
		when(recipeService.getRecipeCommands()).thenReturn(Flux.fromIterable(recipeList));
		when(recipeService.getRecipesCount()).thenReturn(Mono.just(2L));
	}
	
	@Test
	public void testRootPath() throws Exception {
		webClient.get().uri("/")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void test_HTTP_BAD_REQUEST() throws Exception {
		webClient.get().uri("/index_400").exchange().expectStatus().isBadRequest();
	}
}


