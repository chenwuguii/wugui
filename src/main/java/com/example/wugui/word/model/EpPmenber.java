package com.example.wugui.word.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 奖惩情况
 *
 * @author czy
 * @date 2020/12/21 16:05
 */
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpPmenber {
    private String uconnection;
    private String uname;
    private String ubirthday;
    private String uworkunit;
    private String uploity;
    private String ustatus;
}
