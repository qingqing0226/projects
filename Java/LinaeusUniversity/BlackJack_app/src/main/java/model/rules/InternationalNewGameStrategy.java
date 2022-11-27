package model.rules;

import model.Dealer;
import model.Player;

class InternationalNewGameStrategy implements NewGameStrategy {

  public boolean newGame(Dealer dealer, Player player) {
    dealer.dealCard(player, true);
    dealer.dealCard(dealer, true);
    dealer.dealCard(player, true);

    return true;
  }
}