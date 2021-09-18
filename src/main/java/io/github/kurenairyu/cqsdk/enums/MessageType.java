package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-08 9:40
 */
public enum MessageType {

    @JsonProperty("text")
    TEXT,
    @JsonProperty("image")
    IMAGE,
    @JsonProperty("face")
    FACE,
    @JsonProperty("record")
    RECORD,
    @JsonProperty("video")
    VIDEO,
    @JsonProperty("at")
    AT,
    @JsonProperty("rps")
    RPS,
    @JsonProperty("dice")
    DICE,
    @JsonProperty("shake")
    SHAKE,
    @JsonProperty("poke")
    POKE,
    @JsonProperty("anonymous")
    ANONYMOUS,
    @JsonProperty("share")
    SHARE,
    @JsonProperty("contact")
    CONTACT,
    @JsonProperty("location")
    LOCATION,
    @JsonProperty("music")
    MUSIC,
    @JsonProperty("reply")
    REPLY,
    @JsonProperty("forward")
    FORWARD,
    @JsonProperty("node")
    NODE,
    @JsonProperty("xml")
    XML,
    @JsonProperty("json")
    JSON, NONE,

}
