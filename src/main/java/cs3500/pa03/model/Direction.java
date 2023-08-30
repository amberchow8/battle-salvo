package cs3500.pa03.model;

/**
 * Represents a direction that a BattleSalvo ship can face.
 */
public enum Direction {
  UP(0),
  RIGHT(1),
  DOWN(2),
  LEFT(3);

  private final int value;

  /**
   * Creates a Direction based on the number given
   *
   * @param num the number associated with this direction
   */
  Direction(int num) {
    this.value = num;
  }

  /**
   * Checks if the given int matches this value
   *
   * @return a boolean representing if num matches this value
   */
  public boolean matches(int num) {
    return num == this.value;
  }
}
