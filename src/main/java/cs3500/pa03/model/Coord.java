package cs3500.pa03.model;

/**
 * Represents one cell of a BattleSalvo board.
 */
public class Coord {
  private int coordX;
  private int coordY;
  private CellStatus status;

  /**
   * Instantiates a coord.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Coord(int x, int y) {
    this.coordX = x;
    this.coordY = y;
    this.status = CellStatus.NOTHING;
  }

  /**
   * Getter method to return this x coordinate.
   *
   * @return this x
   */
  public int getX() {
    return coordX;
  }

  /**
   * Getter method to return this y coordinate.
   *
   * @return this y
   */
  public int getY() {
    return coordY;
  }

  /**
   * Getter method to return this status.
   *
   * @return this status
   */
  public CellStatus getStatus() {
    return status;
  }

  /**
   * Changes the status of this coordinate to the given.
   *
   * @param s the status to change this to
   */
  public void updateStatus(CellStatus s) {
    status = s;
  }

  /**
   * Overrides built in equals method
   * Checks if x and y of this Coord is the same as the given object
   *
   * @param other the object to check for equality
   * @return if the given object is the same Coord as this
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Coord)) {
      return false;
    } else {
      return this.coordX == ((Coord) other).getX()
          && this.coordY == ((Coord) other).getY();
    }
  }

  /**
   * Determines if this coordinate has the same location as the given.
   *
   * @param c the coodinate to compare this to
   * @return whether or not they overlap
   */
  public boolean overlaps(Coord c) {
    return (this.coordX == c.coordX) && (this.coordY == c.coordY);
  }

  /**
   * Returns the string representation of this cell's status
   * on an opponent board.
   *
   * @return a string of this cell's status
   */
  public String printOpponentStatus() {
    if (status == CellStatus.UNTOUCHED_SHIP) {
      return "-";
    } else if (status == CellStatus.NOTHING) {
      return "-";
    } else if (status == CellStatus.MISSED_SHIP) {
      return "*";
    } else {
      return "S";
    }
  }

  /**
   * Returns the string representation of this cell's status
   * on an opponent board.
   *
   * @return a string of this cell's status
   */
  public String printUserStatus() {
    if (status == CellStatus.UNTOUCHED_SHIP) {
      return "S";
    } else if (status == CellStatus.NOTHING) {
      return "-";
    } else if (status == CellStatus.MISSED_SHIP) {
      return "*";
    } else {
      return "H";
    }
  }
}
