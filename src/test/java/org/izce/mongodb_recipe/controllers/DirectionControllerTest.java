package org.izce.mongodb_recipe.controllers;

import static org.izce.mongodb_recipe.controllers.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.DirectionService;
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
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.core.publisher.Mono;

@WebFluxTest(DirectionController.class)
public class DirectionControllerTest {
	@MockBean
	DirectionService directionService;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;

	DirectionCommand direction;
	
	@BeforeEach
	public void setUp() throws Exception {
		var recipe = new RecipeCommand("2");
		direction = new DirectionCommand("1", "Cook");
		recipe.getDirections().add(direction);
		
		when(directionService.saveDirectionCommand(any())).thenReturn(Mono.just(direction));

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
	public void testAddDirection() throws Exception {
		
		webClient.post().uri("/recipe/2/direction/add")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJsonString(direction))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.direction").isEqualTo("Cook");
		
	}
	
	@Test
    public void testUpdateDirection() throws Exception {
		DirectionCommand dcUpdated = new DirectionCommand("1", "Slice");
		
		when(directionService.saveDirectionCommand(any())).thenReturn(Mono.just(dcUpdated));

		webClient.post().uri("/recipe/2/direction/1/update")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJsonString(dcUpdated))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.direction").isEqualTo("Slice");
    }
	
	@Test
	public void testDeleteExistingDirection() throws Exception {
		
		when(directionService.delete(any())).thenReturn(Mono.empty());

		webClient.delete().uri("/recipe/2/direction/1/delete")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1");
	}
	
	@Test
	public void testDeleteMissingDirection() throws Exception {
		// DirectionCommand cc = new DirectionCommand(3L, "Stir");

		webClient.delete().uri("/recipe/2/direction/3/delete")
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.id").isEqualTo("3");
	}

}

