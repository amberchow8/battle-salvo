package cs3500.pa03.model.player;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents any BattleSalvo player.
 */
public abstract class BattleSalvoPlayer implements Player {
  protected List<Coord> shotAtOpponent;
  protected List<Ship> myShips;

  /**
   * Creates a new BattleSalvo player given default empty parameters.
   *
   * @param c the shots taken so far
   * @param s the ships placed
   */
  public BattleSalvoPlayer(List<Coord> c, List<Ship> s) {
    shotAtOpponent = c;
    myShips = s;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public abstract String name();

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    ArrayList<Ship> ships = new ArrayList<>();

    // for every entry in the given map
    for (Map.Entry<ShipType, Integer> entry : specifications.entrySet()) {
      ShipType type = entry.getKey();
      Integer amount = entry.getValue();

      for (int i = 0; i < amount; i++) {
        // create a ship that fits in the given board and doesn't overlap other ships
        Ship currentShip = new Ship(type);
        currentShip.setupCoords(height, width, ships);
        ships.add(currentShip);
      }
    }
    return ships;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> openShips = new ArrayList<>();

    // create list of all ship coordinates to compare
    ArrayList<Coord> allShipCoords = new ArrayList<>();
    for (Ship s : myShips) {
      for (Coord c : s.getCells()) {
        allShipCoords.add(c);
      }
    }

    // add the coordinates that overlap untouched ships
    for (Coord myCoord : allShipCoords) {
      for (Coord opponentC : opponentShotsOnBoard) {
        if (myCoord.overlaps(opponentC)) {
          openShips.add(opponentC);
          myCoord.updateStatus(CellStatus.HIT_SHIP);
        } else {
          myCoord.updateStatus(CellStatus.MISSED_SHIP);
        }
      }
    }
    return openShips;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    System.out.println(result.toString() + ": " + reason);
  }

}
