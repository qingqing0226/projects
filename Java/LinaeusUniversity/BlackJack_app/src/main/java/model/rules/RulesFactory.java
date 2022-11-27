package model.rules;

/**
 * Creates concrete rules.
 */
public class RulesFactory {

  /**
   * Creates the rule to use for the dealer's hit behavior.
   * 
   * @return The rule to use
   */
  public HitStrategy getHitRule() {
    return new Soft17();
  }

  /**
   * Creates the rule to use when starting a new game.
   * 
   * @return The rule to use.
   */
  public NewGameStrategy getNewGameRule() {
    return new AmericanNewGameStrategy();
  }

  /**
   * Creates the rule to use for deciding who wins.
   * 
   * @return The rule to use.
   */
  public WinStrategy getWinStrategy() {
    return new PlayerAdvantageWinStrat();
  }
}