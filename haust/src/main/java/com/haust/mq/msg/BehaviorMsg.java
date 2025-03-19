package com.haust.mq.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
public class BehaviorMsg  implements Serializable {
    Long userId;
    String behavior;
    String behaviorDetail;
    LocalDateTime createTime;
}
