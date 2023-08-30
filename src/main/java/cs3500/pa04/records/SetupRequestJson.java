package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents arguments for a server request during game setup.
 *
 * @param width the width of the board
 * @param height the height of the board
 * @param fleetSpecifications a FleetRequestJson of specifications regarding
 *                            ship types and amounts
 */
public record SetupRequestJson(
    @JsonProperty("height") int height,
    @JsonProperty("width") int width,
    @JsonProperty("fleet-spec") FleetRequestJson fleetSpecifications
) {
}
