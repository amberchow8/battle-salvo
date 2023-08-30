package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a list of coord shots for JSON argument
 *
 * @param volley the list of coordinates in this volley in json format
 */
public record VolleyJson(
    @JsonProperty ("coordinates") List<CoordJson> volley
) {
}
