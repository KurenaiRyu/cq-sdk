package io.github.kurenairyu.cqsdk.pojo.result;

import lombok.Data;
import org.kurenai.qqbot.constant.ResponseStatus;

/**
 * @author Kurenai
 * @since 2021-04-21 17:52
 */

@Data
public class BaseResult {

    private String         echo;
    private int            retcode;
    private ResponseStatus status;

}
