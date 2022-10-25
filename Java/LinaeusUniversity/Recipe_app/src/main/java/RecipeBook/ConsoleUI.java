package RecipeBook;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {
  private Strategy strategy;

  public void menu() {
    System.out.println("\n---------------- Main Menu -------------------");
    System.out.println("[1] Work with ingredients.");
    System.out.println("[2] Work with recipes");
    System.out.println("[3] Exit.");
  }

  public int ingredientMenu(Scanner sc) {
    System.out.println("\n---------------- Ingredient -------------------");
    System.out.println("[1] Add an ingredient.");
    System.out.println("[2] List all available ingredients.");
    System.out.println("[3] Look at the details of an ingredient.");
    System.out.println("[4] Delete an ingredient.");
    System.out.println("[5] Go to main menu");
    return readChoice(sc);
  }

  public int recipeMenu(Scanner sc) {
    System.out.println("\n---------------- Recipe -------------------");
    System.out.println("[1] Add a recipe");
    System.out.println("[2] List all available recipes.");
    System.out.println("[3] Look at a particular recipe");
    System.out.println("[4] Delete a recipe.");
    System.out.println("[5] Search recipes");
    System.out.println("[6] Go to main menu");
    return readChoice(sc);
  }

  // read user input, return an integer
  public int readChoice(Scanner sc) {
    System.out.print("Enter a number: ");
    
    if(sc.hasNext()) {
      String choice = sc.next();
      if(choice.length() == 1 && Character.isDigit(choice.charAt(0))){
        return Integer.parseInt(choice);
      } else {
        return -1;  // return -1 if it is not a one digit number
      }
    }
    return -1;
  }

  // take user input and add an ingredient to the list in Book
  public void addIngredientPrompt(Scanner sc, Book b) {
    System.out.print("Enter name: ");
    sc.nextLine();
    String name = sc.nextLine();
    System.out.print("Enter unit: ");
    String unit = sc.nextLine();
    System.out.print("Enter price: ");
    String price = sc.nextLine();
    System.out.print("Enter label (vegan,gluten,dairy,meat,fish): ");
    String label = sc.nextLine();
    Ingredient.Labels l = checkLabel(label);
    b.addIngredient(name, unit, Double.parseDouble(price), l);
    clearScreen();
    System.out.println(name + " has been successfully added to the ingredient list.");
  }

  // take a string and return its corresponding Labels
  public Ingredient.Labels checkLabel(String s) {
    Ingredient.Labels l = Ingredient.Labels.DEFAULT;
    if(s.equalsIgnoreCase("vegan")) {
      l = Ingredient.Labels.VEGAN;
    } else if(s.equalsIgnoreCase("dairy")) {
      l = Ingredient.Labels.DAIRY;
    } else if(s.equalsIgnoreCase("gluten")) {
      l = Ingredient.Labels.GLUTEN;
    } else if(s.equalsIgnoreCase("fish")) {
      l = Ingredient.Labels.FISH;
    } else if(s.equalsIgnoreCase("meat")) {
      l = Ingredient.Labels.MEAT;
    }
    return l;
  }
  
  // print a list of available ingredients from the book
  public void listAvailableIngredients(Book b) {
    ArrayList<Ingredient> list = b.getIngredientList();
    for(Ingredient i: list) {
      System.out.println(i.getName());
    }
  }

  // look at the details of an Ingredient
  // return true if it exists, otherwise false
  public boolean lookAtDetailsOfIngredient(Scanner sc, Book b) {
    System.out.print("Enter ingredient name: ");
    sc.nextLine();
    String name = sc.nextLine();

    ArrayList<Ingredient> list = b.getIngredientList();
    System.out.println(name);
    for(Ingredient i: list) {
      if(i.getName().equalsIgnoreCase(name)){
        System.out.println("Unit: " + i.getUnit());
        System.out.println("Price: " + i.getPrice());
        System.out.println("Label: " + i.getLabel());
        return true;
      }
    }
    return false;
  }

  // a warning for the user who decides to delete an ingredient
  public String deleteIngredientWarning(Scanner sc) {
    System.out.println("Deletion of an ingredient may affect some recipes.\nNext time when you start the application, the affected recipes may have an incomplete ingredient list.");
    System.out.print("Are you sure you want to do this? (Y/N) ");
    return sc.next().toUpperCase();
  }

  // remove an Ingredient from Ingredient ArrayList in Book
  public void deleteIngredientPrompt(Scanner sc, Book b) {
    System.out.print("Enter ingredient name: ");
    sc.nextLine();
    String name = sc.nextLine();
    ArrayList<Ingredient> in = b.getIngredientList();
    Ingredient removed = null;

    for(Ingredient i: in){
      if(i.getName().equalsIgnoreCase(name)) {
        removed = i;
      }
    }

    // check if the Ingredient exists
    if(removed != null){
      b.deleteIngredient(removed);
      System.out.println(removed.getName() + " has been successfully deleted from the ingredient list.");
    } else {
      errorMessage();
    }
  }

  // add a recipe
  public void addRecipePrompt(Scanner sc, Book b) {
    System.out.print("Enter recipe name: ");
    sc.nextLine();
    String name = sc.nextLine();
    System.out.print("Enter number of portions: ");
    int portions = sc.nextInt();
    sc.nextLine();
    /* add ingredients */
    System.out.println("\nSpecify ingredient details: ");
    ArrayList<Ingredient> igdForRecipe = new ArrayList<>();
    String[] details;
    Ingredient i;
    double price;
    int amount;
    Ingredient.Labels l = null;
    boolean reading = true;
    while(reading){
      details = readIngredientDetails(sc, b);
      price = Double.parseDouble(details[2]);
      amount = Integer.parseInt(details[3]);
      if(details[5] != null) {
        l = checkLabel(details[5]);
        b.addIngredient(details[0], details[1], price, l);
      }
      i = new Ingredient(details[0], details[1], price, amount, details[4]);
      igdForRecipe.add(i);
      System.out.print("Add more ingredients? (Y/N) ");
      String ans = sc.nextLine().toUpperCase();
      if(ans.equals("N")) {
        reading = false;
      } else if(!ans.equals("Y")) {
        errorMessage();
      }
    }
    /* add instructions */
    System.out.println("\nWrite instructions:");
    System.out.print("How many steps are there in the instructions? ");
    int steps = sc.nextInt();
    sc.nextLine();
    StringBuilder instructions = new StringBuilder();
    for(int j = 0; j < steps; j++) {
      instructions.append("* " + addInstruction(sc, j+1));
    }
    /* add the recipe */
    b.addRecipe(name, portions, igdForRecipe, instructions.toString());
    System.out.println("Recipe " + name +  " has been successfully added to the book.");
  }

  // read Ingredient details and return a string array
  public  String[] readIngredientDetails(Scanner sc, Book b) {
    String[] details = new String[6];
    System.out.print("Enter ingredient name: ");
    details[0] = sc.nextLine();
    /* if the Ingredient exists */
    for(Ingredient i: b.getIngredientList()) {
      if(details[0].equalsIgnoreCase(i.getName())) {
        System.out.println("Great! This ingredient is already listed in the book.");
        System.out.println("(" + i.getName() + " unit: " + i.getUnit() + " price: " + i.getPrice() + ")");
        details[0] = i.getName();
        details[1] = i.getUnit();
        details[2] = i.getPrice() + "";
        System.out.print("Enter amount: ");
        details[3] = sc.nextLine();
        System.out.print("Enter comment (optional; i.e. what is it used for): ");
        details[4] = checkComment(sc);
        details[5] = null;
        return details;
      }
    }
    /* if the Ingredient does not exist */
    System.out.println("This ingredient is not listed in the book. More details are needed.");
    System.out.print("Enter unit: ");
    details[1] = sc.nextLine();
    System.out.print("Enter price (per unit): ");
    details[2] = sc.nextLine();
    System.out.print("Enter amount: ");
    details[3] = sc.nextLine();
    System.out.print("Enter comment (optional; i.e. what is it used for): ");
    details[4] = checkComment(sc);
    System.out.print("Enter label (vegan,gluten,dairy,meat,fish): ");
    details[5] = sc.nextLine();
    return details;
  }
  public String checkComment(Scanner sc) {
    String comment = "";
    comment = sc.nextLine();
    if(comment.equals("")){
      return ".";
    } else {
      return comment;
    }
  }

  // add step by step instruction
  // return a string as one step
  public String addInstruction(Scanner sc, int index) {
    System.out.print("Step " + index + ": ");
    return sc.nextLine();
  }

  public void listAvailableRecipes(Book b) {
    for (Recipe r: b.getRecipeList()) {
      System.out.println(r.getName());
    }
  }

  // list recipe name, portions, price, ingredients, instructions
  public void lookAtRecipe(Scanner sc, Book b){
    ArrayList<Recipe> list = b.getRecipeList();
    Recipe r = null;
    System.out.print("Enter recipe name: ");
    sc.nextLine();
    String n = sc.nextLine();
    System.out.print("Enter number of portions: ");
    int p = Integer.parseInt(sc.nextLine());
    // look for the recipe in the list
    for(Recipe re: list) {
      if(re.getName().equalsIgnoreCase(n)) {
        r = re;
      }
    }
    // if the recipe is found
    if(r != null) {
      double times = (double) p/r.getPortions();
      double totalPrice = 0;
      double price;
      for(Ingredient i: r.getIngredients()) {
        price = Math.ceil(i.getAmount()*times)*i.getPrice();  // round to the nearest integer greater than or equal to amount
        totalPrice += price;
      }
      String[] instructions = r.getInstructions().split("\\*");
      presentRecipe(r, p, totalPrice, times, instructions, b);
    } else {
      errorMessage();
    }
  }

  // print the recipe details
  public void presentRecipe(Recipe recipe, int portions, double price, double times, String[] instructions, Book b) {
    System.out.println("\nNAME: " + recipe.getName());
    System.out.println("PORTIONS: " + portions);
    System.out.println("TOTAL PRICE: " + price);
    System.out.println("INGREDIENT LIST: ");
    for(Ingredient i: recipe.getIngredients()) {
      boolean available = false;
      // check if it is available
      for(Ingredient in: b.getIngredientList()) {
        if(i.getName().equalsIgnoreCase(in.getName())) {
          available = true;
        }
      }
      String a = available? " (Available) " : " (Unavailable) ";
      System.out.println("\t - " + Math.ceil(i.getAmount()*times) + " " +  i.getUnit() + " " + i.getName() + a + i.getComment());
      
    }
    System.out.println("INSTRUCTIONS: ");
    for(int j = 1; j < instructions.length; j++) {  // j=1 --> skip first element(empty string)
      System.out.println(">>>>> Step " + j + ": " + instructions[j]);
    }
  }

  // delete a recipe according to the name
  public void deleteRecipePrompt(Scanner sc, Book b) {
    System.out.print("Enter recipe name: ");
    sc.nextLine();
    String n = sc.nextLine();
    Recipe delete = null;
    for(Recipe r: b.getRecipeList()){
      if(n.equalsIgnoreCase(r.getName())){
        delete = r;
      }
    }
    if(delete != null) {
      b.deleteRecipe(delete);
      System.out.println(delete.getName() + " has been successfully deleted from the book.");
    } else {
      errorMessage();
    }
  }

  // let user choose a search strategy and return it
  public Strategy chooseStrategy(Scanner sc) {
    System.out.println("---------------- Search -------------------");
    System.out.println("[1]  Based on ingredient name.");
    System.out.println("[2]  Based on recipe max price.");
    System.out.println("[3]  Based on recipe name.");
    int choice = readChoice(sc);
    if(choice == 1) {
      return new IngredientNameStrategy();
    } else if(choice == 2) {
      return new MaxPriceStrategy();
    } else if(choice == 3) {
      return new RecipeNameStrategy();
    } else {
      clearScreen();
      errorMessage();
      return chooseStrategy(sc);
    }
  }
  // set concrete strategy
  public void setStrategy(Strategy strategy) {
    this.strategy = strategy;
  }

  // execute search strategy and present the results
  public void executeStrategy(Scanner sc, Book b) {
    ArrayList<Recipe> recipes = strategy.search(sc, b);
    System.out.println("Found " + recipes.size() + " recipe(s).\nHere are the results:\n");
    if(!recipes.isEmpty()) {
      if(strategy.getClass() == (new MaxPriceStrategy()).getClass()){
        maxPriceResults(recipes);
      } else {
        for(Recipe r: recipes){
          System.out.println(r.getName());
        }
      }
    }
  }

  // show the prices for found recipes
  public void maxPriceResults(ArrayList<Recipe> recipes) {
    for(Recipe r: recipes){
      System.out.println(r.getName() + "\t (Price: " +  r.calculatePrice() +")");
    }
  }

  public void errorMessage() {
    System.out.println("\nInvalid input.\nPlease try again.\n");
  }

  public void clearScreen() {
    for(int i = 0; i < 30; i++){
      System.out.println();
    }
  }

  public void exit() {
    System.out.println("Prepare for exit...");
  }

  public void terminateProgram() {
    System.out.println("Changes have been saved to the recipe book.\nThe program is terminated.");
  }

}
