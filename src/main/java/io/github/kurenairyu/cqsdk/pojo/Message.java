package io.github.kurenairyu.cqsdk.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kurenairyu.cqsdk.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kurenai
 * @since 2021-04-08 9:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {

    private static final long        serialVersionUID = -4345339430511834838L;
    private              MessageType type;
    private              Data        data;

    @lombok.Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        String        file;
        String        text;
        Integer       id;
        Type          type;
        String        url;
        Boolean       cache;
        Boolean       proxy;
        Long          timeout;
        Boolean       magic;
        String        qq;
        Float         lat;
        Float         lon;
        String        title;
        //TODO: 和位置冲突
        List<Message> content;
        String        userId;
        String        nickname;
        String        data;
    }

    public enum Type {
        @JsonProperty("flash")
        FLASH,
        @JsonProperty("qq")
        QQ,
        @JsonProperty("group")
        GROUP,
        @JsonProperty("163")
        NET_EASE_CLOUD,
        @JsonProperty("xm")
        XM,
        @JsonProperty("custom")
        CUSTOM,
    }

    public interface Constant {
        String ALL_QQ = "all";
    }
}
