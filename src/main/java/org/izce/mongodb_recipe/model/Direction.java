package org.izce.mongodb_recipe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document
public class Direction {
	
	@Id
	private String id;
	@NonNull
	private String direction;
	
//	@ToString.Exclude
//	@EqualsAndHashCode.Exclude
//	private Recipe recipe;
}
