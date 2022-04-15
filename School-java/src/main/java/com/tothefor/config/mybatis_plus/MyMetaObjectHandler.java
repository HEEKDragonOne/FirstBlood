package com.tothefor.config.mybatis_plus;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tothefor.utils.MD5Utils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 数据库填充策略
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */


@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时的策略
    @Override
    public void insertFill(MetaObject metaObject) {
        //setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        this.setFieldValByName("itemAddTime",new Date(),metaObject);
        this.setFieldValByName("itemUpdateTime",new Date(),metaObject);

        this.setFieldValByName("recordAddTime",new Date(),metaObject);
        this.setFieldValByName("recordUpdateTime",new Date(),metaObject);
        this.setFieldValByName("password", MD5Utils.getMD5("123456"),metaObject); //设置默认密码

        this.setFieldValByName("getCode", RandomUtil.randomString(6),metaObject);


    }

    //更新时的策略
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("itemUpdateTime",new Date(),metaObject);
        this.setFieldValByName("recordUpdateTime",new Date(),metaObject);
    }
}
