package io.github.kurenairyu.cqsdk.pojo.result;

import lombok.Data;
import org.kurenai.qqbot.constant.ResponseStatus;

/**
 * @author Kurenai
 * @since 2021-04-09 15:30
 */
@Data
public class ActionResult {

    private Data           data;
    private String         echo;
    private int            retcode;
    private ResponseStatus status;

    @lombok.Data
    public static class Data {
        private int messageId;
    }

}
