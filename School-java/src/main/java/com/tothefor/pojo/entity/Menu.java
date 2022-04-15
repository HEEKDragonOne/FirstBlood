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
import java.util.List;

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
@TableName("s_menu")
public class Menu implements Serializable {
    /**
     * 唯一标识符
     */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    /**
     * 菜单编号
     */
    @TableField("menu_id")
    private Long menuId;
    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;
    //菜单路径
    @TableField("menu_path")
    private String menuPath;
    //菜单图标
    @TableField("menu_ico")
    private String menuIco;

    @TableField(exist = false)
    private List<Menu> children;

    /**
     * 父级id
     */
    @TableField("pid")
    private Long pid;
}

