package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-07 16:18
 */

public enum EventType {
    @JsonProperty("message")
    MESSAGE,
    @JsonProperty("notice")
    NOTICE,
    @JsonProperty("request")
    REQUEST,
    @JsonProperty("meta_event")
    META_EVENT,
    NONE
}
