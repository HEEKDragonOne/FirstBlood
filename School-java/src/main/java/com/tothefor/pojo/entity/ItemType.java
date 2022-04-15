package com.tothefor.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.io.Serializable;

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
@TableName("s_item_type")
public class ItemType implements Serializable {
    private static final long serialVersionUID = 373498731959536545L;
    /**
     * 类型唯一标识符
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 类型编号
     */
    @TableField("type_id")
    private Long typeId;
    /**
     * 类型名称
     */
    @TableField("typename")
    private String typename;
    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("is_delete")
    private Boolean isDelete;

    /**
     * 类型描述
     */
    @TableField("description")
    private String description;


}

