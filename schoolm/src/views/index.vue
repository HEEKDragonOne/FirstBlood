<template>
  <el-container
      class="layout-container-demo"
      style="height: 100%; border: 1px solid #eee">
    <!--    rgb(238, 241, 246)-->
    <el-aside :width=" sideWidth +'px'" style="background-color: rgb(238, 241, 246);height: 100%" >
      <el-scrollbar style="background-color: rgb(48,65,86)">
        <!--       default-openeds 默认展开 -->
        <el-menu :default-openeds="['1']" style="height: 100%;"
                 :collapse="isCollapse"
                 :collapse-transition="false"
                 background-color="rgb(48,65,86)"
                 text-color="#fff"
                 :router="true"
                 active-text-color="yellow">

          <div style="height: 60px;line-height: 60px; text-align: center">
            <img src="../assets/tothefor.png" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
            <b style="color: white" v-show="logoTextShow">TeamC便捷管理系统</b>
          </div>

          <!--          主目录1-->
            <el-menu-item :index="'/home'">
              <el-icon><component :is="HomeFilled"></component></el-icon>
              <b v-show="logoTextShow">首页</b>
            </el-menu-item>

          <div v-for="item in menuData" :key="item.id+''">
              <el-sub-menu :index="item.id+''">
                <template #title>
                  <el-icon><component :is="item.menuIco"></component></el-icon>
                  <b v-show="logoTextShow">{{ item.menuName }}</b>
                </template>
                <div v-for="subItem in item.children" :key="subItem.id+ ''">
                  <el-menu-item :index="subItem.menuPath">
                    <el-icon><component :is="subItem.menuIco"></component></el-icon>
                    {{ subItem.menuName }}
                  </el-menu-item>
                </div>
              </el-sub-menu>
          </div>

<!--          &lt;!&ndash;          主目录2 &ndash;&gt;-->
<!--          <el-sub-menu index="2">-->
<!--            <template #title>-->
<!--              <el-icon><component :is="IconMenu"></component></el-icon>-->
<!--              <b v-show="logoTextShow">物品管理</b>-->
<!--            </template>-->
<!--            <el-menu-item :index="'/itemShow'">-->
<!--              <el-icon><component :is="List"></component></el-icon>物品列表-->
<!--            </el-menu-item>-->
<!--            <el-menu-item :index="'/itemType'">-->
<!--              <el-icon><component :is="Tickets"></component></el-icon>物品类型-->
<!--            </el-menu-item>-->
<!--          </el-sub-menu>-->

<!--          &lt;!&ndash;          主目录3 &ndash;&gt;-->
<!--          <el-sub-menu index="3">-->
<!--            <template #title>-->
<!--              <el-icon><component :is="Lock"></component></el-icon>-->
<!--              <b v-show="logoTextShow">权限设置</b>-->
<!--            </template>-->
<!--            <el-menu-item :index="'/role'">-->
<!--              <el-icon><component :is="User"></component></el-icon>角色管理-->
<!--            </el-menu-item>-->
<!--            <el-menu-item :index="'/mans'">-->
<!--              <el-icon><component :is="Avatar"></component></el-icon>人员管理-->
<!--            </el-menu-item>-->
<!--          </el-sub-menu>-->
<!--          -->
<!--          &lt;!&ndash;          主目录4 &ndash;&gt;-->
<!--          <el-sub-menu index="4">-->
<!--            <template #title>-->
<!--              <el-icon><component :is="Setting"></component></el-icon>-->
<!--              <b v-show="logoTextShow">系统设置</b>-->
<!--            </template>-->
<!--            <el-menu-item :index="'/pwd'">-->
<!--              <el-icon><component :is="Key"></component></el-icon>修改密码-->
<!--            </el-menu-item>-->
<!--            <el-menu-item :index="'/perPwd'">-->
<!--              <el-icon><component :is="User"></component></el-icon>个人密码-->
<!--            </el-menu-item>-->
<!--          </el-sub-menu>-->

        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px;border-bottom: 1px solid #ccc;" >

        <div style="flex: 1;font-size: 18px;" class="hiddennav">
          <el-icon> <component style="cursor: pointer;" :is="collapseBtnClass" @click="collepse"></component></el-icon>
        </div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" class="mbx">
<!--          :to="{path: item.path}-->
          <el-breadcrumb-item v-for="item in breadList" v-bind:key="item.path">
            {{ item.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>

        <div class="toolbar">
          <span>{{ localName }}</span>
          <el-dropdown style="margin-left: 5px">
            <el-icon style="margin-right: 8px; margin-top: 2px"><setting/></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="userexit">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <el-scrollbar>
          <router-view />
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import {Menu as IconMenu} from '@element-plus/icons-vue'
import {List,HomeFilled,Grid,Tickets,Lock,Avatar,Setting,Key,View,User,Document,Calendar} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
export default {
  name: 'index',
  components:{
    IconMenu, List,HomeFilled,Grid,Tickets,Lock,Avatar,Setting,Key,View,User,Document,Calendar,
  },
  data() {
    return {
      collapseBtnClass: 'fold',
      isCollapse: false, //控制折叠
      sideWidth: 250,
      logoTextShow: true,
      localName: '墨水记忆',
      IconMenu,List,HomeFilled,Grid,Tickets,Lock,Avatar,Setting,Key,View,User,Document,Calendar,
      menuData:[],
    }
  },
  methods: {
    getuserName(){ //获取当前用户名
      let token = localStorage.getItem('token')
      this.$http.post("/user/getUserNameByToken",token).then(res=>{
        if(res.statusCode == '200'){
          this.localName = res.data
        }
      })
    },
    userexit(){ //退出
      this.$router.push("/login")
    },
    collepse() { //左侧菜单栏收缩
      this.isCollapse = !this.isCollapse
      if(this.isCollapse){
        this.sideWidth = 60 //隐藏的宽度
        this.collapseBtnClass = 'expand'
        this.logoTextShow = false
      }else{
        this.sideWidth = 250 //展开宽度
        this.collapseBtnClass = 'fold'
        this.logoTextShow = true
      }
    },
    getMenusByToken(){ //通过token获取对应的菜单
      let token = localStorage.getItem("token")
      this.$http.post("/user/getMenu",token).then(res=>{
        if(res.statusCode == '200'){
          this.menuData = res.data
        }else {
          ElMessage.error('加载失败！')
        }
      }).catch(()=>{
        ElMessage.error('加载失败！')
      })
    }
  },
  created() {
    this.getMenusByToken()
    this.getuserName()
  },
  computed: {
    breadList(){
      return this.$route.matched;
    }
  }
}

</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  /*  background-color: #b3c0d1;*/
  background-color: #ffffff;
  color: var(--el-text-color-primary);
}
/*.layout-container-demo .el-aside {*/
/*  !*width: 240px;*!*/
/*  color: var(--el-text-color-primary);*/
/*  background: #fff !important;*/
/*  border-right: solid 1px #e6e6e6;*/
/*  box-sizing: border-box;*/
/*}*/
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 0;
}
.layout-container-demo .toolbar {
  position: absolute;
  display: inline-flex;
  align-items: center;
  top: 50%;
  right: 20px;
  transform: translateY(-50%);
}

.hiddennav{
  position: relative;
  float: left;
  top:30%;
}
.mbx{
  position: relative;
  float: left;
  top:35%;
  left: 5px;
}


</style>
