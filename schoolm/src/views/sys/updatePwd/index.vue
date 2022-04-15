<template>
  <div id="pwd-main"><br>
    <div class="position-updatepwd">
      <el-form :model="resetForm" status-icon :rules="resetFormRules" ref="resetForm" label-width="100px"  style="width: 300px">
        <el-form-item label="人员ID" prop="userid">
          <el-input type="text" v-model="resetForm.userid" auto-complete="off"></el-input>
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
    var validateid = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入被修改人员ID！'));
      } else if (value.toString().length < 1) {
        callback(new Error('人员ID不能为空！'));
      } else {
        callback();
      }
    };
    return{
      //重置密码
      resetForm: {
        newP: '',
        newPP: '',
        userid: '',
      },
      resetFormRules: {
        newP: [{ required: true, validator: validatePass, trigger: 'blur' }],
        newPP: [{ required: true, validator: validatePass2, trigger: 'blur' }],
        userid: [{ required: true, validator: validateid, trigger: 'blur' }]
      },

    }
  },
  methods:{
    subup(resetForm){
      this.$refs[resetForm].validate((valid)=>{
        if(valid){
          this.$http.post("/user/updateOther",this.resetForm).then(res=>{
            if(res.statusCode == '200'){
              ElMessage({
                message: '修改成功！',
                type: 'success',
              })
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
  top: 300px;
  left: 710px;
}
</style>
