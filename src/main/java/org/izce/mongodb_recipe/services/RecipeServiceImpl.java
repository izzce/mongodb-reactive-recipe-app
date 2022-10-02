package org.izce.mongodb_recipe.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.CategoryToCategoryCommand;
import org.izce.mongodb_recipe.converters.RecipeCommandToRecipe;
import org.izce.mongodb_recipe.converters.RecipeToRecipeCommand;
import org.izce.mongodb_recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.repositories.reactive.CategoryReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.DirectionReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.IngredientReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.NoteReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.RecipeReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.UomReactiveRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
	private final RecipeReactiveRepository recipeRepo;
    private final CategoryReactiveRepository categoryRepo;
    private final IngredientReactiveRepository ingredientRepo;
    private final UomReactiveRepository uomRepo;
    private final NoteReactiveRepository notesRepo;
    private final DirectionReactiveRepository directionRepo;
    
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final CategoryToCategoryCommand cTocc;
    private final UnitOfMeasureToUnitOfMeasureCommand uom2uomc;

	public RecipeServiceImpl(
			RecipeReactiveRepository rr, 
			CategoryReactiveRepository cr, 
			IngredientReactiveRepository ir,
			UomReactiveRepository uomr,
			NoteReactiveRepository nr,
			DirectionReactiveRepository dr,
			RecipeCommandToRecipe rc2r, 
			RecipeToRecipeCommand r2rc,
			UnitOfMeasureToUnitOfMeasureCommand uom2uomc, 
			CategoryToCategoryCommand cTocc) {
		
		log.debug("Initializing RecipeServiceImpl...");
		this.recipeRepo = rr;
		this.categoryRepo = cr;
		this.ingredientRepo = ir;
		this.uomRepo = uomr;
		this.notesRepo = nr;
		this.directionRepo = dr;
		this.recipeCommandToRecipe = rc2r;
		this.recipeToRecipeCommand = r2rc;
		this.uom2uomc = uom2uomc;
		this.cTocc = cTocc;
	}

	@Override
	public Flux<Recipe> getRecipes() {
		return recipeRepo.findAll();
	}
	
	@Override
	public Flux<RecipeCommand> getRecipeCommands() {
		return getRecipes().map(recipeToRecipeCommand::convert);
	}

	@Override
	public Mono<Long> getRecipesCount() {
		return recipeRepo.count();
	}

	@Override
	public Mono<Recipe> findById(String id) {
		return recipeRepo.findById(id);
	}
	
    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);
        
        if (recipe.getId() == null) {
        	recipe = recipeRepo.save(recipe).block();
        }
        
        if (recipe.getCategories().size() > 0) {
        	List<Category> newCategories = new ArrayList<>();
        	for (var c : recipe.getCategories()) {
        		if (c.getId() == null) {
        			Optional<Category> savedCategory = categoryRepo.findByDescriptionIgnoreCase(c.getDescription()).blockOptional();
        			if (savedCategory.isPresent()) {
        				newCategories.add(savedCategory.get());
        			} else {
        				// First save the new category and then add to set of categories (a fresh set)!
        				newCategories.add(categoryRepo.save(c).block());
        			}
        		} else {
        			newCategories.add(c);
        		}
        	}
        	
        	recipe.setCategories(newCategories);
        }
       
        if (recipe.getDirections().size() > 0) {
        	List<Direction> newDirections = new ArrayList<>();
        	for(var d : recipe.getDirections()) {
        		//d.setRecipe(recipe);
        		if (d.getId() == null) {
        			newDirections.add(directionRepo.save(d).block());
        		} else {
        			newDirections.add(d);
        		}
        	}
        	recipe.setDirections(newDirections);
        }
        
        if (recipe.getIngredients().size() > 0) {
        	List<Ingredient> newIngredients = new ArrayList<>();
        	for(var i : recipe.getIngredients()) {
        		//i.setRecipe(recipe);
        		if (i.getId() == null) {
        			newIngredients.add(ingredientRepo.save(i).block());
        		} else {
        			newIngredients.add(i);
        		}
        	}
        	recipe.setIngredients(newIngredients);
        }
        
        if (recipe.getNotes().size() > 0) {
        	List<Note> newNotes = new ArrayList<>();
        	for (var n : recipe.getNotes()) {
        		//n.setRecipe(recipe);
        		if (n.getId() == null) {
        			newNotes.add(notesRepo.save(n).block());
        		} else {
        			newNotes.add(n);
        		}
        	}
        	recipe.setNotes(newNotes);
        }
        
        // log.info("Saved Recipe - id: {}, name: {}", recipe.getId(), recipe.getDescription());
        
        return recipeRepo.save(recipe).map(recipeToRecipeCommand::convert);
    }

	@Override
	public Mono<CategoryCommand> findCategoryByDescription(String description) {
		return categoryRepo.findByDescriptionIgnoreCase(description)
				.switchIfEmpty(Mono.defer(() -> categoryRepo.save(new Category(description))))
				.map(cTocc::convert);
	}
	
	@Override
	public Mono<UnitOfMeasureCommand> findUom(String uom) {
		return uomRepo.findByUomIgnoreCase(uom)
				.switchIfEmpty(Mono.defer(() -> {
					throw new NoSuchElementException("No such UnitOfMeasure defined: " + uom); 
				})).map(uom2uomc::convert);
	}

	@Override
	public Mono<UnitOfMeasureCommand> findUom(String uomId, boolean flag) {
		return uomRepo.findById(uomId)
				.switchIfEmpty(Mono.defer(() -> {
					throw new NoSuchElementException("No such UnitOfMeasure defined: " + uomId);
				})).map(uom2uomc::convert);
	}
	
	@Override
	public Flux<UnitOfMeasureCommand> findAllUoms() {
		return uomRepo.findAll().map((uom2uomc::convert));
	}

	@Override
	public Mono<RecipeCommand> findRecipeCommandById(String id) {
		return this.findById(id).map(recipeToRecipeCommand::convert);
	}

	@Override
	public Mono<Void> delete(String recipeId) {
		return recipeRepo.deleteById(recipeId);
	}

}
