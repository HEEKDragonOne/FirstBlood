package com.tothefor.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tothefor.pojo.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Author DragonOne
 * @Date 2022/3/12 19:59
 * @墨水记忆 www.tothefor.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO implements Serializable {
    private String username;
//    @JsonIgnore
    private String password;
    private String userToken;
    private List<Menu> menus;
    private String roleName;
    private String RULpath;
}
