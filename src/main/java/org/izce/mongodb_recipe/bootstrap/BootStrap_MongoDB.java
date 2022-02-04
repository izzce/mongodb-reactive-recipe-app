package org.izce.mongodb_recipe.bootstrap;

import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Difficulty;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.repositories.DirectionRepository;
import org.izce.mongodb_recipe.repositories.IngredientRepository;
import org.izce.mongodb_recipe.repositories.NoteRepository;
import org.izce.mongodb_recipe.repositories.reactive.CategoryReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.UomReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BootStrap_MongoDB implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	UomReactiveRepository uomReactiveRepo;
	
	@Autowired
	CategoryReactiveRepository categoryReactiveRepo;
	
	@Autowired
	RecipeReactiveRepository recipeReactiveRepo;
	
	@Autowired
	IngredientRepository ingredientRepo;

	@Autowired
	DirectionRepository directionRepo;
	
	@Autowired
	NoteRepository noteRepo;
	
	
	public BootStrap_MongoDB() {
		log.debug("Running BootStrap_MongoDB");
	}

	@Transactional
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (categoryReactiveRepo.count().block() == 0L) {
			log.debug("Loading Categories");
			loadCategories();
		}

		if (uomReactiveRepo.count().block() == 0L) {
			log.debug("Loading UOMs");
			loadUom();
		}
		
		if (recipeReactiveRepo.count().block() == 0L) {
			log.debug("Loading Recipes");
			loadRecipes();
		}
		
		
		log.error("##### UnitOfMeasure Count: " + uomReactiveRepo.count().block());
		log.error("##### Category Count: " + categoryReactiveRepo.count().block());
		log.error("##### Recipe Count: " + recipeReactiveRepo.count().block());
	}

	private void loadCategories() {
		categoryReactiveRepo.save(new Category("American")).block();
		categoryReactiveRepo.save(new Category("Italian")).block();
		categoryReactiveRepo.save(new Category("Mexican")).block();
		categoryReactiveRepo.save(new Category("Turkish")).block();
		categoryReactiveRepo.save(new Category("Chinese")).block();
	}

	private void loadUom() {
		uomReactiveRepo.save(new UnitOfMeasure("Teaspoon")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Tablespoon")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Cup")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Pinch")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Ounce")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Each")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Pint")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Dash")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Goz karari")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Piece")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Clove")).block();
		uomReactiveRepo.save(new UnitOfMeasure("Pound")).block();
	}
	
	
	private void loadRecipes() {
		// CATEGORIES
		Category mexican = categoryReactiveRepo.findByDescription("Mexican").block();
		Category american = categoryReactiveRepo.findByDescription("American").block();
		Category chinese = categoryReactiveRepo.findByDescription("Chinese").block();

		// UNIT of MEASURES
		UnitOfMeasure piece = uomReactiveRepo.findByUom("Piece").block();
		UnitOfMeasure teaspoon = uomReactiveRepo.findByUom("Teaspoon").block();
		UnitOfMeasure tablespoon = uomReactiveRepo.findByUom("Tablespoon").block();
		UnitOfMeasure dash = uomReactiveRepo.findByUom("Dash").block();
		UnitOfMeasure clove = uomReactiveRepo.findByUom("Clove").block();
		UnitOfMeasure pound = uomReactiveRepo.findByUom("Pound").block();
		UnitOfMeasure cup = uomReactiveRepo.findByUom("Cup").block();
		UnitOfMeasure pint = uomReactiveRepo.findByUom("Pint").block();

		//////////////////////////////////////////
		// RECIPE-1: Perfect Guacamole
		//////////////////////////////////////////

		Recipe r1 = new Recipe();
		r1.setDescription("Perfect Guacamole");
		r1.setCookTime(0);
		r1.setPrepTime(10);
		r1.setSource("Lezzet Sitesi");
		r1.setUrl("https://www.lezzet.com.tr/yemek-tarifleri/diger-tarifler/sos-tarifleri/guacamole-sos");
		r1.setImageUrl("https://i.lezzet.com.tr/images-xxlarge-recipe/guacamole-sos-fa500b30-90fa-41dd-8ef2-c2c400cd144d.jpg");
		r1.getCategories().add(mexican);
		r1.getCategories().add(american);
		r1.setServings(3);
		r1.setDifficulty(Difficulty.MODERATE);
		// DB will auto-generate the ID in persisting the object and will return the object with id.
		r1 = recipeReactiveRepo.save(r1).block();

		addDirection(r1, "Cut avocado, remove flesh.");
		addDirection(r1, "Mash with a fork.");
		addDirection(r1, "Add salt, lime juice, and the rest.");
		addDirection(r1, "Cover with plastic and chill to store.");

		// INGREDIENTS of RECIPE-1
		addIngredient(r1, "ripe avocado", 2.0f, piece);
		addIngredient(r1, "salt", 0.5f, teaspoon);
		addIngredient(r1, "fresh lime juice or lemon juice", 1.0f, tablespoon);
		addIngredient(r1, "minced red onion or thinly sliced green onion", 2.0f, tablespoon);
		addIngredient(r1, "serrano chiles, stems and seeds removed, minced", 1.5f, piece);
		addIngredient(r1, "cilantro (leaves and tender stems), finely chopped", 2.0f, tablespoon);
		addIngredient(r1, "freshly grated black pepper", 1.0f, dash);
		addIngredient(r1, "ripe tomato, seeds and pulp removed, chopped", 0.5f, piece);
		// recipe1 = recipeRepo.save(recipe1);

		// RECIPE-1 NOTES
		addNote(r1, "Cut avocado, remove flesh: Cut the avocados in half.");
		addNote(r1, "Mash with a fork: Using a fork, roughly mash the avocado.");
		addNote(r1, "Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice.");
		addNote(r1, "Cover with plastic and chill to store: Place plastic wrap on the surface.");
		r1 = recipeReactiveRepo.save(r1).block();

		log.debug("Added Recipe1: {}", "Perfect Guacamole!");

		//////////////////////////////////////////
		// RECIPE-2: Spicy Grilled Chicken Tacos
		//////////////////////////////////////////

		Recipe r2 = new Recipe();
		r2.setDescription("Red Velvet Cookies");
		r2.setCookTime(15);
		r2.setPrepTime(20);
		r2.setSource("Cooking Classy Sitesi");
		r2.setUrl("https://www.cookingclassy.com/recipes/red_velvet_cookies/");
		r2.setImageUrl("https://www.cookingclassy.com/wp-content/uploads/2012/08/red-velvet-cookies-33-1024x1536.jpg");
		r2.getCategories().add(mexican);
		r2.getCategories().add(chinese);
		r2.getCategories().add(american);
		r2.setServings(5);
		r2.setDifficulty(Difficulty.HARD);
		// DB will auto-generate the ID.
		r2 = recipeReactiveRepo.save(r2).block();

		addDirection(r2, "Prepare a gas or charcoal grill for medium-high, direct heat.");
		addDirection(r2, "Make the marinade and coat the chicken.");
		addDirection(r2, "Grill the chicken.");
		addDirection(r2, "Warm the tortillas.");
		addDirection(r2, "Assemble the tacos.");
		// recipe2 = recipeRepo.save(recipe2);

		// INGREDIENTS of RECIPE-2
		addIngredient(r2, "ancho chili powder", 2.0f, tablespoon);
		addIngredient(r2, "dried oregano", 1.0f, teaspoon);
		addIngredient(r2, "dried cumin", 1.0f, pound);
		addIngredient(r2, "sugar", 1.0f, teaspoon);
		addIngredient(r2, "salt", 0.5f, pint);
		addIngredient(r2, "garlic, finely chopped", 1.0f, clove);
		addIngredient(r2, "roughly chopped cilantaro", 1.0f, piece);
		addIngredient(r2, "sour cream thinned with 1/4 cup milk", 0.5f, cup);
		addIngredient(r2, "lime, cut into wedges", 1.0f, piece);
		// recipe2 = recipeRepo.save(recipe2);

		// RECIPE-2 NOTES
		addNote(r2, "Prepare a gas or charcoal grill for medium-high, direct heat.");
		addNote(r2, "Make the marinade and coat the chicken: In a large bowl, stir together the chili powder.");
		addNote(r2, "Grill the chicken: Grill the chicken for 3 to 4 minutes per side.");
		addNote(r2, "Warm the tortillas: Place each tortilla on the grill or on a hot.");
		addNote(r2, "Assemble the tacos: Slice the chicken into strips.");

		r2 = recipeReactiveRepo.save(r2).block();

		log.debug("Added Recipe2: {}", "Spicy Grilled Chicken Tacos!");
	}

	private void addIngredient(Recipe recipe, String description, float amount, UnitOfMeasure uom) {
		var i = new Ingredient(description, amount, uom);
		i.setRecipe(recipe);
		i = ingredientRepo.save(i);
		recipe.getIngredients().add(i);
	}

	private void addDirection(Recipe recipe, String direction) {
		var d = new Direction(direction);
		d.setRecipe(recipe);
		d = directionRepo.save(d);
		recipe.getDirections().add(d);
	}

	private void addNote(Recipe recipe, String note) {
		var n = new Note(note);
		n.setRecipe(recipe);
		n = noteRepo.save(n);
		recipe.getNotes().add(n);
	}
}