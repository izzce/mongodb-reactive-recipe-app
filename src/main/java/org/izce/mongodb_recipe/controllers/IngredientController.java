package org.izce.mongodb_recipe.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.services.IngredientService;
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
@SessionAttributes({ "recipe", "uomList" })
public class IngredientController {
	private final IngredientService ingredientService;

	@Autowired
	public IngredientController(IngredientService ingredientService) {
		log.debug("Initializing IngredientController ...");
		this.ingredientService = ingredientService;
	}

	@PostMapping(value = "/recipe/{recipeId}/ingredient/add", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> addIngredient(
			@PathVariable String recipeId, 
			@RequestBody IngredientCommand ingredient,
			@ModelAttribute("recipe") RecipeCommand recipe,
			@ModelAttribute("uomList") List<UnitOfMeasureCommand> uomList, 
			Model model) throws Exception {

		var uomId = ingredient.getUom().getId();
		ingredient.setUom(uomList.stream().filter(e -> e.getId().equals(uomId)).findAny().orElseThrow());

		IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(ingredient);
		recipe.getIngredients().add(savedIngredient);
		model.addAttribute("recipe", recipe);

		Map<String, Object> map = new HashMap<>();
		map.put("id", savedIngredient.getId());
		map.put("amount", savedIngredient.getAmount());
		map.put("uomid", savedIngredient.getUom().getId());
		map.put("description", savedIngredient.getDescription());
		map.put("alltext", savedIngredient.toString());
		map.put("status", "OK");

		return map;
	}
	
	@PostMapping(value = "/recipe/{recipeId}/ingredient/{ingredientId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> updateIngredient(
			@PathVariable String recipeId, 
			@RequestBody IngredientCommand ingredient,
			@ModelAttribute("recipe") RecipeCommand recipe,
			@ModelAttribute("uomList") List<UnitOfMeasureCommand> uomList, 
			Model model) throws Exception {

		var uomId = ingredient.getUom().getId();
		ingredient.setUom(uomList.stream().filter(e -> e.getId().equals(uomId)).findAny().orElseThrow());

		IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(ingredient);
		recipe.getIngredients().add(savedIngredient);
		model.addAttribute("recipe", recipe);

		Map<String, Object> map = new HashMap<>();
		map.put("id", savedIngredient.getId());
		map.put("amount", savedIngredient.getAmount());
		map.put("uomid", savedIngredient.getUom().getId());
		map.put("description", savedIngredient.getDescription());
		map.put("alltext", savedIngredient.toString());
		map.put("status", "OK");

		return map;
	}

	@DeleteMapping(value = "/recipe/{recipeId}/ingredient/{ingredientId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> deleteIngredient(
			@ModelAttribute("recipe") RecipeCommand recipe,
			@PathVariable String recipeId, 
			@PathVariable String ingredientId, 
			Model model, 
			HttpServletRequest req, 
			HttpServletResponse resp) throws Exception {

		if (true == recipe.getIngredients().removeIf(e -> e.getId().equals(ingredientId))) {
			ingredientService.delete(ingredientId);
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);			
		}
		
		return Map.of("id", ingredientId, "status", "OK");
	}

}
