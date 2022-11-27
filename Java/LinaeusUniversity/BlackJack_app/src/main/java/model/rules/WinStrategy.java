package model.rules;

import model.Player;

/**
 * an interface that show a possible win strategy.
 */
public interface WinStrategy {
  boolean playerWins(Player player, Player dealer);
}
