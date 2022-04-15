package com.tothefor.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)// 使用链式方法
@TableName("s_record")
public class Record implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 记录单名称
     */
    @TableField("record_name")
    private String recordName;
    /**
     * 启用状态
     */
    @TableField("is_show")
    private Boolean isShow;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_delete")
    private Boolean isDelete;
    /**
     * 入库时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "record_add_time",fill = FieldFill.INSERT) //在插入时填充
    private Date recordAddTime;
    /**
     * 最后修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "record_update_time",fill = FieldFill.INSERT_UPDATE) //在插入和更新时填充
    private Date recordUpdateTime;
    /**
     * 物品描述或备注
     */
    @TableField("description")
    private String description;
    /**
     * 签到人
     */
    @TableField("record_man")
    private String recordMan;
    /**
     * 领取码
     */
    @TableField(value = "getCode",fill = FieldFill.INSERT)
    private String getCode;

}

