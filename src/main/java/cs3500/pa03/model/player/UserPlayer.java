package cs3500.pa03.model.player;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import java.util.List;

/**
 * Represents a BattleSalvo player playing through the console.
 */
public class UserPlayer extends BattleSalvoPlayer {
  private UserPlayerShots recentShots;

  /**
   * Creates a new BattleSalvo player given default empty parameters.
   *
   * @param c the shots taken so far
   * @param s the ships placed
   */
  public UserPlayer(List<Coord> c, List<Ship> s, UserPlayerShots u) {
    super(c, s);
    recentShots = u;
  }

  /**
   * Returns the name of this player.
   *
   * @return a string of this name
   */
  @Override
  public String name() {
    return "Console Player";
  }

  /**
   * Returns this player's shots on the opponent's board,
   * obtained from the shots container of this.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    shotAtOpponent.addAll(recentShots.getShots());
    return recentShots.getShots();
  }
}
