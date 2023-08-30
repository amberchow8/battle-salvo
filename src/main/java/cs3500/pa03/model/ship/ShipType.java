package cs3500.pa03.model.ship;

/**
 * Represents the different sized ships in BattleSalvo.
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  private final int length;

  /**
   * Creates a ShipType with a given length.
   *
   * @param length the number of cells this shipType occupies
   */
  ShipType(int length) {
    this.length = length;
  }

  /**
   * Getter method to return this value.
   *
   * @return the int value of this
   */
  public int getLength() {
    return this.length;
  }
}

