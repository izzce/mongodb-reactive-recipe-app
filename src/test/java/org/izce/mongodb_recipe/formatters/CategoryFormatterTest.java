package org.izce.mongodb_recipe.formatters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.formatters.CategoryFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryFormatterTest {
	CategoryFormatter categoryFormatter;
	CategoryCommand categoryCommand;
	

	@BeforeEach
	void setUp() throws Exception {
		categoryCommand = new CategoryCommand("1", "Turkish");	
		categoryFormatter = new CategoryFormatter();
	}
	
	@Test
	void testPrint() {
		String print = categoryFormatter.print(categoryCommand, Locale.getDefault());
		assertEquals("Turkish", print);
	}

	@Test
	void testParse() throws Exception {
		CategoryCommand parsedCategoryCommand = categoryFormatter.parse("Turkish", Locale.getDefault());
		assertEquals(categoryCommand.getDescription(), parsedCategoryCommand.getDescription());
	}

}
