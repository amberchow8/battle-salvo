package cs3500.pa03.model;

/**
 * Represents the possible statuses of a coordinate in
 * a BattleSalvo board.
 */
public enum CellStatus {
  NOTHING,
  UNTOUCHED_SHIP,
  HIT_SHIP,
  MISSED_SHIP;
}
