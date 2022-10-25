package RecipeBook;

import java.util.ArrayList;

public class Recipe {
  private String name;
  private int portions;
  private ArrayList<Ingredient> ingredients;
  private String instructions;

  public Recipe(String name, int portions, ArrayList<Ingredient> ingredients, String instructions){
    this.name = name;
    this.portions = portions;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }
  public int getPortions() {
    return portions;
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getInstructions() {
    return instructions;
  }

  // calculate the total price for the recipe
  public double calculatePrice(){
    double sum = 0;
    for (Ingredient i: ingredients) {
      sum += (i.getPrice()*i.getAmount());
    }
    return sum;
  }
}
