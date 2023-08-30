package cs3500.pa04;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents an adapter used to convert a normal Ship to a ShipJson.
 */
public class ShipAdapter {
  private Coord startingCoord;
  private int length;
  private String direction;

  /**
   * Creates an adaptor for a ship by calculating and storing the fields
   * needed to create a shipJson.
   *
   * @param myShip the Ship that this will represent and adapt
   */
  public ShipAdapter(Ship myShip) {
    this.length = myShip.getCells().size();
    setDirection(myShip);
    setStartingCoord(myShip);
  }

  /**
   * Finds the starting coord of this ship and sets it as a field.
   */
  public void setStartingCoord(Ship ship) {
    Coord start = ship.getCells().get(0);

    for (int i = 1; i < ship.getCells().size(); i++) {
      Coord currentCoord = ship.getCells().get(i);

      if (this.direction.equals("VERTICAL")) {
        // if the ship is vertical, store the starting coord as the one
        // with the lowest y position (highest on the board)
        if (currentCoord.getY() < start.getY()) {
          start = currentCoord;
        }
      }

      if (this.direction.equals("HORIZONTAL")) {
        // if this ship is horizontal, store the starting coord as the one with
        // the lowest x position (leftmost on the board)
        if (currentCoord.getX() < start.getX()) {
          start = currentCoord;
        }
      }
    }

    this.startingCoord = start;
  }

  /**
   * Returns the starting coord of this ship.
   *
   * @return the starting coord
   */
  public Coord getStartingCoord() {
    return startingCoord;
  }

  /**
   * Calculates the length of this ship
   *
   * @return the length
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Returns the drection this ship is facing.
   *
   * @return String representation of this direction
   */
  public String getDirection() {
    return this.direction;
  }

  /**
   * Determines the direction this ship is facing.
   */
  private void setDirection(Ship ship) {
    ArrayList<Integer> listX = new ArrayList<>();
    ArrayList<Integer> listY = new ArrayList<>();
    for (Coord c : ship.getCells()) {
      listX.add(c.getX());
      listY.add(c.getY());
    }
    if (Collections.frequency(listX, listX.get(0)) == listX.size()) {
      this.direction = "VERTICAL";
    } else if (Collections.frequency(listY, listY.get(0)) == listY.size()) {
      this.direction = "HORIZONTAL";
    }
  }
}
