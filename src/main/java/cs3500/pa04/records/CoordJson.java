package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents JSON format for a Coord
 *
 * @param coordX the x position
 * @param coordY the y position
 */
public record CoordJson(
    @JsonProperty ("x") int coordX,
    @JsonProperty ("y") int coordY) {
}
