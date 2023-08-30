package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents fleet specifications requested by the
 * server during game setup.
 *
 * @param carriers the amount of carriers to place
 * @param battleships the amount of battleships to place
 * @param destroyers the amount of destroyers to place
 * @param submarines the amount of submarines to place
 */
public record FleetRequestJson(
    @JsonProperty("CARRIER") int carriers,
    @JsonProperty("BATTLESHIP") int battleships,
    @JsonProperty("DESTROYER") int destroyers,
    @JsonProperty("SUBMARINE") int submarines) {
}
