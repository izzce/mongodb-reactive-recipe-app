package org.izce.mongodb_recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.CategoryCommandToCategory;
import org.izce.mongodb_recipe.converters.CategoryToCategoryCommand;
import org.izce.mongodb_recipe.converters.DirectionCommandToDirection;
import org.izce.mongodb_recipe.converters.DirectionToDirectionCommand;
import org.izce.mongodb_recipe.converters.IngredientCommandToIngredient;
import org.izce.mongodb_recipe.converters.IngredientToIngredientCommand;
import org.izce.mongodb_recipe.converters.NoteCommandToNote;
import org.izce.mongodb_recipe.converters.NoteToNoteCommand;
import org.izce.mongodb_recipe.converters.RecipeCommandToRecipe;
import org.izce.mongodb_recipe.converters.RecipeToRecipeCommand;
import org.izce.mongodb_recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.izce.mongodb_recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.repositories.reactive.CategoryReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.DirectionReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.IngredientReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.NoteReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.UomReactiveRepository;
import org.izce.mongodb_recipe.services.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecipeServiceImplTest {
	static final String RECIPE_NAME = "Minestrone Soup";
	static final String CATEGORY_NAME = "Italian";
	static final String CATEGORY_NAME2 = "Turkish";
	static final String UOM_SPOON = "Spoon";
	static final String UOM_PINCH = "Pinch";

	RecipeServiceImpl service;
	Recipe recipe;
	Category category, category2;
	UnitOfMeasure uom1, uom2;
	
	@Mock RecipeReactiveRepository repository;
	@Mock CategoryReactiveRepository catRepo;
	@Mock IngredientReactiveRepository ingredientRepo;
	@Mock UomReactiveRepository uomRepo;
	@Mock NoteReactiveRepository noteRepo;
	@Mock DirectionReactiveRepository directionRepo;
    RecipeToRecipeCommand recipeToRecipeCommand;
    RecipeCommandToRecipe recipeCommandToRecipe;
    UnitOfMeasureToUnitOfMeasureCommand uom2uomc;
    UnitOfMeasureCommandToUnitOfMeasure uomc2uom;
    CategoryToCategoryCommand cTocc;
    
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		recipe = new Recipe();
		recipe.setDescription(RECIPE_NAME);
		category = new Category();
		category.setDescription(CATEGORY_NAME);
		category2 = new Category();
		category2.setDescription(CATEGORY_NAME2);
		recipe.getCategories().add(category);
		recipe.getCategories().add(category2);
		
		uom1 = new UnitOfMeasure();
		uom1.setUom(UOM_SPOON);
		uom2 = new UnitOfMeasure();
		uom2.setUom(UOM_PINCH);
		
		cTocc = new CategoryToCategoryCommand();
		uom2uomc = new UnitOfMeasureToUnitOfMeasureCommand();
		uomc2uom = new UnitOfMeasureCommandToUnitOfMeasure();
		
		recipeToRecipeCommand = new RecipeToRecipeCommand(cTocc, new IngredientToIngredientCommand(uom2uomc),
				new DirectionToDirectionCommand(), new NoteToNoteCommand());

		recipeCommandToRecipe = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
				new IngredientCommandToIngredient(uomc2uom), new DirectionCommandToDirection(),
				new NoteCommandToNote());
		service = new RecipeServiceImpl(repository, catRepo, ingredientRepo, uomRepo, noteRepo, directionRepo,
				recipeCommandToRecipe, recipeToRecipeCommand, uom2uomc, cTocc);
	}

	@Test
	void testGetRecipes() {
		when(repository.findAll()).thenReturn(Flux.just(recipe));
		
		AtomicInteger recipeCount = new AtomicInteger();
		service.getRecipes().map(r -> recipeCount.addAndGet(1)).blockLast();
		
		assertEquals(recipeCount.get(), 1);
		
		verify(repository, times(1)).findAll();
	}
	
	@Test
	void testGetRecipesCount() {
		when(repository.count()).thenReturn(Mono.just(1L));
		
		assertEquals(1L, service.getRecipesCount().block());
		
		verify(repository, times(1)).count();
	}
	
	@Test
	void testFindById() {
		when(repository.findById(anyString())).thenReturn(Mono.just(recipe));
		
		assertEquals(recipe.getDescription(), service.findById("1").block().getDescription());
		
		verify(repository, times(1)).findById(anyString());
	}
	
	@Test
	void testFindRecipeCommandById() {
		when(repository.findById(anyString())).thenReturn(Mono.just(recipe));
		
		assertEquals(recipe.getDescription(), service.findRecipeCommandById("1").block().getDescription());
		
		verify(repository, times(1)).findById(anyString());
	}
	
	@Test
	void testFindCategoryByDescription_Existing() {
		when(catRepo.findByDescriptionIgnoreCase(anyString())).thenReturn(Mono.just(category));
		var categoryReturned = service.findCategoryByDescription(CATEGORY_NAME);
		assertEquals(category.getDescription(), categoryReturned.block().getDescription());
		
		verify(catRepo, times(1)).findByDescriptionIgnoreCase(anyString());
	}
	
	@Test
	void testFindCategoryByDescription_New() {
		when(catRepo.findByDescriptionIgnoreCase(anyString())).thenReturn(Mono.empty());
		when(catRepo.save(any(Category.class))).thenReturn(Mono.just(category2));
		
		assertEquals(CATEGORY_NAME2, service.findCategoryByDescription(CATEGORY_NAME2).block().getDescription());
		
		verify(catRepo, times(1)).findByDescriptionIgnoreCase(anyString());
		verify(catRepo, times(1)).save(any(Category.class));
	}
	
	@Test
	void testFindUomString() {
		when(uomRepo.findByUomIgnoreCase(anyString())).thenReturn(Mono.just(uom1));
		assertEquals(uom1.getUom(), service.findUom(anyString()).block().getUom());
		
		verify(uomRepo, times(1)).findByUomIgnoreCase(anyString());
	}

	@Test
	void testFindUomLong() {
		when(uomRepo.findById(anyString())).thenReturn(Mono.just(uom1));
		assertEquals(uom1.getUom(), service.findUom("Spoon", true).block().getUom());
		
		verify(uomRepo, times(1)).findById(anyString());
	}

	@Test
	void testFindAllUoms() {
		when(uomRepo.findAll()).thenReturn(Flux.just(uom1));
		List<UnitOfMeasureCommand> uomList = service.findAllUoms().collectList().block();
		
		assertEquals(List.of(uom1), List.of(uomc2uom.convert(uomList.get(0))));
		
		verify(uomRepo, times(1)).findAll();
	}

	@Test
	void testDelete() {
		service.delete("1");
		verify(repository, times(1)).deleteById("1");
	}
	
	@Test
	void testSaveRecipeCommand() {
		when(repository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));
		when(catRepo.findByDescriptionIgnoreCase(CATEGORY_NAME)).thenReturn(Mono.just(category));
		when(catRepo.findByDescriptionIgnoreCase(CATEGORY_NAME2)).thenReturn(Mono.empty());
		when(catRepo.save(any(Category.class))).thenReturn(Mono.just(category2));
		
		RecipeCommand savedRecipeCommand  = service.saveRecipeCommand(recipeToRecipeCommand.convert(recipe)).block();
		
		assertEquals(recipe.getDescription(), savedRecipeCommand.getDescription());
		
		verify(repository, times(2)).save(any(Recipe.class));
		verify(catRepo, times(2)).findByDescriptionIgnoreCase(anyString());
		verify(catRepo, times(1)).save(any(Category.class));
	}
	
}
