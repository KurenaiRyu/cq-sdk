package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-08 9:08
 */

public enum EventMessageType {

    @JsonProperty("private")
    PRIVATE,
    @JsonProperty("group")
    GROUP,
    NONE, @JsonProperty("meta_event")
    META_EVENT
}
