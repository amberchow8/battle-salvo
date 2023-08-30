package cs3500.pa03.model.player;

import cs3500.pa03.model.Coord;
import java.util.List;

/**
 * Represents a container for shots taken by the console player.
 */
public class UserPlayerShots {
  private List<Coord> shots;

  /**
   * Creates a UserPlayerShots.
   *
   * @param s the default starting shots
   */
  public UserPlayerShots(List<Coord> s) {
    shots = s;
  }

  /**
   * Given the most recent shots dictated by the console player,
   * updates the shots of this.
   *
   * @param s a list of shots just entered by the user
   */
  public void updateShots(List<Coord> s) {
    shots = s;
  }

  /**
   * Returns the shots of this.
   *
   * @return a list of shots
   */
  public List<Coord> getShots() {
    return shots;
  }


}
