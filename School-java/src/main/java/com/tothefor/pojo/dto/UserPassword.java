package com.tothefor.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author DragonOne
 * @Date 2022/3/13 20:17
 * @墨水记忆 www.tothefor.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPassword {

    private Long userid;
    private String oldP;
    private String newP;
    private String newPP;
}
