package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * Tests methods in the class Driver.
 */
class DriverTest {
  /**
   * Tests that the right error is handled when the given port is invalid.
   */
  @Test
  public void testBadPortInput() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    String host = "sampleHost";
    String port = "s";
    String[] args = {host, port};

    Driver.main(args);

    // Assert
    // Add your assertions here based on the expected output
    // For example, if you expect a successful connection message, you can use:
    assertTrue(outputStream.toString().contains(
        "Second argument should be an integer. Format: `[host] [part]`."
    ));
    // Reset System.out
    System.setOut(System.out);
  }

  /**
   * Tests that there are no exceptions when running a valid host and port.
   */
  @Test
  public void testGoodServer() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    String host = "0.0.0.0";
    String port = "35001";
    String[] args = {host, port};

    Driver.main(args);
    assertDoesNotThrow(() -> Driver.main(args));

    // Assert
    // Add your assertions here based on the expected output
    // For example, if you expect a successful connection message, you can use:
    assertTrue(outputStream.toString().contains(
        "Unable to connect to the server."));

    // Reset System.out
    System.setOut(System.out);
  }

  @Test
  public void testManualPlay() {
    // Prepare the input stream
    String input = "6 6\n1 1 1 1\n0 0\n0 1\n0 2\n0 3\n0 4\n0 5\n1 1\n1 2\n1 3\n1 4\n1 5\n1 0\n"
        + "2 1\n2 2\n2 3\n2 4\n2 5\n2 0\n3 1\n3 2\n3 3\n3 4\n3 5\n3 0\n4 1\n4 2\n4 3\n4 5\n4 0\n"
        + "4 4\n5 1\n5 2\n5 3\n5 4\n5 5\n5 0\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    // Prepare the output stream
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    String[] args = new String[0];
    Driver.main(args);

    // Assert the expected output
    String expected = outputStream.toString().substring(0, 30);
    String expectedOutput = "Welcome to BattleSalvo!\nPlease";
    assertEquals(expectedOutput, expected);
  }

}