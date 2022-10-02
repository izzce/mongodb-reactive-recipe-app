package org.izce.mongodb_recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.izce.mongodb_recipe.controllers.TestUtils.asJsonString;

import java.math.BigDecimal;
import java.util.List;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.services.IngredientService;
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

import reactor.core.publisher.Mono;

@WebFluxTest(IngredientController.class)
public class IngredientControllerTest {
	@MockBean
	IngredientService ingredientService;
	@Autowired
	WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;
	
	RecipeCommand recipe;
	UnitOfMeasureCommand piece;
	List<UnitOfMeasureCommand> uomList;

	@BeforeEach
	public void setUp() throws Exception {
		recipe = new RecipeCommand("2");
		piece = new UnitOfMeasureCommand("1", "Piece");
		uomList = List.of(piece);
		
		final var webFilter = new WebFilter() {
	        @Override
	        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain webFilterChain) {
	            return exchange.getSession()
	            		.doOnNext(webSession -> {
	            			webSession.getAttributes().put("recipe", recipe);
	            			webSession.getAttributes().put("uomList", uomList);
	            		})
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
	public void testAddIngredient() throws Exception {
		IngredientCommand ic = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(ic);

		when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(ic));

		webClient.post().uri("/recipe/2/ingredient/add")
				.attribute("recipe", recipe).attribute("uomList", uomList)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJsonString(ic))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.description").isEqualTo("Salt");
	}

	@Test
	public void testUpdateIngredient() throws Exception {
		IngredientCommand dc = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(dc);

		IngredientCommand icUpdated = new IngredientCommand("1", recipe.getId(), "Sugar", new BigDecimal(0.5f), piece);

		when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(icUpdated));
		
		webClient.post().uri("/recipe/2/ingredient/1/update")
		.attribute("recipe", recipe).attribute("uomList", uomList)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(asJsonString(icUpdated))
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.id").isEqualTo("1")
		.jsonPath("$.description").isEqualTo("Sugar");

	}

	@Test
	public void testDeleteExistingIngredient() throws Exception {
		IngredientCommand ic = new IngredientCommand("1", recipe.getId(), "Salt", new BigDecimal(0.5f), piece);
		recipe.getIngredients().add(ic);
		
		when(ingredientService.delete(any())).thenReturn(Mono.empty());
		
		webClient.delete().uri("/recipe/2/ingredient/1/delete")
				.attribute("recipe", recipe).attribute("uomList", uomList)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1");
	}

	@Test
	public void testDeleteMissingIngredient() throws Exception {
		webClient.delete().uri("/recipe/2/ingredient/13/delete")
				.attribute("recipe", recipe).attribute("uomList", uomList)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.id").isEqualTo("13");
	}
	
}

