package com.haust.user.mq.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
public class UserMsg implements Serializable {

    public String account;
    public String ip;
    public Long loginTimes ;
    public LocalDateTime createTime ;
}
