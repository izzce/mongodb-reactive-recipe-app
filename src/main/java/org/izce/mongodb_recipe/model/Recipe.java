package org.izce.mongodb_recipe.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@Document
public class Recipe {
	
	@Id
	private String id;
	@NonNull
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	private Difficulty difficulty;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	private Byte[] image;
	private String imageUrl;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	@DBRef
	private List<Direction> directions = new ArrayList<>();

	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	@DBRef
	private List<Ingredient> ingredients = new ArrayList<>();

	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	@DBRef
	private List<Note> notes = new ArrayList<>();

	@ToString.Exclude
	@EqualsAndHashCode.Exclude 
	@DBRef
	private List<Category> categories = new ArrayList<>();
}
