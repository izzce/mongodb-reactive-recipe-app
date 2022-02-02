package org.izce.mongodb_recipe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document
public class Direction {
	
	@Id
	private String id;
	@NonNull
	private String direction;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@DBRef
	private Recipe recipe;
}
