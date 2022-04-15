package com.tothefor.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author DragonOne
 * @Date 2022/3/13 17:06
 * @墨水记忆 www.tothefor.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PieData {
    private Integer value;
    private String name;
}
