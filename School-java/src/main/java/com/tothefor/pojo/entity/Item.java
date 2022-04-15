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
@TableName("s_item")
public class Item implements Serializable {
    private static final long serialVersionUID = 373498731959536545L;
    /**
     * 物品id
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 物品编号
     */
    @TableField("item_id")
    private String itemId;
    /**
     * 物品名
     */
    @TableField("name")
    private String name;
    /**
     * 物品类型编号
     */
    @TableField("typeID")
    private Long typeid;
    /**
     * 物品名称
     */
    @TableField(exist = false)
    private String typeName;
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
     * 物品入库时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "item_add_time",fill = FieldFill.INSERT) //在插入时填充
    private Date itemAddTime;
    /**
     * 最后修改时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "item_update_time",fill = FieldFill.INSERT_UPDATE) //在插入和更新时填充
    private Date itemUpdateTime;
    /**
     * 物品规格
     */
    @TableField("size")
    private String size;
    /**
     * 物品描述或备注
     */
    @TableField("description")
    private String description;
    /**
     * 物品数量
     */
    @TableField("item_count")
    private Long itemCount;


}

