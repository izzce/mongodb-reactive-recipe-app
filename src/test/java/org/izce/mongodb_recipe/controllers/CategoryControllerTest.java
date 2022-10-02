package org.izce.mongodb_recipe.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.izce.mongodb_recipe.controllers.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
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
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.core.publisher.Mono;

@WebFluxTest(CategoryController.class)
public class CategoryControllerTest {
	@MockBean
	RecipeService recipeService;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;
	
	RecipeCommand rc;
	
	@BeforeEach
	public void setUp() throws Exception {
		rc = new RecipeCommand("2");

		final var webFilter = new WebFilter() {
	        @Override
	        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain webFilterChain) {
	            return exchange.getSession()
	            		.doOnNext(webSession -> webSession.getAttributes().put("recipe", rc))
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
	public void testAddExistingCategory() throws Exception {
		CategoryCommand cc = new CategoryCommand("Turkish");
		cc.setId("1");
		rc.getCategories().add(cc);
		
		when(recipeService.findCategoryByDescription(any())).thenReturn(Mono.just(cc));
		
		webClient.post().uri("/recipe/2/category/add")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJsonString(cc))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.status", is("PRESENT"));
	}
	
	@Test
	public void testAddNewCategory() throws Exception {
		
		CategoryCommand cc = new CategoryCommand("2", "Italian");
		// TODO add or not add to recipe?
		//rc.getCategories().add(cc);
		
		when(recipeService.findCategoryByDescription(anyString())).thenReturn(Mono.just(cc));
		
		webClient.post().uri("/recipe/2/category/add")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(asJsonString(cc))
						.exchange()
						.expectStatus().isOk()
						.expectBody()
						.jsonPath("$.id").isEqualTo("2")
						.jsonPath("$.description").isEqualTo("Italian");
	}
	
	@Test
    public void testDeleteExistingCategory() throws Exception {
		CategoryCommand cc = new CategoryCommand("1", "Turkish");
		rc.getCategories().add(cc);

		webClient.delete().uri("/recipe/2/category/1/delete")
        				.exchange()
        				.expectStatus().isOk()
        				.expectBody()
        				.jsonPath("$.id").isEqualTo("1");
    }
	
	@Test
	public void testDeleteMissingCategory() throws Exception {
		//CategoryCommand cc = new CategoryCommand("2", "Italian");

		webClient.delete().uri("/recipe/2/category/2/delete")
						.exchange()
						.expectStatus().isNotFound()
						.expectBody()
						.jsonPath("$.id").isEqualTo("2");
	}
	
}

