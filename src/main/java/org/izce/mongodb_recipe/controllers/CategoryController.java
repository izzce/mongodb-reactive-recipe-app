package org.izce.mongodb_recipe.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@SessionAttributes({ "recipe" })
public class CategoryController {
	private final RecipeService recipeService;

	public CategoryController(RecipeService recipeService) {
		log.debug("CategoryController ...");
		this.recipeService = recipeService;
	}
	
	@PostMapping(value = "/recipe/{recipeId}/category/add", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<Map<String, String>> addCategory(
			@PathVariable String recipeId, 
			@RequestBody CategoryCommand category,
			@ModelAttribute("recipe") RecipeCommand recipe) throws Exception {

		if (recipe.getCategories().stream().anyMatch(e -> e.getDescription().equalsIgnoreCase(category.getDescription()))) {
			return Mono.just(Map.of("status", "PRESENT"));
		} else {
			return recipeService.findCategoryByDescription(category.getDescription()).flatMap(cc -> {
				recipe.getCategories().add(cc);
				return Mono.just(Map.of("id", cc.getId().toString(), "description", cc.getDescription(), "status", "OK"));
			});
		}
	}

	@DeleteMapping(value = "/recipe/{recipeId}/category/{categoryId}/delete", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<Map<String, String>> deleteCategory(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String categoryId, 
			ServerHttpResponse resp) throws Exception {

		boolean elementRemoved = recipe.getCategories().removeIf(e -> e.getId().equals(categoryId));
		if (!elementRemoved) {
			resp.setStatusCode(HttpStatus.NOT_FOUND);
		}
		
		return Mono.just(Map.of("id", categoryId));
	}

}

