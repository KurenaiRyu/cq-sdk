package io.github.kurenairyu.cqsdk.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kurenai
 * @since 2021-04-02 14:24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bot {

    private long   qq;
    private String nickname;
    private String xClientRole;

}
