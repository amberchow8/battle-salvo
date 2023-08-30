package cs3500.pa04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

/**
 * Mock a Socket to simulate behaviors of ProxyControllers being connected to a server.
 */
public class MockSocket extends Socket {

  private final InputStream testInputs;
  private final ByteArrayOutputStream testLog;

  /**
   * @param testLog what the server has received from the client
   * @param toSend what the server will send to the client
   */
  public MockSocket(ByteArrayOutputStream testLog, List<String> toSend) {
    this.testLog = testLog;

    // Set up the list of inputs as separate messages of JSON for the ProxyController to handle
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    for (String message : toSend) {
      // writes the messages of toSend to the stringWriter
      printWriter.println(message);
    }
    // stores the contents of toSend to this testInputs
    this.testInputs = new ByteArrayInputStream(stringWriter.toString().getBytes());
  }

  /**
   * Returns the input of this, to test what the server will
   * send to the client.
   *
   * @return an inputstream of messages to send
   */
  @Override
  public InputStream getInputStream() {
    return this.testInputs;
  }

  /**
   * Returns the log os this, to test what the server has
   * received from the client.
   *
   * @return an outputstream of the log received
   */
  @Override
  public OutputStream getOutputStream() {
    return this.testLog;
  }
}

