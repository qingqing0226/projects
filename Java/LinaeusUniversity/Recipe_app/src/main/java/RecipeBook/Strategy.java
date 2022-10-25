package RecipeBook;

import java.util.ArrayList;
import java.util.Scanner;

public interface Strategy {
  public ArrayList<Recipe> search(Scanner sc, Book b);
}
