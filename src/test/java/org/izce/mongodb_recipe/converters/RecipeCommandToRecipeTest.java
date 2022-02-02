package org.izce.mongodb_recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.izce.mongodb_recipe.commands.CategoryCommand;
import org.izce.mongodb_recipe.commands.DirectionCommand;
import org.izce.mongodb_recipe.commands.IngredientCommand;
import org.izce.mongodb_recipe.commands.NoteCommand;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.commands.UnitOfMeasureCommand;
import org.izce.mongodb_recipe.converters.CategoryCommandToCategory;
import org.izce.mongodb_recipe.converters.DirectionCommandToDirection;
import org.izce.mongodb_recipe.converters.IngredientCommandToIngredient;
import org.izce.mongodb_recipe.converters.NoteCommandToNote;
import org.izce.mongodb_recipe.converters.RecipeCommandToRecipe;
import org.izce.mongodb_recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import org.izce.mongodb_recipe.model.Difficulty;
import org.izce.mongodb_recipe.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecipeCommandToRecipeTest {
    public static final String RECIPE_ID = "1";
    public static final Integer COOK_TIME = Integer.valueOf("5");
    public static final Integer PREP_TIME = Integer.valueOf("7");
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTION_ID = "100";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf("3");
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final String CAT_ID_1 = "1";
    public static final String CAT_ID_2 = "2";
    public static final String INGRED_ID_1 = "3";
    public static final String INGRED_ID_2 = "4";
    public static final String NOTES_ID = "9";

    RecipeCommandToRecipe converter;


    @BeforeEach
    public void setUp() throws Exception {
        converter = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                new DirectionCommandToDirection(), new NoteCommandToNote());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void convert() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setCookTime(COOK_TIME);
        recipeCommand.setPrepTime(PREP_TIME);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setServings(SERVINGS);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setUrl(URL);

        NoteCommand notes = new NoteCommand();
        notes.setId(NOTES_ID);
        notes.setNote("Cook");
        recipeCommand.getNotes().add(notes);

        CategoryCommand category = new CategoryCommand(CAT_ID_1, "Italian");
        CategoryCommand category2 = new CategoryCommand(CAT_ID_2, "Turkish");

        recipeCommand.getCategories().add(category);
        recipeCommand.getCategories().add(category2);

        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(INGRED_ID_1);
        ingredient.setDescription("Sugar");
        ingredient.setAmount(new BigDecimal(0.5f));
        ingredient.setUom(new UnitOfMeasureCommand("0", "Teaspoon"));

        IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId(INGRED_ID_2);
        ingredient2.setDescription("Salt");
        ingredient2.setAmount(new BigDecimal(0.5f));
        ingredient2.setUom(new UnitOfMeasureCommand("1", "Tablespoon"));
        
        recipeCommand.getIngredients().add(ingredient);
        recipeCommand.getIngredients().add(ingredient2);
        
        DirectionCommand direction = new DirectionCommand(DIRECTION_ID, "Stir");
        recipeCommand.getDirections().add(direction);

        //when
        Recipe recipe  = converter.convert(recipeCommand);

        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(1, recipe.getNotes().size());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(1, recipe.getDirections().size());
    }

}