package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-08 9:27
 */

public enum Sex {
    @JsonProperty("male")
    MALE,
    @JsonProperty("female")
    FEMALE,
    @JsonProperty("unknown")
    UNKNOWN
}
