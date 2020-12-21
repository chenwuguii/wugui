package com.example.wugui.word.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 家庭成员
 *
 * @author czy
 * @date 2020/12/21 16:05
 */
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpRewandpun {
    private String urewdate;
    private String urewunit;
    private String urewdesc;
}