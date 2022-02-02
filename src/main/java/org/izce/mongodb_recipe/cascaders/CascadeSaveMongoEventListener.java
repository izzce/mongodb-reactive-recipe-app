package org.izce.mongodb_recipe.cascaders;

import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class CascadeSaveMongoEventListener extends AbstractMongoEventListener<Object> {
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<Object> event) {
		Object source = event.getSource();
		
		if (source instanceof Recipe) {
			var recipe = (Recipe) source;
			if (recipe.getCategories() != null) {
				recipe.getCategories().forEach(category -> mongoOperations.save(category));
			}
			if (recipe.getDirections() != null) {
				recipe.getDirections().forEach(direction -> mongoOperations.save(direction));
			}
			if (recipe.getIngredients() != null) {
				for (var ingredient : recipe.getIngredients()) {
					if (ingredient.getUom() != null) {
						mongoOperations.save(ingredient.getUom());
					}
					mongoOperations.save(ingredient);
				}
			}
			if (recipe.getNotes() != null) {
				recipe.getNotes().forEach(note -> mongoOperations.save(note));
			}
		} else if (source instanceof Ingredient) {
			var ingredient = (Ingredient) source;
			if (ingredient.getUom() != null) {
				mongoOperations.save(ingredient.getUom());
			}
		}
	}
}