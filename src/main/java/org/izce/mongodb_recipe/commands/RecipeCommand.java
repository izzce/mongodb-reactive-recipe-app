package org.izce.mongodb_recipe.commands;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.izce.mongodb_recipe.model.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCommand {
	private String id;
    
    @NotBlank
    @Size(min=3, max=255)
    private String description;
    @Range(min=1, max=200)
    private Integer prepTime;
    @Range(min=1, max=200)
    private Integer cookTime;
    @Range(min=1, max=100)
    private Integer servings;
    private String source;
    //@NotNull
    private Difficulty difficulty;
    
    @URL
    private String url;
    @URL
    private String imageUrl;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Byte[] image;
    
    //@NotEmpty
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DirectionCommand> directions = new ArrayList<>();
    //@NotEmpty
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<IngredientCommand> ingredients = new ArrayList<>();
    //@NotEmpty
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CategoryCommand> categories = new ArrayList<>();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<NoteCommand> notes = new ArrayList<>();
    
    public RecipeCommand(String id) {
    	this.id = id;
    }
}
