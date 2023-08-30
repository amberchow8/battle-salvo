package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.player.FakePlayer;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import cs3500.pa04.records.CoordJson;
import cs3500.pa04.records.EndGameJson;
import cs3500.pa04.records.FleetRequestJson;
import cs3500.pa04.records.MessageJson;
import cs3500.pa04.records.SetupRequestJson;
import cs3500.pa04.records.VolleyJson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests that the controller responds properly to different requests from
 * the server using a MockServer.
 */
class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController controller;
  private final ObjectMapper mapper = new ObjectMapper();
  private FakePlayer player;
  private OpponentBoard board;


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    // resets the log
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", testLog.toString());

    // resets the board to a 6x6 with one of each shiptype
    // and creates the player using these ships
    List<Ship> ships = List.of(new Ship(ShipType.BATTLESHIP),
        new Ship(ShipType.CARRIER), new Ship(ShipType.DESTROYER),
        new Ship(ShipType.SUBMARINE));
    board = new OpponentBoard(ships, 6, 6);
    player = new FakePlayer(new ArrayList<>(), ships, board);
  }

  /**
   * Tests that the controller returns a volley when given a setup json.
   */
  @Test
  public void testHandleTakeShots() {
    // prepare sample message received from the server
    JsonNode sampleMessage = createSampleEmptyMessage("take-shots");

    // create the client with all necessary messages
    MockSocket socket = new MockSocket(this.testLog, List.of(sampleMessage.toString()));

    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }

    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Tests that the controller returns a fleet when prompted with
   * the method-name "setup".
   */
  @Test
  public void testHandleSetup() {
    // prepare sample message received from the server
    FleetRequestJson fleetRequest = new FleetRequestJson(1, 1, 1, 1);
    SetupRequestJson setupRequest = new SetupRequestJson(6, 6, fleetRequest);
    JsonNode sampleMessage = createSampleMessage("setup", setupRequest);

    // create the client with the necessary messages
    MockSocket socket = new MockSocket(this.testLog, List.of(sampleMessage.toString()));

    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }

    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Tests that handleReportDamage returns a MessageJson
   */
  @Test
  public void testHandleReportDamage() {
    // prepare sample message for setup
    FleetRequestJson fleetRequest = new FleetRequestJson(1, 1, 1, 1);
    SetupRequestJson setupRequest = new SetupRequestJson(6, 6, fleetRequest);
    JsonNode sampleSetup = createSampleMessage("setup", setupRequest);
    // create sample volley for report-damage
    VolleyJson volleyRequest = new VolleyJson(
        new ArrayList<>(List.of(
            new CoordJson(0, 0),
            new CoordJson(0, 1))));
    JsonNode sampleMessage = createSampleMessage(
        "report-damage", volleyRequest);
    // create the client with the necessary messages
    MockSocket socket = new MockSocket(this.testLog,
        List.of(sampleSetup.toString(), sampleMessage.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Tests that handleJoin returns a MessageJson
   */
  @Test
  public void testHandleJoin() {
    // create sample message for join
    JsonNode sampleMessage = createSampleEmptyMessage("join");
    // create the client with the necessary messages
    MockSocket socket = new MockSocket(this.testLog,
        List.of(sampleMessage.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Tests that handleSuccessfulHits returns a MessageJson
   */
  @Test
  public void testHandleSuccessfulHits() {
    // create sample message for successful-hits
    VolleyJson successfulHitsJson = new VolleyJson(
        new ArrayList<>(List.of(
            new CoordJson(0, 3),
            new CoordJson(1, 1))));
    JsonNode sampleMessage = createSampleMessage(
        "successful-hits", successfulHitsJson);
    // create the client with the necessary messages
    MockSocket socket = new MockSocket(this.testLog,
        List.of(sampleMessage.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Tests that handleEndGame returns a MessageJson
   */
  @Test
  public void testHandleEndGame() {
    // create sample message for end-game
    EndGameJson endGameJson = new EndGameJson(GameResult.WIN, "Win");
    JsonNode sampleMessage = createSampleMessage("end-game", endGameJson);
    // create the client with the necessary messages
    MockSocket socket = new MockSocket(this.testLog,
        List.of(sampleMessage.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);

    // create sample message for end-game
    EndGameJson endGameJson2 = new EndGameJson(GameResult.LOSE, "Lose");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson2);
    // create the client with the necessary messages
    MockSocket socket2 = new MockSocket(this.testLog,
        List.of(sampleMessage2.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket2, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);

    // create sample message for end-game
    EndGameJson endGameJson3 = new EndGameJson(GameResult.DRAW, "Draw");
    JsonNode sampleMessage3 = createSampleMessage("end-game", endGameJson3);
    // create the client with the necessary messages
    MockSocket socket3 = new MockSocket(this.testLog,
        List.of(sampleMessage3.toString()));
    // create a controller
    try {
      this.controller = new ProxyController(socket3, player);
    } catch (IOException e) {
      fail(); // if the controller can't be created
    }
    // run the controller and verify the response
    this.controller.run();
    responseToClass(MessageJson.class);
  }

  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   * @param <T>      Type to try converting the current test stream to.
   */
  private <T> void responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(testLog.toString());
      jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a class of the type provided, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName   name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }

  /**
   * Create a MessageJson with no arguments for some name.
   *
   * @param messageName the name of the type of message
   * @return a MessageJson for the object
   */
  private JsonNode createSampleEmptyMessage(String messageName) {
    MessageJson messageJson = new MessageJson(messageName, mapper.createObjectNode());
    return JsonUtils.serializeRecord(messageJson);
  }

}