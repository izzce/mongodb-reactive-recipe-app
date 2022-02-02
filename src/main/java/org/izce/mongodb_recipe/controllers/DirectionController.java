package org.izce.mongodb_recipe.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

@Slf4j
@Controller
@SessionAttributes({ "recipe" })
public class DirectionController {
	private final DirectionService directionService;

	@Autowired
	public DirectionController(DirectionService directionService) {
		log.debug("Initializing DirectionController ...");
		this.directionService = directionService;
	}

	@PostMapping(value = "/recipe/{recipeId}/direction/add", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> addDirection(
			@PathVariable String recipeId, 
			@RequestBody DirectionCommand direction,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		direction.setRecipeId(recipeId);
		DirectionCommand savedDirection = directionService.saveDirectionCommand(direction);
		recipe.getDirections().add(savedDirection);
		model.addAttribute("recipe", recipe);

		return Map.of("id", savedDirection.getId(), "direction", savedDirection.getDirection());
	}
	
	@PostMapping(value = "/recipe/{recipeId}/direction/{directionId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> updateDirection(
			@PathVariable String recipeId, 
			@RequestBody DirectionCommand direction,
			@ModelAttribute("recipe") RecipeCommand recipe,
			Model model) throws Exception {
		
		DirectionCommand savedDirection = directionService.saveDirectionCommand(direction);
		recipe.getDirections().remove(direction);
		recipe.getDirections().add(savedDirection);
		model.addAttribute("recipe", recipe);

		return Map.of("id", savedDirection.getId().toString(), "direction", savedDirection.getDirection());
		
	}

	@DeleteMapping(value = "/recipe/{recipeId}/direction/{directionId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> deleteDirection(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String directionId, 
			Model model, 
			HttpServletRequest req, 
			HttpServletResponse resp) throws Exception {

		boolean elementRemoved = recipe.getDirections().removeIf(e -> e.getId().equals(directionId));
		if (!elementRemoved) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			directionService.delete(directionId);			
		}
		return Map.of("id", directionId);
	}

}
