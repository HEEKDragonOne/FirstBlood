<template>
  <div id="pwd-main"><br>
    <div class="position-updatepwd">
      <el-form :model="resetForm" status-icon :rules="resetFormRules" ref="resetForm" label-width="100px"  style="width: 300px">
        <el-form-item label="个人ID" prop="userid">
          <el-input type="text" v-model="resetForm.userid" auto-complete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="原密码" prop="oldP">
          <el-input type="password" show-password v-model="resetForm.oldP" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newP">
          <el-input type="password" show-password v-model="resetForm.newP" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="newPP">
          <el-input type="password" show-password v-model="resetForm.newPP" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn-sub">
      <el-button type="primary" @click="subup('resetForm')">修改</el-button>
    </div>

  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "index",
  data(){
    var validateoldPass = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入原密码'));
      }else {
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入新密码'));
      } else if (value.toString().length < 6 || value.toString().length > 18) {
        callback(new Error('密码长度为6 - 18个字符'))
      } else {
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.resetForm.newP) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return{
      //重置密码
      resetForm: {
        oldP: '',
        newP: '',
        newPP: '',
        userid: '156854656',
      },
      resetFormRules: {
        oldP: [{ required: true, validator: validateoldPass, trigger: 'blur' }],
        newP: [{ required: true, validator: validatePass, trigger: 'blur' }],
        newPP: [{ required: true, validator: validatePass2, trigger: 'blur' }]
      },

    }
  },
  methods:{
    getIDByToken(){
      let token = localStorage.getItem("token")
      this.$http.post("/user/getUserId",token).then(res=>{
        if(res.statusCode == '200'){
          this.resetForm.userid = res.data
        }
      }).catch(()=>{
        ElMessage.error('获取失败！')
      })
    },
    subup(resetForm){ //提交修改密码
      this.$refs[resetForm].validate((valid)=>{
        if(valid){
          this.$http.post("/user/updateP",this.resetForm).then(res=>{
            if(res.statusCode == '200'){
              ElMessage({
                message: '修改成功！请重新登录！',
                type: 'success',
              })
              this.$router.push("/login")
            }else {
              ElMessage.error('修改失败！')
            }
          }).catch(()=>{
            ElMessage.error('修改失败！')
          })
        }else{
          return false;
        }
      })
    }
  },
  created() {
    this.getIDByToken()
  }

}
</script>

<style scoped>
#pwd-main{
  margin-left: 15px;
}
.position-updatepwd{
  position: absolute;
  top: 150px;
  left: 550px;
}
.btn-sub{
  position: absolute;
  top: 350px;
  left: 710px;
}
</style>
