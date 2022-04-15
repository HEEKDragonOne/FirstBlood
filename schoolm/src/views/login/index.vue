<template>
  <div id="login-main">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bolder;color: #2155c2">登录系统</span>
        </div>
      </template>

      <el-form :model="resetForm" status-icon :rules="resetFormRules" ref="resetForm" label-width="100px"  style="width: 300px" hide-required-asterisk="false">
        <el-form-item label="用户名" prop="username">
          <el-input type="text" v-model="resetForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" show-password v-model="resetForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
      </el-form>

      <div class="subm">
        <el-button type="primary" round @click="subup('resetForm')"> 登录</el-button>
<!--        <el-button type="primary" round> 注 册</el-button>-->
      </div>

    </el-card>
    <div class="footerbtn">
      <a href="https://beian.miit.gov.cn/" target="_blank" style="text-decoration: none;color: black;">蜀ICP备2021021930号-1</a>
    </div>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "index",
  data(){
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码！'));
      } else {
        callback();
      }
    };
    var validateid = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入用户名！'));
      } else {
        callback();
      }
    };
    return{
      //重置密码
      resetForm: {},
      resetFormRules: {
        password: [
          { required: true, validator: validatePass,trigger: 'blur' }
        ],
        username: [
          { required: true, validator: validateid, trigger: 'blur' }
        ]
      },

    }
  },
  methods:{
    subup(resetForm){
      this.$refs[resetForm].validate((valid)=>{
        if(valid){
          this.$http.post("/user/login",this.resetForm).then(res=>{
            if(res.statusCode == '200'){
              localStorage.setItem("token",res.data.userToken)
              ElMessage({
                message: '登录成功！',
                type: 'success',
              })
              this.$router.push("/")
            }else {
              ElMessage.error('登录失败！')
            }
          }).catch(()=>{
            ElMessage.error('登录失败！')
          })
        }else{
          ElMessage.error('请填写！')
          return false;
        }
      })
    }
  },
  created() {
    localStorage.removeItem("token")
  }

}
</script>

<style scoped>
#login-main{
  margin-left: 15px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  width: 480px;
  margin-left: 30%;
  margin-top: 10%;
}
.subm{
  margin-left: 35%;
  margin-top: 7%;
}
.footerbtn{
  margin-left: 580px;
  margin-top: 150px;
}

</style>
