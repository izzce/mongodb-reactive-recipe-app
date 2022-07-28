package org.izce.mongodb_recipe.controllers;

import org.izce.mongodb_recipe.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	private final RecipeService recipeService;

	public IndexController(RecipeService recipeService) {
		log.debug("Initializing IndexController...");
		this.recipeService = recipeService;
	}

	@RequestMapping({"/", "/index" })
	public String getIndexPage(Model model) {
		log.debug("Index page is requested!");
		model.addAttribute("recipes", recipeService.getRecipes().collectList().block());
		log.debug("Number of recipes found: {}", recipeService.getRecipesCount().block());
		return "index";
	}
	
	@RequestMapping("/surprise")
	public String surprise() {
		log.info("surprise page is requested!");
		return "surprise";
	}
}
