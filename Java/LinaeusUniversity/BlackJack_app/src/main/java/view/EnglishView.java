package view;

/**
 * Implements an english console view.
 */
public class EnglishView implements View {
  private int c;

  /**
   * Shows a welcome message.
   */
  public void displayWelcomeMessage() {
    for (int i = 0; i < 50; i++) {
      System.out.print("\n");
    }
    System.out.println("Hello Black Jack World");
    System.out.println("Type 'p' to Play, 'h' to Hit, 's' to Stand or 'q' to Quit\n");
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
    System.out.println(name + " is given a card.");
  }

  public void displayCard(model.Card card) {
    System.out.println("" + card.getValue() + " of " + card.getColor());
  }

  public void displayPlayerHand(Iterable<model.Card> hand, int score) {
    displayHand("Player", hand, score);
  }

  public void displayDealerHand(Iterable<model.Card> hand, int score) {
    displayHand("Dealer", hand, score);
  }

  private void displayHand(String name, Iterable<model.Card> hand, int score) {
    System.out.println(name + " Has: ");
    for (model.Card c : hand) {
      displayCard(c);
    }
    System.out.println("Score: " + score);
    System.out.println("");
  }

  /**
   * Displays the winner of the game.
   * 
   * @param dealerIsWinner True if the dealer is the winner.
   */
  public void displayGameOver(boolean dealerIsWinner) {
    System.out.println("GameOver: ");
    if (dealerIsWinner) {
      System.out.println("Dealer Won!");
    } else {
      System.out.println("You Won!");
    }

  }

}
