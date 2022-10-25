package RecipeBook;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.FileSystems;

public class Main {
  private Book book;
  private ConsoleUI ui;
  private Scanner scanner;

  Main(){
    book = new Book();
    ui = new ConsoleUI();
    scanner = new Scanner(System.in);
  }


  public Book getBook() {
    return this.book;
  }

  public ConsoleUI getUi() {
    return this.ui;
  }

  public Scanner getScanner() {
    return this.scanner;
  }
  
  private void digitalCookingBook() {
    loadBook();
    editBook();
    closeBook();
  }

  // read files and initialize Book, Ingredient, Recipe objects
  private void loadBook() {
    String ingredientTxt = readFile("ingredients.txt");
    addIngredientObjects(ingredientTxt);
    String recipeBookTxt = readFile("recipe_book.txt");
    String[] items = recipeBookTxt.split(";");
    int count = 0;
    String[] recipe = new String[4];
    for(int i = 0; i < items.length; i++) {
      recipe[count] = items[i];
      count++;
      if (count == 4) {
        count = 0;
        addRecipeObject(recipe[0], Integer.parseInt(recipe[1]), recipe[2], recipe[3]);
        recipe = new String[4];
      }
    }
  }

  // Ingredient objects of Ingredient ArrayList in Book
  public void addIngredientObjects(String ingredients) {
    String[] ingredientList = ingredients.split(";");
    String[] ingredientDetails;
    for(String i: ingredientList){
      ingredientDetails = i.split(",");
      double price = Double.parseDouble(ingredientDetails[2]);
      Ingredient.Labels l = ui.checkLabel(ingredientDetails[3]);
      book.addIngredient(ingredientDetails[0], ingredientDetails[1], price, l);
    }
  }

  // add a Recipe object in Recipe ArrayList in Book
  public void addRecipeObject(String recipeName, int portions, String ingredientStr, String instructions) {
    // ingredientStr: [amount, unit, name, (comment);...] --> amount, unit, name, comment
    // book.ingredientList: [name, unit, price, label;...] -->  price
    // recipe need: [name, unit, price, amount, comment]
    double price;
    String name;
    ArrayList<Ingredient> ingredientsForRecipe = new ArrayList<>();
    ingredientStr = ingredientStr.substring(1,ingredientStr.length()-1);  // remove []
    String[] ingList = ingredientStr.split("#");  // a string list of ingredients
    String[] ingDetails;  // a specific ingredient: [amount, unit, name, (comment)]

    for(String i: ingList){
      ingDetails = i.split(",");
      name = ingDetails[2];

      for(Ingredient j: book.getIngredientList()){
        if(j.getName().equalsIgnoreCase(name)){
          price = j.getPrice();
          Ingredient ing = new Ingredient(name, ingDetails[1], price, Integer.parseInt(ingDetails[0]), ingDetails[3]);
          ingredientsForRecipe.add(ing);
        }
      }
    }

    book.addRecipe(recipeName, portions, ingredientsForRecipe, instructions);
  }

  // switch among option in main menu
  private void editBook() {
    ui.menu();
    int choice = ui.readChoice(scanner);
    if(choice == 1) {
      workOnIngredient();
    } else if(choice == 2) {
      workOnRecipe();
    } else if(choice == 3) {
      ui.exit();
    } else {
      ui.errorMessage();
      editBook();
    }
  }

  // switch among options in submenu Ingredient
  private void workOnIngredient() {
    int option = ui.ingredientMenu(scanner);
    if(option == 1) {
      ui.addIngredientPrompt(scanner, book);
      workOnIngredient();
    } else if(option == 2) {
      ui.listAvailableIngredients(book);
      workOnIngredient();
    } else if(option == 3) {
      boolean checked = ui.lookAtDetailsOfIngredient(scanner, book);
      if(checked) {
        workOnIngredient();
      } else {
        ui.errorMessage();
        workOnIngredient();
      }
    } else if(option == 4) {
      String ans = ui.deleteIngredientWarning(scanner);
      if(ans.equals("Y")) {
        ui.deleteIngredientPrompt(scanner, book);
        workOnIngredient();
      } else if(ans.equals("N")){
        System.out.println("Deletion cancelled.");
        workOnIngredient();
      } else {
        ui.errorMessage();
        workOnIngredient();
      }
    } else if(option == 5) {
      editBook();
    } else {
      ui.errorMessage();
      workOnIngredient();
    }
  }

  // switch among options in submenu Recipe
  private void workOnRecipe() {
    int option = ui.recipeMenu(scanner);
    if(option == 1) {
      ui.addRecipePrompt(scanner, book);
      workOnRecipe();
    } else if(option == 2) {
      ui.listAvailableRecipes(book);
      workOnRecipe();
    } else if(option == 3) {
      ui.lookAtRecipe(scanner, book);
      workOnRecipe();
    } else if(option == 4) {
      ui.deleteRecipePrompt(scanner, book);
      workOnRecipe();
    } else if(option == 5) {
      ui.setStrategy(ui.chooseStrategy(scanner));
      ui.executeStrategy(scanner, book);
      workOnRecipe();
    } else if(option == 6) {
      editBook();
    } else {
      ui.errorMessage();
      workOnIngredient();
    }
  }

  // save changes to files and close scanner
  private void closeBook() {
    // write to ingredients.txt
    StringBuilder text = new StringBuilder();
    String temp = "";
    for(Ingredient i: book.getIngredientList()) {
      temp = i.getName() + "," + i.getUnit() + "," + i.getPrice() + "," + i.getLabel() + ";";
      text.append(temp);
    }
    writeFile(text.toString(), "ingredients.txt");
    // write to recipe_book.txt
    writeToBook("recipe_book.txt");
    ui.terminateProgram();
    scanner.close();
  }

  private void writeToBook(String fileName) {
    // write to recipe_book.txt
    StringBuilder text = new StringBuilder();
    String temp = "";
    for(Recipe r: book.getRecipeList()) {
      ArrayList<Ingredient> ing = r.getIngredients();
      StringBuilder fullstr = new StringBuilder();
      
      for(Ingredient i: ing) {
        temp = "" + i.getAmount() + "," + i.getUnit() + "," + i.getName() + "," + i.getComment() + "#";
        fullstr.append(temp);
      }
      if(fullstr.length() > 0) {
        fullstr.deleteCharAt(fullstr.length()-1);  // remove last char #
      }
      fullstr.insert(0, "[");
      fullstr.append("]");
      String recipe = r.getName() + ";" + r.getPortions() + ";" + fullstr.toString() + ";" + r.getInstructions() + ";";
      text.append(recipe);
    }
    writeFile(text.toString(), fileName);
  }

  // read from file
  public String readFile(String fileName) {
    StringBuilder text = new StringBuilder();
    try {
      File f = new File("RecipeBook/App/" + fileName);
      Scanner sc = new Scanner(f);
      
      while(sc.hasNext()) {
        String s = sc.nextLine();
        text.append(s);
      }
      sc.close();
    } catch (IOException e) {
      System.out.println("Cannot find or read the file: RecipeBook/App/" + fileName);
    }
    return text.toString();
  }

  // write to file
  public void writeFile(String text, String fileName) {
    try {
      File f = new File("RecipeBook/App/" + fileName);
      PrintWriter pr = new PrintWriter(f);
      pr.print(text);
      pr.close();
    } catch (IOException e) {
      System.out.println("The file RecipeBook/App/" + fileName + " cannot be opened for some reason.");
    }
  }
  public static void main(String[] args) {
    /* You start the application here */
    Main m = new Main();
    m.digitalCookingBook();
  }
}


