package io.github.kurenairyu.cqsdk.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Kurenai
 * @since 2021-04-08 9:08
 */

public enum EventNoticeType {

    @JsonProperty("group_recall")
    GROUP_RECALL,
    @JsonProperty("friend_add")
    FRIEND_ADD,
    @JsonProperty("group_ban")
    GROUP_BAN,
    @JsonProperty("group_increase")
    GROUP_INCREASE,
    @JsonProperty("group_decrease")
    GROUP_DECREASE,
    @JsonProperty("group_admin")
    GROUP_ADMIN,
    @JsonProperty("group_upload")
    GROUP_UPLOAD,
    @JsonProperty("friend_recall")
    FRIEND_RECALL,
    @JsonProperty("notify")
    NOTIFY, NONE,
}
