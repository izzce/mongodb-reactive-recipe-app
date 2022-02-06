package org.izce.mongodb_recipe.services;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.IngredientCommandToIngredient;
import org.izce.mongodb_recipe.converters.IngredientToIngredientCommand;
import org.izce.mongodb_recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.repositories.reactive.IngredientReactiveRepository;
import org.izce.mongodb_recipe.repositories.reactive.UomReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientReactiveRepository ingredientRepo;
    private final UomReactiveRepository uomRepo;
    private final IngredientCommandToIngredient ingc2ing;
    private final IngredientToIngredientCommand ing2ingc;
    private final UnitOfMeasureToUnitOfMeasureCommand uom2uomc;

	@Autowired
	public IngredientServiceImpl(
			IngredientReactiveRepository ir,
			IngredientCommandToIngredient ingc2ing,
			IngredientToIngredientCommand ing2ingc,
			UomReactiveRepository uomr,
			UnitOfMeasureToUnitOfMeasureCommand uom2uomc) {
		
		log.debug("Initializing IngredientServiceImpl...");
		this.ingredientRepo = ir;
		this.ingc2ing = ingc2ing;
		this.ing2ingc = ing2ingc;
		this.uomRepo = uomr;
		this.uom2uomc = uom2uomc;
	}
	
	@Override
	public Mono<Ingredient> findById(String id) {
		return ingredientRepo.findById(id);
	}

	@Override
	public Mono<IngredientCommand> findIngredientCommandById(String id) {
		return findById(id).map(ing2ingc::convert);
	}
	
	@Override
	@Transactional
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {
		Ingredient ingredient = ingc2ing.convert(ingredientCommand);

		log.info("Ingredient to be saved: {}", ingredient);
		
		return ingredientRepo.save(ingredient).map(ing2ingc::convert);
	}
	
	@Override
	@Transactional
	public Mono<Void> delete(String ingredientId) {
		log.info("Ingredient to be deleted: {}", ingredientId);		 
		
		return ingredientRepo.deleteById(ingredientId);
	}
	
	@Override
	public Mono<UnitOfMeasureCommand> findUom(String uom) {
		return uomRepo.findByUomIgnoreCase(uom).map(uom2uomc::convert);
		
		//throw new NoSuchElementException("No such UnitOfMeasure defined: " + uom);
	}

	@Override
	public Mono<UnitOfMeasureCommand> findUom(String uomId, boolean flag) {
		return uomRepo.findById(uomId).map(uom2uomc::convert);
		
		//throw new NoSuchElementException("No such UnitOfMeasure defined: " + uomId);
	}
	
	@Override
	public Flux<UnitOfMeasureCommand> findAllUoms() {
		return uomRepo.findAll().map(uom2uomc::convert);
	}
	
}

