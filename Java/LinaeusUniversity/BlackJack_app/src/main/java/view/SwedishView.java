package view;

/**
 * Implements a Swedish console view.
 */
public class SwedishView implements View {
  private int c;

  /**
   * Shows a welcome message.
   */
  public void displayWelcomeMessage() {
    for (int i = 0; i < 50; i++) {
      System.out.print("\n");
    }

    System.out.println("Hej Black Jack Världen");
    System.out.println("----------------------");
    System.out.println("Skriv 'p' för att Spela, 'h' för nytt kort, 's' för att stanna 'q' för att avsluta\n");
  }

  /**
   * Returns pressed characters from the keyboard.
   * 
   * @return the pressed character.
   */
  public void getInput() {
    try {
      c = System.in.read();
      while (c == '\r' || c == '\n') {
        c = System.in.read();
      }
    } catch (java.io.IOException e) {
      System.out.println("" + e);
    }
  }

  public boolean wantsToPlay() {
    return c == 'p';
  }

  public boolean wantsToHit() {
    return c == 'h';
  }

  public boolean wantsToStand() {
    return c == 's';
  }

  public boolean wantsToQuit() {
    return c == 'q';
  }

  public void playerIsGivenCard(String name) {
    System.out.println(name + " fick ett kort.");
  }

  /**
   * Displays a card.
   * 
   * @param card The card to display.
   */
  public void displayCard(model.Card card) {
    if (card.getColor() == model.Card.Color.Hidden) {
      System.out.println("Dolt Kort");
    } else {
      String[] colors = { "Hjärter", "Spader", "Ruter", "Klöver" };
      String[] values = { "två", "tre", "fyra", "fem", "sex", "sju", "åtta", "nio", "tio", "knekt", "dam", "kung",
          "ess" };
      System.out.println("" + colors[card.getColor().ordinal()] + " " + values[card.getValue().ordinal()]);
    }
  }

  public void displayPlayerHand(Iterable<model.Card> hand, int score) {
    displayHand("Spelare", hand, score);
  }

  public void displayDealerHand(Iterable<model.Card> hand, int score) {
    displayHand("Croupier", hand, score);
  }

  /**
   * Displays the winner of the game.
   * 
   * @param dealerIsWinner True if the dealer is the winner.
   */
  public void displayGameOver(boolean dealerIsWinner) {
    System.out.println("Slut: ");
    if (dealerIsWinner) {
      System.out.println("Croupiern Vann!");
    } else {
      System.out.println("Du vann!");
    }
  }

  private void displayHand(String name, Iterable<model.Card> hand, int score) {
    System.out.println(name + " Har: " + score);
    for (model.Card c : hand) {
      displayCard(c);
    }
    System.out.println("Poäng: " + score);
    System.out.println("");
  }

}
