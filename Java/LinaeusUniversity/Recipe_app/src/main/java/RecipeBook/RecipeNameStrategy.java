package RecipeBook;

import java.util.ArrayList;
import java.util.Scanner;

public class RecipeNameStrategy implements Strategy {

  @Override
  public ArrayList<Recipe> search(Scanner sc, Book b) {
    ArrayList<Recipe> recipes = new ArrayList<>();
    System.out.print("Enter recipe name: ");
    sc.nextLine();
    String n = sc.nextLine();
    for(Recipe r: b.getRecipeList()) {
      if(n.equalsIgnoreCase(r.getName())) {
        recipes.add(r);
      }
    }
    return recipes;
  }
  
}
