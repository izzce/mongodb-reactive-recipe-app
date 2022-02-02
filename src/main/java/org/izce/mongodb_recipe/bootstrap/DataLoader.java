package org.izce.mongodb_recipe.bootstrap;

import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Difficulty;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.repositories.CategoryRepository;
import org.izce.mongodb_recipe.repositories.DirectionRepository;
import org.izce.mongodb_recipe.repositories.IngredientRepository;
import org.izce.mongodb_recipe.repositories.NoteRepository;
import org.izce.mongodb_recipe.repositories.RecipeRepository;
import org.izce.mongodb_recipe.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("default")
public class DataLoader implements CommandLineRunner {

	private final RecipeRepository recipeRepo;
	private final IngredientRepository ingredientRepo;
	private final UnitOfMeasureRepository uomRepo;
	private final NoteRepository notesRepo;
	private final CategoryRepository categoryRepo;
	private final DirectionRepository directionRepo;

	@Autowired
	public DataLoader(RecipeRepository recipeRepo, IngredientRepository ingredientRepo, UnitOfMeasureRepository uomRepo,
			NoteRepository notesRepo, CategoryRepository categoryRepo, DirectionRepository directionRepo) {
		log.debug("Initializing DataLoader...");
		this.recipeRepo = recipeRepo;
		this.ingredientRepo = ingredientRepo;
		this.uomRepo = uomRepo;
		this.notesRepo = notesRepo;
		this.categoryRepo = categoryRepo;
		this.directionRepo = directionRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		// CATEGORIES
		Category mexican = categoryRepo.findByDescription("Mexican").get();
		Category american = categoryRepo.findByDescription("American").get();
		Category chinese = categoryRepo.findByDescription("Chinese").get();

		// UNIT of MEASURES
		UnitOfMeasure piece = uomRepo.findByUom("Piece").get();
		UnitOfMeasure teaspoon = uomRepo.findByUom("Teaspoon").get();
		UnitOfMeasure tablespoon = uomRepo.findByUom("Tablespoon").get();
		UnitOfMeasure dash = uomRepo.findByUom("Dash").get();
		UnitOfMeasure clove = uomRepo.findByUom("Clove").get();
		UnitOfMeasure pound = uomRepo.findByUom("Pound").get();
		UnitOfMeasure cup = uomRepo.findByUom("Cup").get();
		UnitOfMeasure pint = uomRepo.findByUom("Pint").get();

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
		// DB will auto-generate the ID in persisting the object and will return the
		// object with id.
		r1 = recipeRepo.save(r1);

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
		r1 = recipeRepo.save(r1);

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
		r2 = recipeRepo.save(r2);

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

		r2 = recipeRepo.save(r2);

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
		n = notesRepo.save(n);
		recipe.getNotes().add(n);
	}

}
