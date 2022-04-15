package com.tothefor.resultR;

import lombok.*;

import java.io.Serializable;


/**
 * 统一结果封装类
 * @Author DragonOne
 * @Date 2022/3/4 11:39
 * @墨水记忆 www.tothefor.com
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class R<T> implements Serializable {

    /**
     * 对应 RCode 状态码中的属性
     */
    private Boolean statusFlag;
    private Integer statusCode;
    private String statusMessage;

    /**
     * @Author DragonOne
     * @Date 2022/3/4 12:04
     * @墨水记忆 www.tothefor.com
     * @属性 timestamp
     * @作用 接口请求时间
     */
    private long timestamp;

    /**
     * @Author DragonOne
     * @Date 2022/3/4 11:59
     * @墨水记忆 www.tothefor.com
     * @属性 data
     * @作用 携带后台传入的数据
     */
    private T data;

    /**
     * @Author DragonOne
     * @Date 2022/3/4 13:04
     * @墨水记忆 www.tothefor.com
     * @方法 SUCCESS
     * @作用 成功，无数据
     * @参数说明
     * @return
     */
    public static <T> R<T> SUCCESS(){
        return SUCCESS(null);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/4 12:11
     * @墨水记忆 www.tothefor.com
     * @方法 SUCCESS
     * @作用 成功，有数据
     * @参数说明
     * @return
     */
    public static <T> R<T> SUCCESS(T data){
        return R.<T>builder().data(data)
                .statusFlag(RCode.SUCCESS.getStatusFlag())
                .statusCode(RCode.SUCCESS.getStatusCode())
                .statusMessage(RCode.SUCCESS.getStatusMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/4 12:38
     * @墨水记忆 www.tothefor.com
     * @方法 FAIL
     * @作用 失败，无数据
     * @参数说明
     * @return
     */
    public static <T extends Serializable> R<T> FAIL() {
        return FAIL(null);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/4 12:38
     * @墨水记忆 www.tothefor.com
     * @方法 FAIL
     * @作用 失败，有数据
     * @参数说明
     * @return
     */
    public static <T> R<T> FAIL(T data){
        return R.<T>builder().data(data)
                .statusFlag(RCode.FAIL.getStatusFlag())
                .statusCode(RCode.FAIL.getStatusCode())
                .statusMessage(RCode.FAIL.getStatusMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:19
     * @墨水记忆 www.tothefor.com
     * @方法 FAIL
     * @作用 更具不同的场景，自定义不同的状态
     * @参数说明 自定义错误编码，自定义错误提示信息，无数据
     * @return
     */
    public static <T> R<T> FAIL(Integer code,String msg){
        return FAIL(code,msg,null);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:19
     * @墨水记忆 www.tothefor.com
     * @方法 FAIL
     * @作用 更具不同的场景，自定义不同的状态
     * @参数说明 自定义错误编码，自定义错误提示信息，有数据
     * @return
     */
    public static <T> R<T> FAIL(Integer code,String msg,T data){
        return R.<T>builder().data(data)
                .statusFlag(RCode.FAIL.getStatusFlag())
                .statusCode(code)
                .statusMessage(msg)
                .timestamp(System.currentTimeMillis())
                .build();
    }


}
