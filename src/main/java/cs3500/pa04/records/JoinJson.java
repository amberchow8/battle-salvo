package cs3500.pa04.records;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents JSON format for join response
 *
 * @param name the name of the player
 * @param gameType the game mode the player is participating in
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") String gameType) {

}
