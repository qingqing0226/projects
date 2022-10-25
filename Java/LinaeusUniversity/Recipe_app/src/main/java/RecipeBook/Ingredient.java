package RecipeBook;

public class Ingredient {
  public enum Labels {
    VEGAN,
    DAIRY,
    GLUTEN,
    FISH,
    MEAT,
    DEFAULT
  }
  private String name;
  private String unit;
  private double price;
  private int amount = 0;
  private String comment = "";  // specify the usage
  private Labels label = Labels.DEFAULT;

  public Ingredient(String name, String unit, double price, Labels label){
    this.name = name;
    this.unit = unit;
    this.price = price;
    this.label = label;
  }

  public Ingredient(String name, String unit, double price, int amount, String comment){
    this.name = name;
    this.unit = unit;
    this.price = price;
    this.amount = amount;
    this.comment = comment;
  }

  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public double getPrice() {
    return price;
  }

  public int getAmount() {
    return amount;
  }

  public String getComment() {
    return comment;
  }

  public Labels getLabel() {
    return label;
  }
}
