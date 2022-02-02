package org.izce.mongodb_recipe.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.exceptions.NotFoundException;
import org.izce.mongodb_recipe.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({ "recipe", "uomList" })
public class RecipeController {
	private final RecipeService recipeService;

	@Autowired
	public RecipeController(RecipeService recipeService) {
		log.debug("Initializing RecipeController ...");
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/show")
	public String showRecipe(
			@PathVariable String id, 
			Model model) {
		log.debug("recipe/show page is requested!");
		model.addAttribute("recipe", recipeService.findById(id));
		return "recipe/show";
	}

	@GetMapping("/recipe/new")
	public String createRecipe(final Model model) {
		log.debug("recipe/new is requested!");
		model.addAttribute("recipe", new RecipeCommand());
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@GetMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model) {
		log.debug("recipe/{}/update page is requested!", id);
		model.addAttribute("recipe", recipeService.findRecipeCommandById(id));
		model.addAttribute("uomList", recipeService.findAllUoms());

		return "recipe/form";
	}

	@PostMapping("/recipe")
	public String saveOrUpdateRecipe(@Validated @ModelAttribute("recipe") RecipeCommand recipe,
			BindingResult bindingResult, Model model, SessionStatus status, 
			HttpServletRequest req, HttpSession session) {

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> {
				log.warn(error.toString());
			});
			// the model attr 'recipe' will still be avail to view for rendering!
			return "recipe/form";
		}
		
		//printRequestMap(req, session, model);

		RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipe);

		if (recipe.getId() == null) {
			// 1. createRecipe(...)
			// 2. recipe/form --> for main recipe details.
			// 3. saveOrUpdateRecipe(...)
			// 4. recipe/form --> for categories, directions, ingredients & notes.

			// A new recipe was just saved. Return to the form to update
			// for categories, directions, ingredients & notes details.
			model.addAttribute("recipe", savedRecipe);
			return "recipe/form";
		} else {
			// This is to remove 'recipe' from session.
			status.setComplete();
			return "redirect:/recipe/" + savedRecipe.getId() + "/show";
		}
	}
	
	@GetMapping(value = "/recipe/{recipeId}/delete")
	public String deleteRecipe(@PathVariable String recipeId) throws Exception {
		
		recipeService.delete(recipeId);
		
		return "redirect:/index";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception) {
		log.error("Handling not found exception: " + exception.getMessage());
		ModelAndView mav = new ModelAndView("error/404");
		mav.addObject("exception", exception);
		return mav;
	}
	
}

