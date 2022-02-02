package org.izce.mongodb_recipe.formatters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Locale;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.formatters.IngredientFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class IngredientFormatterTest {
	IngredientFormatter ingredientFormatter;
	IngredientCommand ingredientCommand;
	
	@BeforeEach
	void setup() {
		ingredientCommand = new IngredientCommand("1", "1", "sugar", new BigDecimal(1), new UnitOfMeasureCommand("1", "Spoon"));	
		ingredientFormatter = new IngredientFormatter();
	}

	@Test
	void testPrint() {
		String print = ingredientFormatter.print(ingredientCommand, Locale.getDefault());
		assertEquals("1 spoon sugar", print);
	}

	@Test
	void testParse() throws Exception {
		IngredientCommand parsedIngredientCommand = ingredientFormatter.parse("1 spoon sugar", Locale.getDefault());
		assertEquals(ingredientCommand.toString(), parsedIngredientCommand.toString());
	}
}
