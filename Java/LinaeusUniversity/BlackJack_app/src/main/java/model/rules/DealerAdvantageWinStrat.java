package model.rules;

import model.Player;

/**
 * a win strategy that gives the advantage to the dealer.
 */
public class DealerAdvantageWinStrat implements WinStrategy {

  @Override
  public boolean playerWins(Player player, Player dealer) {
    if (player.calcScore() == dealer.calcScore()) {
      return false;
    }
    if (player.calcScore() > 21 && dealer.calcScore() <= 21) {
      return false;
    } else if (player.calcScore() <= 21 && dealer.calcScore() > 21) {
      return true;
    } else if (player.calcScore() > 21 && dealer.calcScore() > 21) {
      return false;
    } else {
      return player.calcScore() > dealer.calcScore();
    }
  }

}
