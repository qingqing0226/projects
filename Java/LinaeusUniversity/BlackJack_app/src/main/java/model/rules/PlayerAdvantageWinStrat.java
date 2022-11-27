package model.rules;

import model.Player;

public class PlayerAdvantageWinStrat implements WinStrategy {

  /**
   * a win strategy that gives the advantage to the Player.
   */
  @Override
  public boolean playerWins(Player player, Player dealer) {
    if (player.calcScore() == dealer.calcScore()) {
      return true;
    }
    if (player.calcScore() > 21 && dealer.calcScore() <= 21) {
      return false;
    } else if (player.calcScore() <= 21 && dealer.calcScore() > 21) {
      return true;
    } else if (player.calcScore() > 21 && dealer.calcScore() > 21) {
      return true;
    } else {
      return player.calcScore() > dealer.calcScore();
    }
  }

}
