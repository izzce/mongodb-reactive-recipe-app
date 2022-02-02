package org.izce.mongodb_recipe.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
	@DBRef(db = "unitOfMeasure")
	private UnitOfMeasure uom;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@DBRef
	private Recipe recipe;

	public Ingredient(String description, float amount, UnitOfMeasure uom) {
		this(description, new BigDecimal(amount), uom);
	}
}
