package org.izce.mongodb_recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.exceptions.NotFoundException;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(RecipeController.class)
public class RecipeControllerTest {
	@MockBean
	RecipeService recipeService;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;

	RecipeCommand recipe;
	
	@BeforeEach
	public void setUp() throws Exception {
		recipe = new RecipeCommand("2");
		
		// This is for satisfying "uomList" additions to the model.
		UnitOfMeasureCommand piece = new UnitOfMeasureCommand("1", "Piece");
		when(recipeService.findAllUoms()).thenReturn(Flux.just(piece));
		
		final var webFilter = new WebFilter() {
	        @Override
	        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain webFilterChain) {
	            return exchange.getSession()
	            		.doOnNext(webSession -> webSession.getAttributes().put("recipe", recipe))
	                    .then(webFilterChain.filter(exchange));
	        }
	    };
		
		final var configurer = new WebTestClientConfigurer() {
			@Override
			public void afterConfigurerAdded(Builder builder, WebHttpHandlerBuilder httpHandlerBuilder,
					ClientHttpConnector connector) {
				 httpHandlerBuilder.filters(filters -> filters.add(0, webFilter));
			}
		};
		
		webClient = webClient.mutateWith(configurer);
	}

	@Test
	public void testGetRecipe() throws Exception {
		when(recipeService.findRecipeCommandById(anyString())).thenReturn(Mono.just(recipe));
		
		webClient.get().uri("/recipe/1/show")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		webClient.get().uri("/recipe/new")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {

		when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipe));

		webClient.post().uri(uriBuilder -> uriBuilder.path("/recipe").queryParam("description", "some string").build())
				.attribute("recipe", recipe)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.exchange()
				.expectStatus().isOk();
				
	}

	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("2");

		when(recipeService.findRecipeCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

		webClient.get().uri("/recipe/1/update")
		.exchange()
		.expectStatus().isOk();
	}

	@Test
	public void testGetRecipeNotFound() throws Exception {
		when(recipeService.findRecipeCommandById(anyString())).thenThrow(NotFoundException.class);

		webClient.get().uri("/recipe/1/show")
		.exchange()
		.expectStatus().isNotFound();
	}
}

