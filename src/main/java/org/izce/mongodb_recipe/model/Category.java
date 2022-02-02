package org.izce.mongodb_recipe.model;

import java.util.LinkedHashSet;
import java.util.Set;

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
public class Category {
	
	@Id
	private String id;
	@NonNull
	private String description;
	
	@ToString.Exclude 
	@EqualsAndHashCode.Exclude
	@DBRef
	private Set<Recipe> recipes = new LinkedHashSet<Recipe>();

}
