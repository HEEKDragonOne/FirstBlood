<template>
  <div class="intemShow">
    <!--    搜索框-->
    <div style="padding: 10px 0">
      <el-input style="width: 250px" placeholder="请输入名称" :suffix-icon="Search" v-model="searchname"></el-input>
      <!--      <el-input style="width: 250px;margin-left: 5px" placeholder="请输入类型" :suffix-icon="Search" v-model="searchtype"></el-input>-->

      <el-select
          style="margin-left: 5px"
          v-model="typeState"
          filterable
          allow-create
          default-first-option
          :reserve-keyword="false"
          placeholder="请输入或选择类型"
      >
        <el-option
            v-for="item in typeForm"
            :key="item.roleId"
            :label="item.rolename"
            :value="item.roleId"
        >
        </el-option>
      </el-select>


      <el-button type="primary" style="margin-left: 5px" @click="network">
        <el-icon style="vertical-align: middle;">
          <search />
        </el-icon>
        <span style="vertical-align: middle;"> 搜索 </span>
      </el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="addItem">新增 <el-icon style="margin-left: 3px"><circle-plus /></el-icon></el-button>
      <el-button type="danger" @click="Batchdele">批量删除 <el-icon style="margin-left: 3px"><delete /></el-icon></el-button>
      <el-upload action="http://127.0.0.1:8081/user/import" style="display: inline-block;margin-left: 5px" :on-success="successimport" :on-error="errimport" :show-file-list="false">
        <el-popconfirm confirm-button-text="下载"
                       cancel-button-text="不用了"
                       :icon="InfoFilled"
                       icon-color="red"
                       title="是否需要下载导入模板？"
                       @confirm="confirmEvent">
          <template #reference>
            <el-button type="success">导入 <el-icon style="margin-left: 3px"><upload-filled /></el-icon></el-button>
          </template>
        </el-popconfirm>

      </el-upload>
      <el-button type="success" @click="itemexport" style="margin-left: 5px">导出 <el-icon style="margin-left: 3px"><download /></el-icon></el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" width="70" align="center"></el-table-column>

      <el-table-column fixed prop="id" label="id" width="100" sortable align="center"></el-table-column>
      <el-table-column prop="userid" label="人员编号" width="200" align="center"></el-table-column>
      <el-table-column prop="username" label="人员名称" width="300" align="center"></el-table-column>
      <el-table-column prop="roleName" label="角色类型" width="300" align="center"></el-table-column>
      <el-table-column prop="description" label="人员描述" width="480" align="center"></el-table-column>


      <el-table-column fixed="right" label="操作" width="190"  align="center">
        <template v-slot="scope">
          <el-button @click="handleClick(scope.row)" type="warning" size="default">编辑 <el-icon><edit /></el-icon></el-button>
          <el-button @click="delOne(scope.row.id)" type="danger" size="default">删除 <el-icon><remove /></el-icon></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-config-provider :locale="locale">
      <div style="padding: 10px 0">
        <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            v-model:currentPage="pageNum"
            :page-sizes="[5, 15, 50, 100]"
            v-model:page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </el-config-provider>

    <!--    弹窗信息-->

    <el-dialog title="人员信息" v-model="dialogFormVisible" width="50%">
      <el-form :model="form" size="medium" :rules="rules" ref="form">
        <el-form-item label="ID" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="人员编号" :label-width="formLabelWidth" prop="userid">
          <el-input v-model="form.userid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="人员名称" :label-width="formLabelWidth" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色类型" :label-width="formLabelWidth" prop="roleId">
          <!--          <el-input v-model="form.typeid" autocomplete="off"></el-input>-->
          <el-select
              style="margin-left: 5px"
              v-model="form.roleId"
              filterable
              allow-create
              default-first-option
              :reserve-keyword="false"
              placeholder="请输入或选择类型"
          >
            <el-option
                v-for="item in typeForm"
                :key="item.roleId"
                :label="item.rolename"
                :value="item.roleId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="人员描述" :label-width="formLabelWidth">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> 取消</el-button>
        <el-button type="primary" @click="saveData('form')"> 确认</el-button>
      </span>
      </template>


    </el-dialog>

  </div>
</template>

<script>
import zhCn from 'element-plus/lib/locale/lang/zh-cn'
import {Search} from '@element-plus/icons-vue'
import {ElMessageBox, ElMessage } from 'element-plus'
import fileDownload from 'js-file-download'
export default {
  name: "index",
  components:{
    Search,
  },
  data() {
    return {
      Search,
      locale:zhCn,
      tableData: [],
      total: 0, //数据总条数
      pageNum: 1, //当前页
      pageSize: 5, //页大小
      searchname: "", //搜索名称
      searchtype: 0, //搜索类型
      dialogFormVisible: false, //增加的弹窗
      form: {}, //弹窗中的数据
      typeForm: [], //类型列表
      typeState: 0, //选中的物品类型编号
      multipleSelection: [], //存储批量删除的数据id
      formLabelWidth: '100px',
      rules: {
        username: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        userid:[{required: true, message: '请输入角色编号', trigger: 'blur'}],
        roleId:[{required: true, message: '请选择角色类型', trigger: 'blur'}]
      }
    }
  },
  methods:{
    confirmEvent(){ //下载导入模板
      this.$http.get("/user/exportMB", {
        responseType: 'arraybuffer'
      }).then((res)=>{
        ElMessage({
          message: '下载成功！',
          type: 'success',
        })
        fileDownload(res,'导入模板_'+ new Date().toLocaleString()+'.xlsx')
      }).catch(()=>{
        ElMessage.error('下载失败！')
      })
    },
    errimport(){ //导入失败
      ElMessage.error('导入失败！')
    },
    successimport(){ //导入成功
      this.network()
      ElMessage({
        message: '导入成功！',
        type: 'success',
      })
    },
    itemexport(){ //导出
      this.$http.get("/user/export", {
        responseType: 'arraybuffer'
      }).then((res)=>{
        ElMessage({
          message: '导出成功！',
          type: 'success',
        })
        fileDownload(res,'下载信息_'+ new Date().toLocaleString()+'.xlsx')
      }).catch(()=>{
        ElMessage.error('导出失败！')
      })
    },
    itemTypeGet(){ //获取类型数据
      this.$http.get("/userRole/userRoleAll").then(res=>{
        if(res.statusCode == '200'){
          this.typeForm = res.data
        }
      }).catch(()=>{
        ElMessage.error('角色加载失败！')
      })
    },
    handleSelectionChange(row){ //批量删除选择的数据
      this.multipleSelection = row
    },
    Batchdele(){ //批量删除
      let ids = this.multipleSelection.map(v=>v.id)
      ElMessageBox.confirm(
          '是否批量删除选中数据？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        this.$http.post("/user/batch/del",ids).then(res=>{
          if(res.statusCode == '200'){
            ElMessage({
              message: '删除成功！',
              type: 'success',
            })
            this.network()
          }
        }).catch(()=>{
          ElMessage.error('删除失败！')
        })
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消！',
        })
      })
    },
    delOne(id){ //删除
      ElMessageBox.confirm(
          '是否删除选中数据？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        this.$http.delete("/user/del/"+id).then(res=>{
          if(res.statusCode == '200'){
            ElMessage({
              message: '删除成功！',
              type: 'success',
            })
            this.network()
          }
        }).catch(()=>{
          ElMessage.error('删除失败！')
        })
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消！',
        })
      })
    },
    handleClick(row){ //编辑
      this.itemTypeGet()
      this.form = row
      this.dialogFormVisible=true
    },
    saveData(resetForm){ //用于数据的添加和更新
      this.itemTypeGet()
      this.$refs[resetForm].validate((valid)=>{
        if(valid){
          this.$http.post("/user/save",this.form).then(res=>{
            if(res.statusCode == '200'){
              ElMessage({
                message: '成功！',
                type: 'success',
              })
              this.dialogFormVisible=false
              this.network()
            }
          }).catch(()=>{
            ElMessage.error('失败！')
          })
        }else{
          ElMessage({
            message: '已取消！',
            type: 'warning',
          })
          return false;
        }
      })
    },
    handleSizeChange(val){ //页大小改变
      this.pageSize = val
      this.network()
    },
    handleCurrentChange(val){ //当前页改变
      this.pageNum = val
      this.network()
    },
    addItem(){ //添加物品
      this.itemTypeGet()
      this.dialogFormVisible = true
      this.form = {}
    },
    network(){ //分页查询
      this.searchtype = this.typeState
      this.$http.get("/user/pageAll",{
        params: {
          pageNum: this.pageNum,
          PageSize: this.pageSize,
          itemname: this.searchname,
          searchtype: this.searchtype
        }
      }).then(res=>{
        if(res.statusCode == '200'){
          this.total = res.data.total
          this.tableData = res.data.records
        }
      }).catch(()=>{
        ElMessage.error('数据加载失败,请刷新！')
      })
    },
  },
  created() {
    this.network()
    this.itemTypeGet()
  }
}
</script>

<style scoped>
.intemShow{
  margin-left: 15px;
}

</style>
