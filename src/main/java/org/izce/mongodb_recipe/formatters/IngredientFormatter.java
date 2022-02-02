package org.izce.mongodb_recipe.formatters;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.springframework.format.Formatter;

public class IngredientFormatter implements Formatter<IngredientCommand> {

	@Override
	public String print(IngredientCommand object, Locale locale) {
		return object.toString();
	}

	@Override
	public IngredientCommand parse(String text, Locale locale) throws ParseException {
		String [] parts = text.split("\\s+");
		
		IngredientCommand ic = new IngredientCommand();
		if (parts.length >= 1) {
			ic.setAmount(new BigDecimal(parts[0]));
		}
		if (parts.length >= 2) {
			var uofmc = new UnitOfMeasureCommand();
			uofmc.setUom(parts[1]);
			ic.setUom(uofmc);
		}
		if (parts.length >= 3) {
			StringBuilder sb = new StringBuilder();
			for (int i = 2; i<parts.length; i++) {
				sb.append(parts[i]).append(' ');
			}
			
			ic.setDescription(sb.toString().strip());
		}
		return ic;
	}

}
