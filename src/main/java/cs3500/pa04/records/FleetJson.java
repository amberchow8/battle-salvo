package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents JSON argument for a fleet composed of JSON ships
 *
 * @param fleet fleet of JSON ships
 */
public record FleetJson(
    @JsonProperty ("fleet") List<ShipJson> fleet
) {
}
