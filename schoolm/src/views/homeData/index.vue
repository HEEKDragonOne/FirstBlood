<template>
  <div id="main"><br>
    <el-row :gutter="12" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card shadow="always">
          <div style="color:#53a8ff;font-size: large"><el-icon><component :is="User"></component></el-icon> 欢迎您</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold;color: #42b983;font-size: larger" >{{ localName }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always">
          <div style="color: #53a8ff;font-size: large"><el-icon><component :is="Timer"></component></el-icon> 今天是：{{ dateFormat(new Date()) }}</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold;color: #53a8ff;font-size: larger" >{{ weekday }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always">
          <div style="color:  #21b9a7;font-size: large"><el-icon><component :is="List"></component></el-icon> 物品总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold;color:#21b9a7;font-size: larger" >{{ itemCnt }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always">
          <div style="color:  #b121bd;font-size: large"><el-icon><component :is="Tickets"></component></el-icon> 物品类型总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold;color: #b121bd;font-size: larger" >{{ itemTypeCnt }}</div>
        </el-card>
      </el-col>
    </el-row>


    <div style="margin-top: 100px">
      <el-descriptions
          title="登陆者信息"
          direction="vertical"
          :column="4"
          :size="size"
          border
      >
        <el-descriptions-item label="操作系统">{{requestInfo.osName}}</el-descriptions-item>
        <el-descriptions-item label="浏览器">{{requestInfo.browserName}}</el-descriptions-item>
        <el-descriptions-item label="版本" :span="2">{{requestInfo.browserVersion}}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{requestInfo.ip}}
        </el-descriptions-item>
      </el-descriptions>
    </div>


  </div>
</template>

<script>
import {Timer,List,Tickets,User} from '@element-plus/icons-vue'
// import {ElMessage} from "element-plus";
export default {
  name: "index",
  components:{
    Timer,List,Tickets,User
  },
  data(){
    return{
      weekday: '', //星期
      Timer,List,Tickets,User,
      localName: '墨水记忆',
      itemCnt: 0 ,
      itemTypeCnt: 0,
      pieData: [],
      requestInfo:[]
    }
  },
  methods:{
    getinfo(){
      this.$http.get("/user/getUserInfo").then(res=>{
        if(res.statusCode == '200'){
          this.requestInfo = res.data
        }
      })
    },
    getItemCnt(){ //获取物品总数
      this.$http.post("/item/getItemCnt").then(res=>{
        if(res.statusCode == '200'){
          this.itemCnt = res.data
        }
      })
    },
    getItemTypeCnt(){ //获取物品类型总数
      this.$http.post("/item/getItemTypeCnt").then(res=>{
        if(res.statusCode == '200'){
          this.itemTypeCnt = res.data
        }
      })
    },
    getuserName(){ //获取当前用户名
      let token = localStorage.getItem('token')
      this.$http.post("/user/getUserNameByToken",token).then(res=>{
        if(res.statusCode == '200'){
          this.localName = res.data
        }
      })
    },
    dateFormat () { //时间处理
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      // var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      // var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      // var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      let week = date.getDay() // 星期
      let weekArr = [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ]
      this.weekday = weekArr[week]
      // return year + '年' + month + '月' + day + '日 ' + hours + ':' + minutes + ':' + seconds + ' ' + weekArr[week]
      return year + '年' + month + '月' + day + '日 '
    }
  },
  created() {
    this.getuserName()
    this.getItemCnt()
    this.getItemTypeCnt()
    this.getinfo()
  }

}
</script>

<style scoped>
#main{
  margin-left: 15px;
  margin-right: 15px;
}

</style>
