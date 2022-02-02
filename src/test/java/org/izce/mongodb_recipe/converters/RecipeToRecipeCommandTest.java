package org.izce.mongodb_recipe.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.converters.CategoryToCategoryCommand;
import org.izce.mongodb_recipe.converters.DirectionToDirectionCommand;
import org.izce.mongodb_recipe.converters.IngredientToIngredientCommand;
import org.izce.mongodb_recipe.converters.NoteToNoteCommand;
import org.izce.mongodb_recipe.converters.RecipeToRecipeCommand;
import org.izce.mongodb_recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.model.Difficulty;
import org.izce.mongodb_recipe.model.Direction;
import org.izce.mongodb_recipe.model.Ingredient;
import org.izce.mongodb_recipe.model.Note;
import org.izce.mongodb_recipe.model.Recipe;
import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecipeToRecipeCommandTest {

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
    RecipeToRecipeCommand converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new RecipeToRecipeCommand(
                new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new DirectionToDirectionCommand(), new NoteToNoteCommand());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    public void convert() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDescription(DESCRIPTION);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);

        Note note = new Note();
        note.setId(NOTES_ID);

        recipe.getNotes().add(note);

        Category category = new Category("Italian");
        category.setId(CAT_ID_1);

        Category category2 = new Category("Turkish");
        category2.setId(CAT_ID_2);

        recipe.getCategories().add(category);
        recipe.getCategories().add(category2);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGRED_ID_1);
        ingredient.setDescription("Sugar");
        ingredient.setAmount(new BigDecimal(1.0f));
        ingredient.setUom(new UnitOfMeasure("Teaspoon"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGRED_ID_2);
        ingredient2.setDescription("Salt");
        ingredient2.setAmount(new BigDecimal(1.0f));
        ingredient2.setUom(new UnitOfMeasure("Teaspoon"));

        recipe.getIngredients().add(ingredient);
        recipe.getIngredients().add(ingredient2);
        
        Direction direction = new Direction("Cook");
        direction.setId(DIRECTION_ID);
        recipe.getDirections().add(direction);

        //when
        RecipeCommand command = converter.convert(recipe);

        //then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(2, command.getCategories().size());
        assertEquals(2, command.getIngredients().size());
        assertEquals(1, command.getDirections().size());        
        assertEquals(1, command.getNotes().size());

    }

}
