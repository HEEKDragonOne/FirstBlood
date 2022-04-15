package com.tothefor.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author DragonOne
 * @Date 2022/3/12 17:28
 * @墨水记忆 www.tothefor.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)// 使用链式方法
@TableName("s_role_menu")
public class RoleMenu implements Serializable {
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
     * 菜单编号
     */
    @TableField("menu_id")
    private Long menuId;

}

