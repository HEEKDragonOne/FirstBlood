package com.tothefor.pojo.dto;

import com.tothefor.utils.RequestInfoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author DragonOne
 * @Date 2022/3/13 19:51
 * @墨水记忆 www.tothefor.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class RequestReturnInfo {
    /**
     * ip
     */
    private String ip;
    /**
     *浏览器名称
     */
    private String browserName;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 操作系统
     */
    private String osName ;

}
