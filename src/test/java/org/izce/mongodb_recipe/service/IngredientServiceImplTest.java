package org.izce.mongodb_recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.IngredientCommandToIngredient;
import org.izce.mongodb_recipe.converters.IngredientToIngredientCommand;
import org.izce.mongodb_recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.izce.mongodb_recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.repositories.IngredientRepository;
import org.izce.mongodb_recipe.repositories.UnitOfMeasureRepository;
import org.izce.mongodb_recipe.services.IngredientService;
import org.izce.mongodb_recipe.services.IngredientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class IngredientServiceImplTest {
	IngredientService ingredientService;
	Ingredient ingredient;
	UnitOfMeasure uom;
	@Mock IngredientRepository ingRepo; 
	@Mock UnitOfMeasureRepository uomRepo; 
	UnitOfMeasureCommandToUnitOfMeasure uomc2uom;
	UnitOfMeasureToUnitOfMeasureCommand uom2uomc;
	IngredientCommandToIngredient ic2i;
	IngredientToIngredientCommand i2ic;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		uomc2uom = new UnitOfMeasureCommandToUnitOfMeasure();
		uom2uomc = new UnitOfMeasureToUnitOfMeasureCommand();
		ic2i = new IngredientCommandToIngredient(uomc2uom);
		i2ic = new IngredientToIngredientCommand(uom2uomc);
		ingredientService = new IngredientServiceImpl(ingRepo, ic2i, i2ic, uomRepo, 
				new UnitOfMeasureToUnitOfMeasureCommand());
		uom = new UnitOfMeasure();
		uom.setUom("Teaspoon");
		ingredient = new Ingredient("sugar", 1, uom);
	}

	@Test
	void testFindById() {
		when(ingRepo.findById(anyString())).thenReturn(Optional.of(ingredient));
		assertEquals(ingredient.getDescription(), ingredientService.findById("1").getDescription());
		
		verify(ingRepo, times(1)).findById(anyString());
	}

	@Test
	void testFindIngredientCommandById() {
		when(ingRepo.findById(anyString())).thenReturn(Optional.of(ingredient));
		assertEquals(i2ic.convert(ingredient).getDescription(), 
				ingredientService.findIngredientCommandById(anyString()).getDescription());
		
		verify(ingRepo, times(1)).findById(anyString());
	}

	@Test
	void testSaveIngredientCommand() {
		IngredientCommand ic = i2ic.convert(ingredient);
		when(ingRepo.save(any(Ingredient.class))).thenReturn(ingredient);
		IngredientCommand returnedIc = ingredientService.saveIngredientCommand(ic);
		
		assertEquals(ic.getDescription(), returnedIc.getDescription());
		verify(ingRepo, times(1)).save(any(Ingredient.class));
	}

	@Test
	void testDelete() {
		ingredientService.delete("1");
		verify(ingRepo, times(1)).deleteById("1");
	}

	@Test
	void testFindUomString() {
		when(uomRepo.findByUomIgnoreCase(anyString())).thenReturn(Optional.of(uom));
		assertEquals(uom.getUom(), ingredientService.findUom(anyString()).getUom());
		
		verify(uomRepo, times(1)).findByUomIgnoreCase(anyString());
	}

	@Test
	void testFindUomLong() {
		when(uomRepo.findByUomIgnoreCase(anyString())).thenReturn(Optional.of(uom));
		assertEquals(uom.getUom(), ingredientService.findUom("Teaspoon").getUom());
		
		verify(uomRepo, times(1)).findByUomIgnoreCase(anyString());
	}

	@Test
	void testFindAllUoms() {
		when(uomRepo.findAll()).thenReturn(List.of(uom));
		List<UnitOfMeasureCommand> uomList = ingredientService.findAllUoms();
		
		assertEquals(List.of(uom), List.of(uomc2uom.convert(uomList.get(0))));
		
		verify(uomRepo, times(1)).findAll();
	}

}
