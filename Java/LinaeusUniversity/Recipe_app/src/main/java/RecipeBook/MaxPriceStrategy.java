package RecipeBook;

import java.util.ArrayList;
import java.util.Scanner;

public class MaxPriceStrategy implements Strategy {

  @Override
  public ArrayList<Recipe> search(Scanner sc, Book b) {
    ArrayList<Recipe> recipes = new ArrayList<>();
    System.out.print("Enter max price for the recipe(s): ");
    double max_price = 0;
    double price;

    if(sc.hasNextDouble()) {
      max_price = sc.nextDouble();
    }
    // add the recipe to the ArrayList 'recipes' if its price is less than or equal to max price
    for(Recipe r: b.getRecipeList()) {
      price = r.calculatePrice();
      if(price <= max_price) {
        recipes.add(r);
      }
    }
    return recipes;
  }
  
}
