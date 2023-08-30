package cs3500.pa03.model.player;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.ship.Ship;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an ai BattleSalvo player with automated shot firing.
 */
public class FakePlayer extends BattleSalvoPlayer {
  private OpponentBoard board;
  private List<Coord> shotAtOpponent;
  protected List<Coord> successfulShots;
  private List<Coord> availableShots;
  private List<Coord> filledPositions;
  private List<Coord> hits;
  private List<Ship> myShips;
  protected int shipsLeft;
  private Random rand;

  /**
   * Creates a new BattleSalvo player given default empty parameters.
   *
   * @param c the shots taken so far
   * @param s the ships placed
   * @param b the board of this player
   */
  public FakePlayer(List<Coord> c, List<Ship> s, OpponentBoard b) {
    super(c, s);
    rand = new Random();
    board = b;
    myShips = s;
    shotAtOpponent = new ArrayList<>();
    successfulShots = new ArrayList<>();
    hits = new ArrayList<>();
    availableShots = new ArrayList<>();
    filledPositions = new ArrayList<>();
    for (Ship ship : myShips) {
      filledPositions.addAll(ship.getCells());
    }
    for (int i = 0; i < board.getBoard().length; i++) {
      for (int j = 0; j < board.getBoard()[0].length; j++) {
        availableShots.add(new Coord(j, i));
      }
    }
    shipsLeft = myShips.size();
  }

  /**
   * Returns the name of this player.
   *
   * @return a string of this name
   */
  @Override
  public String name() {
    return "amber-lauren";
  }

  /**
   * Returns this player's shots on the opponent's board
   * by randomly shooting at the board.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    int numShots;
    if (shipsLeft < availableShots.size()) {
      numShots = shipsLeft;
    } else {
      numShots = availableShots.size();
    }

    ArrayList<Coord> shots = new ArrayList<>();
    int x;
    int y;
    Coord shot;
    if (this.shotAtOpponent.size() == 0) {
      x = 0;
      y = 0;
      for (int i = 0; i < this.shipsLeft; i++) {
        shot = new Coord(x, y);
        shots.add(shot);
        x++;
        y++;
      }
    } else {
      for (int i = 0; i < numShots; i++) {
        shots.add(calculateShot(shots));
      }
    }
    this.shotAtOpponent.addAll(shots);
    this.availableShots.removeAll(shots);
    return shots;
  }

  /**
   * Calculates a shot based on previous successful shots
   *
   * @return a shot
   */
  private Coord calculateShot(ArrayList<Coord> shots) {
    Coord shot;
    if (this.successfulShots.size() != 0) {
      shot = successfulShots.get(rand.nextInt(0, successfulShots.size()));
      while (this.shotAtOpponent.contains(shot) || shots.contains(shot)) {
        shot = nextInBoundShot(shot);
      }
    } else {
      shot = nextAvailable();
      while (this.shotAtOpponent.contains(shot) || shots.contains(shot)) {
        shot = nextAvailable();
      }
    }
    return shot;
  }

  /**
   * Returns the next random available shot
   *
   * @return next available shot
   */
  private Coord nextAvailable() {
    Coord nextShot;
    if (this.availableShots.size() != 0) {
      nextShot = this.availableShots.get(
          rand.nextInt(0, this.availableShots.size()));
    } else {
      nextShot = new Coord(rand.nextInt(0, board.getBoard().length),
          rand.nextInt(0, board.getBoard()[0].length));
    }
    return nextShot;
  }

  /**
   * Returns the next in bound shot in relation
   * to the given shot
   *
   * @param shot shot to calculate next shot
   * @return next in bound shot
   */
  private Coord nextInBoundShot(Coord shot) {
    Coord nextShot;
    if (shot.getX() + 1 < board.getBoard()[0].length) {
      nextShot = new Coord(shot.getX() + 1, shot.getY());
    } else if (shot.getY() + 1 < board.getBoard().length) {
      nextShot = new Coord(shot.getX(), shot.getY() + 1);
    } else {
      if (this.availableShots.size() != 0) {
        nextShot = this.availableShots.get(
            rand.nextInt(0, this.availableShots.size()));
      } else {
        nextShot = new Coord(rand.nextInt(0, board.getBoard().length),
            rand.nextInt(0, board.getBoard()[0].length));
      }
    }
    return nextShot;
  }

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
    ArrayList<Coord> hit = new ArrayList<>();
    ArrayList<Coord> toRemoveCoord = new ArrayList<>();
    for (Coord c : opponentShotsOnBoard) {
      if (this.filledPositions.contains(c)) {
        hit.add(c);
        hits.add(c);
        c.updateStatus(CellStatus.HIT_SHIP);
        toRemoveCoord.add(c);
      } else {
        c.updateStatus(CellStatus.MISSED_SHIP);
      }
    }
    this.filledPositions.removeAll(toRemoveCoord);

    ArrayList<Ship> toRemove = new ArrayList<>();
    for (Ship s : myShips) {
      if (this.hits.containsAll(s.getCells())) {
        this.shipsLeft--;
        toRemove.add(s);
      }
    }
    this.myShips.removeAll(toRemove);

    return hit;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    this.successfulShots.addAll(shotsThatHitOpponentShips);
  }
}
