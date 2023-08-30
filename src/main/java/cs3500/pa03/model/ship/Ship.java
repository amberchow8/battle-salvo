package cs3500.pa03.model.ship;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents one ship in a game of BattleSalvo.
 */
public class Ship {
  private List<Coord> coords;
  private String dir;

  /**
   * Creates a new ShipType with coords default coords.
   *
   * @param type the type of ship this is
   */
  public Ship(ShipType type) {

    ArrayList<Coord> coords = new ArrayList<>();
    for (int i = 0; i < type.getLength(); i++) {
      Coord coord = new Coord(0, i);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.add(coord);

    }
    this.coords = coords;
  }

  /**
   * Getter method that returns the coords that this ship spans.
   *
   * @return the coords of this
   */
  public List<Coord> getCells() {
    return coords;
  }

  /**
   * Determines if this ship overlaps the given coordinate.
   *
   * @param coord the coordinate to compare to this
   * @return whether or not they overlap
   */
  public boolean overlapsCoord(Coord coord) {
    for (Coord c : coords) {
      if (c.overlaps(coord)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the coordinates of this ship to random ones within the given board dimensions.
   *
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @param taken a list of ships already on the board
   */
  public void setupCoords(int boardHeight, int boardWidth, ArrayList<Ship> taken) {
    Random rand = new Random();
    int positionX = rand.nextInt(boardWidth);
    int positionY = rand.nextInt(boardHeight);

    // randomly positions the first cell of this ship
    Coord coord = new Coord(positionX, positionY);
    coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
    coords.set(0, coord);
    int direction = rand.nextInt(4);
    for (int i = 1; i < coords.size(); i++) {
      updateCoords(direction, positionX, positionY, i);
    }

    // check if the ship fits or overlaps taken ships
    boolean fits = canShipFit(boardHeight, boardWidth);
    boolean overlaps = overlapsShips(taken);

    // if this ship doesn't fit or overlap, keep trying
    while ((!fits) || overlaps) {
      positionX = rand.nextInt(boardWidth);
      positionY = rand.nextInt(boardHeight);
      direction = rand.nextInt(4);

      coord = new Coord(positionX, positionY);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.set(0, coord);
      for (int i = 1; i < coords.size(); i++) {
        updateCoords(direction, positionX, positionY, i);
      }

      fits = canShipFit(boardHeight, boardWidth);
      overlaps = overlapsShips(taken);
    }
  }

  /**
   * Determines if this ship overlaps any in the given list
   * of already placed ships.
   *
   * @param taken a list of ships already on the game board
   * @return true if the coordinates of this overlap any from taken
   */
  private boolean overlapsShips(ArrayList<Ship> taken) {
    for (Ship ship : taken) {
      for (Coord c : coords) {
        if (ship.overlapsCoord(c)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Helper method that given a random direction, a random position,
   * and game board dimensions, determines if this ship can fit.
   *
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @return if this ship can fit in the given orientation
   */
  private boolean canShipFit(int boardHeight, int boardWidth) {
    for (Coord c : coords) {
      if (c.getX() < 0 || c.getX() >= boardWidth
          || c.getY() < 0 || c.getY() >= boardHeight) {
        return false;
      }
    }
    return true;
  }

  /**
   * Given the starting position of this ship and the direction this ship
   * fits, updates the coordinate at the given index, and changes
   * its status to a ship.
   *
   * @param dir the direction this ship will face
   * @param x the x coordinate this ship starts at
   * @param y the y coordinate this ship starts at
   * @param index the index of the coordinate to be updated
   */
  private void updateCoords(int dir, int x, int y, int index) {
    if (Direction.UP.matches(dir)) {
      Coord coord = new Coord(x, y - index);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.set(index, coord);
    } else if (Direction.RIGHT.matches(dir)) {
      Coord coord = new Coord(x + index, y);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.set(index, coord);
    } else if (Direction.DOWN.matches(dir)) {
      Coord coord = new Coord(x, y + index);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.set(index, coord);
    } else {
      Coord coord = new Coord(x - index, y);
      coord.updateStatus(CellStatus.UNTOUCHED_SHIP);
      coords.set(index, coord);
    }
  }

  /**
   * Determines if this ship has sunk.
   *
   * @return true if all cells in the ship have been hit
   */
  public boolean isSunk() {
    int sunkenCells = 0;
    for (Coord c : coords) {
      if (c.getStatus() == CellStatus.HIT_SHIP) {
        sunkenCells += 1;
      }
    }
    return sunkenCells == coords.size();
  }
}
