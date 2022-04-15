package com.tothefor.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@TableName("s_user")
public class User implements Serializable {
    private static final long serialVersionUID = -37411317986856736L;
    /**
     * 用户唯一标识符
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 用户编号
     */
    @TableField("user_ID")
    private String userid;
    /**
     * 用户名称
     */
    @TableField("username")
    private String username;
    /**
     * 用户密码
     */
    @TableField(value = "password",fill = FieldFill.INSERT)
    @JsonIgnore
    private String password;
    /**
     * 用户角色ID
     */
    @TableField("role_ID")
    private Long roleId;
    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;
    /**
     * 用户描述
     */
    @TableField("description")
    private String description;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_delete")
    private Boolean isDelete;

}

