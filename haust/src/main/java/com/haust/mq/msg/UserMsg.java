package com.haust.mq.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserMsg implements Serializable {
    public String account;
    public String ip;
    public LocalDateTime time ;
    public Long loginTimes ;
}
