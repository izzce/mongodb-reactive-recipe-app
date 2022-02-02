package org.izce.mongodb_recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.IngredientCommandToIngredient;
import org.izce.mongodb_recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngredientCommandToIngredientTest {

	public static final Recipe RECIPE = new Recipe();
	public static final BigDecimal AMOUNT = new BigDecimal("1");
	public static final String DESCRIPTION = "Cheeseburger";
	public static final String ID_VALUE = "1";
	public static final String UOM_ID = "2";
	public static final String UOM_NAME = "Pinch";

	IngredientCommandToIngredient converter;

	@BeforeEach
	public void setUp() throws Exception {
		converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
	}

	@Test
	public void testNullObject() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void convert() throws Exception {
		// given
		IngredientCommand command = new IngredientCommand();
		command.setId(ID_VALUE);
		command.setAmount(AMOUNT);
		command.setDescription(DESCRIPTION);
		UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand(UOM_ID, UOM_NAME);
		command.setUom(unitOfMeasureCommand);

		// when
		Ingredient ingredient = converter.convert(command);

		// then
		assertNotNull(ingredient);
		assertNotNull(ingredient.getUom());
		assertEquals(ID_VALUE, ingredient.getId());
		assertEquals(AMOUNT, ingredient.getAmount());
		assertEquals(DESCRIPTION, ingredient.getDescription());
		assertEquals(UOM_ID, ingredient.getUom().getId());
	}

	@Test
	public void convertWithNonNullUOM() throws Exception {
		// given
		UnitOfMeasureCommand uomc = new UnitOfMeasureCommand("1", "Pinch");
		IngredientCommand command = new IngredientCommand();
		command.setId(ID_VALUE);
		command.setAmount(AMOUNT);
		command.setDescription(DESCRIPTION);
		command.setUom(uomc);

		// when
		Ingredient ingredient = converter.convert(command);

		// then
		assertNotNull(ingredient);
		assertEquals(uomc.getId(), ingredient.getUom().getId());
		assertEquals(uomc.getUom(), ingredient.getUom().getUom());
		assertEquals(ID_VALUE, ingredient.getId());
		assertEquals(AMOUNT, ingredient.getAmount());
		assertEquals(DESCRIPTION, ingredient.getDescription());

	}

}
