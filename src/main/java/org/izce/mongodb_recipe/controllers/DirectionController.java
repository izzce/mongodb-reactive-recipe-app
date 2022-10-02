package org.izce.mongodb_recipe.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.DirectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class DirectionController {
	private final DirectionService directionService;

	public DirectionController(DirectionService directionService) {
		log.debug("Initializing DirectionController ...");
		this.directionService = directionService;
	}

	@PostMapping(value = "/recipe/{recipeId}/direction/add", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<Map<String, String>> addDirection(
			@PathVariable String recipeId, 
			@RequestBody DirectionCommand direction,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		direction.setRecipeId(recipeId);
		
		return directionService.saveDirectionCommand(direction).flatMap(savedDirection -> {
			recipe.getDirections().add(savedDirection);
			model.addAttribute("recipe", recipe);
			return Mono.just(Map.of("id", savedDirection.getId(), "direction", savedDirection.getDirection()));
		});
	}
	
	@PostMapping(value = "/recipe/{recipeId}/direction/{directionId}/update", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<Map<String, String>> updateDirection(
			@PathVariable String recipeId, 
			@RequestBody DirectionCommand direction,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		return directionService.saveDirectionCommand(direction).flatMap(savedDirection -> {
			recipe.getDirections().remove(direction);
			recipe.getDirections().add(savedDirection);
			model.addAttribute("recipe", recipe);
			return Mono.just(Map.of("id", savedDirection.getId(), "direction", savedDirection.getDirection()));
		});
		
	}

	@DeleteMapping(value = "/recipe/{recipeId}/direction/{directionId}/delete", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<Map<String, String>> deleteDirection(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String directionId, 
			Model model,  
			ServerHttpResponse resp) throws Exception {

		boolean elementRemoved = recipe.getDirections().removeIf(e -> e.getId().equals(directionId));
		if (!elementRemoved) {
			resp.setStatusCode(HttpStatus.NOT_FOUND);
			return Mono.just(Map.of("id", directionId));
		} else {
			return directionService.delete(directionId).then(Mono.just(Map.of("id", directionId)));			
		}
	}

}

