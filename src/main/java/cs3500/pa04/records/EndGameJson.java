package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.GameResult;

/**
 * Represents server message args for end-game
 *
 * @param result Game Result
 * @param reason Reason for end game
 */
public record EndGameJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {
}
