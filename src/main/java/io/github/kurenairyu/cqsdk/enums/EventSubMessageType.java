package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-08 9:14
 */

public enum EventSubMessageType {

    @JsonProperty("friend")
    FRIEND,
    @JsonProperty("group")
    GROUP,
    @JsonProperty("other")
    OTHER,
    @JsonProperty("normal")
    NORMAL,
    @JsonProperty("notice")
    NOTICE,
    @JsonProperty("anonymous")
    ANONYMOUS,
    @JsonProperty("connect")
    CONNECT,
    NONE,

}
