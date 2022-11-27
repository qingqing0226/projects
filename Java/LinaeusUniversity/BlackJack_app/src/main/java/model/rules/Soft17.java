package model.rules;

import model.Card;
import model.Player;

/**
 * Soft 17 is a strategy.
 */
public class Soft17 implements HitStrategy {
  private static final int hitLimit = 17;
  private boolean soft = false;

  /**
   * decides whether the AI hits or not.
   */
  public boolean doHit(Player dealer) {
    for (Card c : dealer.getHand()) {
      if (c.getValue() == Card.Value.Ace) {
        soft = true;
      }
    }
    if (dealer.calcScore() == 16) {
      return soft;
    }
    return dealer.calcScore() < hitLimit;
  }

}
