package org.izce.mongodb_recipe.controllers;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.exceptions.NotFoundException;
import org.izce.mongodb_recipe.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@SessionAttributes({ "recipe", "uomList" })
public class RecipeController {
	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/show")
	public String showRecipe(@PathVariable String id, Model model, WebSession session) {
		log.debug("recipe/show page is requested!");
		model.addAttribute("recipe", recipeService.findRecipeCommandById(id));
		return "recipe/show";
	}

	@GetMapping("/recipe/new")
	public String createRecipe(final Model model) {
		log.debug("recipe/new is requested!");
		model.addAttribute("recipe", Mono.just(new RecipeCommand()));
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@GetMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model, WebSession session) {
		log.debug("recipe/{}/update page is requested!", id);
		model.addAttribute("recipe", recipeService.findRecipeCommandById(id));
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@PostMapping("/recipe")
	public Mono<String> saveOrUpdateRecipe(@Validated RecipeCommand recipe, BindingResult bindingResult,
			 Model model, SessionStatus status, ServerHttpRequest req) {

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> {
				log.warn(error.toString());
			});
			// the model attr 'recipe' will still be avail to view for rendering!
			return Mono.just("recipe/form");
		}
		
		//printRequestMap(req, session, model);

		return recipeService.saveRecipeCommand(recipe).flatMap(savedRecipe -> {
			if (recipe.getId() == null) {
				// 1. createRecipe(...)
				// 2. recipe/form --> for main recipe details.
				// 3. saveOrUpdateRecipe(...)
				// 4. recipe/form --> for categories, directions, ingredients & notes.

				// A new recipe was just saved. Return to the form to update
				// for categories, directions, ingredients & notes details.
				model.addAttribute("recipe", Mono.just(savedRecipe));
				return Mono.just("recipe/form");
			} else {
				// This is to remove 'recipe' from session.
				status.setComplete();
				return Mono.just("redirect:/recipe/" + savedRecipe.getId() + "/show");
			}
		});
	}
	
	@GetMapping(value = "/recipe/{recipeId}/delete")
	public Mono<String> deleteRecipe(@PathVariable String recipeId) throws Exception {
		return recipeService.delete(recipeId).thenReturn("redirect:/index");
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public Mono<Rendering> handleNotFound(Exception exception) {
		log.error("Handling not found exception: ", exception);
		return Mono.just(Rendering.view("error/404").modelAttribute("exception", exception).build());
	}
	
}

