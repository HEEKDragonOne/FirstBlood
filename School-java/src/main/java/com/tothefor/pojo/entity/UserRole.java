package com.tothefor.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@TableName("s_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 883443446115747143L;
    /**
     * 唯一标识符
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 角色编号
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 角色名称
     */
    @TableField("rolename")
    private String rolename;
    /**
     * 逻辑删除
     */
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 角色描述
     */
    @TableField("description")
    private String description;


}

