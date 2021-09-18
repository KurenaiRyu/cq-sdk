package io.github.kurenairyu.cqsdk.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Kurenai
 * @since 2021-04-08 12:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action implements Serializable {

    private static final long serialVersionUID = -5594911523779013008L;

    public static final  String QUICK_OPERATION     = ".handle_quick_operation";
    public static final  String SEND_PRIVATE_MSG    = "send_private_msg";
    public static final  String SEND_GROUP_MSG      = "send_group_msg";
    public static final  String SEND_MSG            = "send_msg";
    private static final String ASYNC_SUFFIX        = "_async";
    private static final String RATE_LIMITED_SUFFIX = "_rate_limited";

    private String action;
    private Param  params;
    private Event  context;
    private Param  operation;
    private String echo;

    public Action quickOp() {
        action = QUICK_OPERATION;
        return this;
    }

    public Action async() {
        if (action != null && !action.contains(ASYNC_SUFFIX)) action = action.concat(ASYNC_SUFFIX);
        return this;
    }

    public Action retaLimited() {
        if (action != null && !action.contains(RATE_LIMITED_SUFFIX)) action = action.concat(RATE_LIMITED_SUFFIX);
        return this;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Param {
        private Long                userId;
        private Long                groupId;
        private Collection<Message> message;
        private Boolean             autoEscape;
        private Boolean             noCache;

        private List<Message> reply;
        private Boolean       atSender;
        private Boolean       delete;
        private Boolean       kick;
        private Boolean       ban;
        private Boolean       banDuration;
    }

}
