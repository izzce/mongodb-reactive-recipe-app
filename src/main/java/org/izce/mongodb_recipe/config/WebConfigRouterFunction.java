package org.izce.mongodb_recipe.config;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.services.RecipeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebConfigRouterFunction {
	
	@Bean
	public RouterFunction<?> routes(RecipeService recipeService) {
		return RouterFunctions.route(GET("/api/recipes"), 
				serverRequest -> ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(recipeService.getRecipes(), Recipe.class));
	}
}
