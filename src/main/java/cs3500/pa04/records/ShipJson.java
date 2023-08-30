package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents JSON format for a ship
 *
 * @param startingCoord the json representation of the coord this ship begins on
 * @param length the length of the ship
 * @param dir the string representation of the direction this ship lies in
 */
public record ShipJson(
    @JsonProperty ("coord") CoordJson startingCoord,
    @JsonProperty ("length") int length,
    @JsonProperty ("direction") String dir) {
}
