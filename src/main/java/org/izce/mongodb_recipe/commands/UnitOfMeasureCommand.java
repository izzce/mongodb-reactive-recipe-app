package org.izce.mongodb_recipe.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitOfMeasureCommand {
    private String id;
    private String uom;
    
}