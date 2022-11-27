package controller;

import model.DealingCardObserver;
import model.Game;
import view.View;

/**
 * Scenario controller for playing the game.
 */
public class Player implements DealingCardObserver {

  private View view;
  private Game game;

  /**
   * a constructor.
   */
  public Player(View view, Game game) {
    this.view = view;
    this.game = game;

    game.addSubscriber(this);
  }

  /**
   * Runs the play use case.
   * 
   * 
   * @return True as long as the game should continue.
   */
  public boolean play() {
    view.displayWelcomeMessage();

    view.displayDealerHand(game.getDealerHand(), game.getDealerScore());
    view.displayPlayerHand(game.getPlayerHand(), game.getPlayerScore());

    if (game.isGameOver()) {
      view.displayGameOver(game.isDealerWinner());
    }

    view.getInput();

    if (view.wantsToPlay()) {
      game.newGame();
    } else if (view.wantsToHit()) {
      game.hit();
    } else if (view.wantsToStand()) {
      game.stand();
    }

    return !view.wantsToQuit();

  }

  @Override
  public void cardDealingEvent(model.Player p) {
    if (p.getClass().equals(model.Dealer.class)) {
      view.playerIsGivenCard("Dealer");
      view.displayDealerHand(game.getDealerHand(), game.getDealerScore());
    } else {
      view.playerIsGivenCard("Player");
      view.displayPlayerHand(game.getPlayerHand(), game.getPlayerScore());
    }

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }
}