package RecipeBook;

import java.util.ArrayList;
import java.util.Scanner;

public class IngredientNameStrategy implements Strategy {
  // search available recipe based on ingredient name
  @Override
  public ArrayList<Recipe> search(Scanner sc, Book b) {
    ArrayList<String> ingredientStr = new ArrayList<>();
    String n = "";
    sc.nextLine();
    while(!(n = enterIngredient(sc)).equals("")) {  // an empty string stops the loop
      ingredientStr.add(n);
    }
    ArrayList<Recipe> recipes = new ArrayList<>();
    int matched = 0;
    
    for(Recipe r: b.getRecipeList()) {
      ArrayList<Ingredient> inObjects = r.getIngredients();
      // remove duplicate ingredients
      ArrayList<String> availableIngredients = removeDuplicates(inObjects);

      for(String str1: availableIngredients) {
        for(String str2: ingredientStr) {
          if(str2.equalsIgnoreCase(str1)) {
            matched++;
          }
        }
      }
      if(matched >= ingredientStr.size()) {  // recipe contains all ingredients
        recipes.add(r);
      }
      matched = 0;
    }
    return recipes;
  }

  public String enterIngredient(Scanner sc) {
    System.out.print("Enter ingredient name (press ENTER to stop): ");
    return sc.nextLine();
  }

  public ArrayList<String> removeDuplicates(ArrayList<Ingredient> inObjects) {
    ArrayList<String> availableIngredients = new ArrayList<>();
    // remove duplicate ingredients
    for(Ingredient in: inObjects) {
      if(!availableIngredients.contains(in.getName())){
        availableIngredients.add(in.getName());
      }
    }
    return availableIngredients;
  }
  
}
