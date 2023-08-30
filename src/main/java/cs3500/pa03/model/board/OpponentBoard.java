package cs3500.pa03.model.board;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import java.util.List;

/**
 * Represents the board of the AI opponent.
 */
public class OpponentBoard extends Board {

  /**
   * Given a list of ships and board dimensions, creates a 2d array of
   * coordinates with ships at specified locations.
   *
   * @param s a list of ships that exist on this board
   * @param height the height of this board
   * @param width the width of this board
   */
  public OpponentBoard(List<Ship> s, int height, int width) {
    super(s, height, width);
  }

  /**
   * Returns a string representation of this board as a grid.
   */
  public String[][] convert() {

    String[][] converted = new String[cells.length][cells[0].length];

    // for every coord in cells, add its string representation to converted
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[0].length; j++) {
        Coord current = cells[i][j];
        converted[i][j] = current.printOpponentStatus();
      }
    }
    return converted;
  }
}
