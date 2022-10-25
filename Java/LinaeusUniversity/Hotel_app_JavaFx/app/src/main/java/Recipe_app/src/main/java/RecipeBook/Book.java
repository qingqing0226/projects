package RecipeBook;

import java.util.ArrayList;

public class Book {
  private ArrayList<Ingredient> ingredientList;
  private ArrayList<Recipe> recipeList;

  public Book () {
    ingredientList = new ArrayList<>();
    recipeList = new ArrayList<>();
  }

  public ArrayList<Ingredient> getIngredientList() {
    return ingredientList;
  }

  public ArrayList<Recipe> getRecipeList() {
    return recipeList;
  }

  // delete an ingredient
  public void deleteIngredient(Ingredient i) {
    ingredientList.remove(i);
  }

  // add an ingredient if it is not already in ingredientList
  public void addIngredient(String name, String unit, double price, Ingredient.Labels l) {
    Ingredient ingredient = new Ingredient(name, unit, price, l);
    if(notHaveIngredient(ingredient)) {
      ingredientList.add(ingredient);
    }
  }

  // help method of addIngredient()
  // if the ingredient is in the list return false, else return true
  public boolean notHaveIngredient(Ingredient in) {
    for(Ingredient i: ingredientList){
      if(i.getName().equalsIgnoreCase(in.getName())) {
        return false;
      }
    }
    return true;
  }

  // delete the recipe  r
  public void deleteRecipe(Recipe r) {
    recipeList.remove(r);
  }

  // add a recipe and add its ingredients if they are not in ingredientList
  public void addRecipe(String name, int portions, ArrayList<Ingredient> ingredients, String instructions){
    Recipe recipe = new Recipe(name, portions, ingredients, instructions);
    recipeList.add(recipe);
    for(Ingredient i: ingredients) {
      if(notHaveIngredient(i)){
        ingredientList.add(i);
      }
    }
  }

}
