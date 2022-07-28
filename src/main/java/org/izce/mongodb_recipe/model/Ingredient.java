package org.izce.mongodb_recipe.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Ingredient {
	
	@Id
	private String id;
	@NonNull
	private String description;
	@NonNull
	private BigDecimal amount;
	@NonNull 
	private UnitOfMeasure uom;

	public Ingredient(String description, float amount, UnitOfMeasure uom) {
		this(description, new BigDecimal(amount), uom);
	}
}
