
![](https://img-blog.csdnimg.cn/e96c7ae561684f83a611631d1b24ca8f.png)

# FirstBlood
一个基于SpringBoot+Vue3实现的前后端分离后台管理系统。在学习的过程中让自己也成长了很多，踩了很多坑但也收获到了很多。

## 设计知识点

前端：Vue3、Element-Plus、Echarts、Axios、Vue-Router

后端：SpringBoot、MyBatis-Plus

JDK：1.8

MySQL：8.0

其他相关：Hutool工具、JWT等。


## 注意事项

- 管理员创建用户时，用户的默认密码为123456。
- 添加物品时（手动SQL添加），物品类型的编号必须是类型表中已有的类型的编号。通过界面添加则只需要选择即可。
- 关于登录时报错空指针问题：原因在于插入数据时，每一条的数据的id字段不是从0开始的，而是从某一个数开始的。所以我们需要自己手动修改两张表的id字段。一个是s_menu表，将其id字段改为从1开始。第二个是s_user_role表，需要将其id改为role_id，保证id和role_id保持一致即可；修改id和role_id时还需要注意对照role表中的数据。

s_menu表：id字段从1开始排，每一条数据的pid就是其他数据的id。所以修改时需要对应修改。 <br>
s_user_role表：修改id和role_id一致。但修改role_id的话还需要修改s_role_menu表中的role_id。所以将初始数据的id改为role_id即可。


## 功能截图

### 物品管理

![在这里插入图片描述](https://img-blog.csdnimg.cn/c61429c688574ed8983b8066712441d2.png)




### 物品类型

![在这里插入图片描述](https://img-blog.csdnimg.cn/c1d6cb8b177d4008a32dbe34ce3754f6.png)




### 借出管理

![在这里插入图片描述](https://img-blog.csdnimg.cn/fdcc44cf98a044548403d7e5bcccec1b.png)




### 数据展示

将数据进行可视化展示。

![在这里插入图片描述](https://img-blog.csdnimg.cn/5ccfa9abf6c142e286467ec8cc2d6b3a.png)




![在这里插入图片描述](https://img-blog.csdnimg.cn/fcdc53b8b577481199c41f9f8c53f3fd.png)




### 角色管理

![在这里插入图片描述](https://img-blog.csdnimg.cn/43cd719eab9a4414a8207614c70a4923.png)


### 人员管理

![在这里插入图片描述](https://img-blog.csdnimg.cn/d1c874154a5445ccaad86aeae817e624.png)




### 密码修改

可以修改其他账号的密码。

![在这里插入图片描述](https://img-blog.csdnimg.cn/305296b8638547b7b55276ae275da87b.png)




### 个人密码

修改当前账号的密码。

![在这里插入图片描述](https://img-blog.csdnimg.cn/b4d822120b2943568d0ef930140fda15.png)




## 数据库表

用户表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/ffa0e1b19e51407cb47e5f5af3b2f581.png)


角色表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/5408000ed2194a60a354512c47b67dd0.png)




菜单表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/6186631213644d5594cc455276778513.png)


权限表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/c26a5a43d343482eb4328bf17506daa9.png)


物品信息表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/8311abd58cda4b079797be274432c273.png)


类型表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/983e5e498cf84f069eda82c61ef11699.png)


物品借出记录表：

![在这里插入图片描述](https://img-blog.csdnimg.cn/02d8994b1d484f03a130a9cbed5517e4.png)






## 其他效果展示

![在这里插入图片描述](https://img-blog.csdnimg.cn/d8a7051b01b64ec2acecb685d14c90ef.png)




![在这里插入图片描述](https://img-blog.csdnimg.cn/71250d4f00044d3fa39337e73333929d.png)



