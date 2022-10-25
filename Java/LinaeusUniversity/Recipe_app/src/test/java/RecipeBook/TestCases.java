package RecipeBook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;


class TestCases {
  /* Here are the test cases for the class 'Ingredient' */
  @Test
  void testConstructor1() {
    /* test first constructor */
    Ingredient i = new Ingredient("fakeName", "fakeUnit", 10.0, Ingredient.Labels.DAIRY);
    assertEquals("fakeName", i.getName());
    assertEquals("fakeUnit", i.getUnit());
    assertEquals(10.0, i.getPrice());
    assertEquals(Ingredient.Labels.DAIRY, i.getLabel());
  }

  @Test
  void testConstructor2() {
    /* test second constructor */
    /* all getters will be tested in this test case, so there is no need to do separate tests */
    Ingredient i = new Ingredient("fakeName", "fakeUnit", 10.0, 5, "comment");
    assertEquals("fakeName", i.getName());
    assertEquals("fakeUnit", i.getUnit());
    assertEquals(10.0, i.getPrice());
    assertEquals(5, i.getAmount());
    assertEquals("comment", i.getComment());
  }

  /* Here are the test cases for the class 'Recipe' */
  @Test
  void testRecipeConstructor() {
    /* test constructor and getters */
    Ingredient in1 = new Ingredient("in1", "unit1", 1.0, 1, "comment");
    Ingredient in2 = new Ingredient("in2", "unit2", 2.0, 1, "comment");
    Ingredient in3 = new Ingredient("in3", "unit3", 3.0, 1, "comment");
    ArrayList<Ingredient> ins = new ArrayList<>();
    ins.add(in1);
    ins.add(in2);
    ins.add(in3);
    Recipe r = new Recipe("recipeName", 2, ins, "instructions");
    assertNotNull(r);
    assertEquals("recipeName", r.getName());
    assertEquals(2, r.getPortions());
    assertEquals(ins, r.getIngredients());
    assertEquals("instructions", r.getInstructions());
  }

  @Test
  void testCalculatePrice() {
    /* test method calculatePrice() */
    Ingredient in1 = new Ingredient("in1", "unit1", 1.0, 1, "comment");
    Ingredient in2 = new Ingredient("in2", "unit2", 2.0, 1, "comment");
    Ingredient in3 = new Ingredient("in3", "unit3", 3.0, 1, "comment");
    ArrayList<Ingredient> ins = new ArrayList<>();
    ins.add(in1);
    ins.add(in2);
    ins.add(in3);
    Recipe r = new Recipe("recipeName", 2, ins, "instructions");
    assertEquals(6.0, r.calculatePrice());
  }

  /* Here are the test cases for the class 'Book' */
  @Test
  void testBookConstructor() {
    /* test constructor and getters */
    Book b = new Book();
    assertNotNull(b);  // a new Book object b is created, so b should not be null
    assertNotNull(b.getIngredientList());  // an ArrayList<Ingredient> object is created, it shouldn't be null
    assertNotNull(b.getRecipeList());      // an ArrayList<Recipe> object is created, it shouldn't be null
  }

  @Test
  void testAddIngredient() {
    /* test method addIngredient() */
    Book b = new Book();
    b.addIngredient("name", "unit", 10.0, Ingredient.Labels.MEAT);
    assertEquals("name", b.getIngredientList().get(0).getName());  // the Ingredient object's name attribute should have the value 'name'
  }

  @Test
  void testDeleteIngredient() {
    /* test method deleteIngredient() */
    Book b = new Book();
    b.addIngredient("name", "unit", 10.0,Ingredient.Labels.MEAT);
    assertEquals(1, b.getIngredientList().size());  // the ArrayList size should be 1
    b.deleteIngredient(b.getIngredientList().get(0));
    assertEquals(0, b.getIngredientList().size());  // the ArrayList size should be 0
  }

  @Test
  void testNotHaveIngredient() {
    /* test method notHaveIngredient() */
    Book b = new Book();
    b.addIngredient("name", "unit", 10.0, Ingredient.Labels.MEAT);
    Ingredient i = new Ingredient("name", "unit", 10.0, Ingredient.Labels.MEAT);
    assertFalse(b.notHaveIngredient(i));  // The ArrayList<Ingredient> in Book contains ingredient i, the expression should be evaluated to false
  }

  @Test
  void testAddRecipe() {
    /* test method addRecipe() */
    Book b = new Book();
    Ingredient in1 = new Ingredient("in1", "unit1", 1.0, 1, "comment");
    Ingredient in2 = new Ingredient("in2", "unit2", 2.0, 1, "comment");
    Ingredient in3 = new Ingredient("in3", "unit3", 3.0, 1, "comment");
    ArrayList<Ingredient> ins = new ArrayList<>();
    ins.add(in1);
    ins.add(in2);
    ins.add(in3);
    b.addRecipe("recipeName", 1, ins, "instructions");
    assertEquals("recipeName", b.getRecipeList().get(0).getName());  // the Recipe object's name attribute should have value 'recipeName'
  }

  @Test
  void testDeleteRecipe() {
    /* test method deleteRecipe() */
    Book b = new Book();
    Ingredient in1 = new Ingredient("in1", "unit1", 1.0, 1, "comment");
    Ingredient in2 = new Ingredient("in2", "unit2", 2.0, 1, "comment");
    Ingredient in3 = new Ingredient("in3", "unit3", 3.0, 1, "comment");
    ArrayList<Ingredient> ins = new ArrayList<>();
    ins.add(in1);
    ins.add(in2);
    ins.add(in3);
    Recipe delete = null;
    b.addRecipe("recipeName", 1, ins, "instructions");  // add a recipe
    assertEquals(1, b.getRecipeList().size());  // recipe list's size should be 1
    for(Recipe r: b.getRecipeList()) {
      if(r.getName().equals("recipeName")) {
        delete = r;
      }
    }
    if(delete != null) {
      b.deleteRecipe(delete);  // remove the recipe
    }
    assertEquals(0, b.getRecipeList().size());  // recipe list's size should be 0
  }

  /* Here are test cases for the class "Main" */
  @Test
  void testMain() {
    /* test constructor */
    Main main = new Main();
    assertNotNull(main.getBook());
    assertNotNull(main.getUi());
    assertNotNull(main.getScanner());
  }
  

  @Test
  void testAddIngredientObjects(){
    /* test method addIngredientObjects() */
    Main m = new Main();
    String s = "sugar,grams,0.01,vegan;beef,kg,200,meat,;strawberry,pieces,5.0,vegan";
    m.addIngredientObjects(s);
    assertNotNull(m.getBook().getIngredientList());
  }

  @Test
  void testAddRecipeObject() {
    /* test method addRecipeObject() */
    Main m = new Main();
    String ingredientStr = "[1,litre,flour,batter#50,grams,sugar#3,pieces,egg]";
    String instructions = "*Mix everything * Put in oven * Wait for one hour";
    m.addRecipeObject("testRecipe", 2, ingredientStr, instructions);
    assertEquals("testRecipe", m.getBook().getRecipeList().get(0).getName());
  }

}
