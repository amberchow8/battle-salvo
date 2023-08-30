package cs3500.pa04;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Coord;
import cs3500.pa04.records.CoordJson;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple utils class used to hold static methods that help with serializing and deserializing JSON.
 */
public class JsonUtils {
  /**
   * Converts a given record object to a JsonNode.
   *
   * @param record the record to convert
   * @return the JsonNode representation of the given record
   * @throws IllegalArgumentException if the record could not be converted correctly
   */
  public static JsonNode serializeRecord(Record record) throws IllegalArgumentException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.convertValue(record, JsonNode.class);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Given record cannot be serialized");
    }
  }

  /**
   * Converts given list of CoordJson to list of Coord
   *
   * @param coordJsonList list to convert
   * @return list of Coord
   */
  public static List<Coord> coordJsonToCoord(List<CoordJson> coordJsonList) {
    List<Coord> coordList = new ArrayList<>();
    for (CoordJson coordJson : coordJsonList) {
      Coord coord = new Coord(coordJson.coordX(), coordJson.coordY());
      coordList.add(coord);
    }
    return coordList;
  }
}
