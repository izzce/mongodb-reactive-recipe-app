package org.izce.mongodb_recipe.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.springframework.format.Formatter;

public class CategoryFormatter implements Formatter<CategoryCommand> {

	@Override
	public String print(CategoryCommand object, Locale locale) {
		return object.getDescription();
	}

	@Override
	public CategoryCommand parse(String text, Locale locale) throws ParseException {
		CategoryCommand cc = new CategoryCommand();
		cc.setDescription(text);
		return cc;
	}

}
