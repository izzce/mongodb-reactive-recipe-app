package org.izce.mongodb_recipe.controllers;

import static org.izce.mongodb_recipe.controllers.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.NoteService;
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

@WebFluxTest(NoteController.class)
public class NoteControllerTest {
	@MockBean
	NoteService noteService;
	RecipeCommand recipe;
	@Autowired
	private WebTestClient webClient;
	@MockBean
	private StorageService storageService;
	@MockBean
	private MongoOperations mongoOperations;

	@BeforeEach
	public void setUp() throws Exception {
		recipe = new RecipeCommand("2");
		
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
	public void testAddNote() throws Exception {
		NoteCommand nc = new NoteCommand("1", "Cook", recipe.getId());
		recipe.getNotes().add(nc);

		when(noteService.saveNoteCommand(any())).thenReturn(Mono.just(nc));
		
		webClient.post().uri("/recipe/2/note/add")
				.attribute("recipe", recipe)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJsonString(nc))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.note").isEqualTo("Cook");
	}

	@Test
	public void testUpdateNote() throws Exception {
		NoteCommand dc = new NoteCommand("1", "Cook", recipe.getId());
		recipe.getNotes().add(dc);

		NoteCommand ncUpdated = new NoteCommand("1", "Cook mildly.", recipe.getId());

		when(noteService.saveNoteCommand(any())).thenReturn(Mono.just(ncUpdated));

		webClient.post().uri("/recipe/2/note/1/update")
		.attribute("recipe", recipe)
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(asJsonString(ncUpdated))
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.id").isEqualTo("1")
		.jsonPath("$.note").isEqualTo("Cook mildly.");

	}

	@Test
	public void testDeleteExistingNote() throws Exception {
		NoteCommand dc = new NoteCommand("2", "Cook", recipe.getId());
		recipe.getNotes().add(dc);

		when(noteService.delete(any())).thenReturn(Mono.empty());
		
		webClient.delete().uri("/recipe/2/note/2/delete")
				.attribute("recipe", recipe)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("2");
	}

	@Test
	public void testDeleteMissingNote() throws Exception {
		when(noteService.delete(any())).thenReturn(Mono.empty());
		
		webClient.delete().uri("/recipe/2/note/3/delete")
				.attribute("recipe", recipe)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.id").isEqualTo("3");
	}

}
