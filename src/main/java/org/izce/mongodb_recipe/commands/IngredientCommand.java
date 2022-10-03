package org.izce.mongodb_recipe.commands;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

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
    @NotEmpty
    private String description;
    @NonNull
    @Digits(integer=3, fraction=1)
    private BigDecimal amount;
    @NonNull
    private UnitOfMeasureCommand uom;
    
    @Override
    public String toString() {
    	return amount + " " + uom.getUom().toLowerCase() + " " + description; 
    }
}