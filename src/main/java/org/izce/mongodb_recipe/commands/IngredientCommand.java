package org.izce.mongodb_recipe.commands;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal amount;
    //@NonNull
    private UnitOfMeasureCommand uom;
    
    @Override
    public String toString() {
    	return amount + " " + uom.getUom().toLowerCase() + " " + description; 
    }
}