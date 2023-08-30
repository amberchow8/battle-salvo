package cs3500.pa03.model.board;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import java.util.List;

/**
 * Represents a BattleSalvo board.
 */
public abstract class Board {
  protected List<Ship> ships;
  protected Coord[][] cells;

  /**
   * Given a list of ships and board dimensions, creates a 2d array of
   * coordinates with ships at specified locations.
   *
   * @param s a list of ships that exist on this board
   * @param height the height of this board
   * @param width the width of this board
   */
  public Board(List<Ship> s, int height, int width) {
    this.ships = s;
    this.cells = new Coord[height][width];

    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        // creates coordinates with default status NOTHING
        Coord c = new Coord(w, h);
        this.cells[h][w] = c;
        for (Ship ship : this.ships) {
          if (ship.overlapsCoord(c)) {
            // if it overlaps the ships of this, change its status
            this.cells[h][w].updateStatus(CellStatus.UNTOUCHED_SHIP);
          }
        }
      }
    }
  }

  /**
   * Returns the cells of this board.
   *
   * @return a 2d array of coords from this board
   */
  public Coord[][] getBoard() {
    return cells;
  }

  /**
   * Returns the string representation of this board.
   *
   * @return the board as a 2d String array
   */
  public abstract String[][] convert();

  /**
   * Determines how many ships are left on this board.
   *
   * @return an int representing ships that haven't sunk
   */
  public int amtShipsLeft() {
    int shipsInPlay = 0;

    // iterate over the cells of ships on this board
    for (Ship ship : ships) {
      int cellsNotSunk = ship.getCells().size();
      for (Coord shipCoord : ship.getCells()) {

        // iterate over the cells of this board
        for (int i = 0; i < cells.length; i++) {
          for (int j = 0; j < cells[0].length; j++) {

            // if they overlap and the cell on this board is hit, subtract one
            if ((cells[i][j].getStatus().equals(CellStatus.HIT_SHIP))
                && cells[i][j].equals(shipCoord)) {
              cellsNotSunk = cellsNotSunk - 1;
            }
          }
        }
      }
      if (cellsNotSunk != 0) {
        shipsInPlay += 1;
      }
    }
    return shipsInPlay;
  }

  /**
   * Determines if the given coordinate is open on this board.
   *
   * @param shot the coord with location to check
   * @return true if the coord has yet to be hit
   */
  public boolean isCoordOpen(Coord shot) {
    // for all coordinates in this board
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[0].length; j++) {
        Coord current = cells[i][j];
        if (current.overlaps(shot) && (current.getStatus() == CellStatus.UNTOUCHED_SHIP
            || current.getStatus() == CellStatus.NOTHING)) {
          // return true if the current cell is either an open spot or an untouched ship
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Counts how many cells are still open on this board.
   *
   * @return the amount of cells on this board that can still be shot at
   */
  public int amtOpenCells() {
    int openCells = 0;
    // for all coordinates in this board
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[0].length; j++) {

        // count how many cells are untouched
        if (cells[i][j].getStatus().equals(CellStatus.UNTOUCHED_SHIP)
            || cells[i][j].getStatus().equals(CellStatus.NOTHING)) {
          openCells += 1;
        }
      }
    }
    return openCells;
  }

  /**
   * Given a list of coordinates and their status, update this board to match.
   *
   * @param coordsToUpdate the coordinates of the most recent opponent shots
   */
  public void updateBoard(List<Coord> coordsToUpdate) {
    // for all cells of this board
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[0].length; j++) {
        for (Coord shot : coordsToUpdate) {
          Coord current = cells[i][j];
          // change its status if it is in the given list
          if (current.overlaps(shot) && current.getStatus() == CellStatus.UNTOUCHED_SHIP) {
            current.updateStatus(CellStatus.HIT_SHIP);
          } else if (current.overlaps(shot) && current.getStatus() == CellStatus.NOTHING) {
            current.updateStatus(CellStatus.MISSED_SHIP);
          }
        }
      }
    }
  }

}
