package com.haust.user.mq.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class LikeMsg implements Serializable {
    Long id;
    Long likedTimes;
}
