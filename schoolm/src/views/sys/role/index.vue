<template>
  <div class="intemShow">
    <!--    搜索框-->
    <div style="padding: 10px 0">
      <el-input style="width: 250px" placeholder="请输入名称" :suffix-icon="Search" v-model="searchname"></el-input>

      <el-button type="primary" style="margin-left: 5px" @click="network">
        <el-icon style="vertical-align: middle;"><search /></el-icon>
        <span style="vertical-align: middle;"> 搜索 </span>
      </el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="addItem">新增 <el-icon style="margin-left: 3px"><circle-plus /></el-icon></el-button>
      <el-button type="danger" @click="Batchdele">批量删除 <el-icon style="margin-left: 3px"><delete /></el-icon></el-button>
      <el-upload action="http://127.0.0.1:8081/userRole/import" style="display: inline-block;margin-left: 5px" :on-success="successimport" :on-error="errimport" :show-file-list="false">
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

      <el-table-column fixed prop="id" label="id" width="110" sortable align="center"></el-table-column>
      <el-table-column prop="roleId" label="角色编号" width="250" align="center"></el-table-column>
      <el-table-column prop="rolename" label="角色名称" width="250" align="center"></el-table-column>
      <el-table-column prop="description" label="角色描述" width="500" align="center"></el-table-column>

      <el-table-column fixed="right" label="操作" width="300"  align="center">
        <template v-slot="scope">
          <el-button type="info" size="default" @click="selectMenu(scope.row.id)">权限分配 <el-icon><Grid /></el-icon></el-button>
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
            :current-page="pageNum"
            :page-sizes="[5, 15, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </el-config-provider>


    <el-dialog title="角色信息" v-model="dialogFormVisible" width="50%">
      <el-form :model="form" size="medium" :rules="rules" ref="form">
        <el-form-item label="ID" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="角色编号" :label-width="formLabelWidth" prop="roleId">
          <el-input v-model="form.roleId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" :label-width="formLabelWidth" prop="rolename">
          <el-input v-model="form.rolename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" :label-width="formLabelWidth">
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

    <!--      权限分配弹窗-->
    <el-dialog title="权限分配" v-model="menuFormVisible" width="50%">
      <el-tree
          :data="menudata"
          show-checkbox
          default-expand-all
          :default-checked-keys="checkdata"
          node-key="id"
          ref="tree"
          highlight-current
          :props="defaultProps">
      </el-tree>

      <template #footer>
      <span class="dialog-footer">
        <el-button @click="menuFormVisible = false"> 取消</el-button>
        <el-button type="primary" @click="saveRoleMenu"> 确认</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import zhCn from 'element-plus/lib/locale/lang/zh-cn'
import {Search} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";
import fileDownload from "js-file-download";
// import fileDownload from 'js-file-download'
export default {
  name: "index",
  components:{
    Search,
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'menuName'
      },
      Search,
      locale:zhCn,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      searchname: "",
      dialogFormVisible: false,
      menuFormVisible:false,
      form: {},
      menudata: [], //权限列表数据
      checkdata: [], //选择数据
      nowRoleID: 0,//选择的分配角色id
      multipleSelection: [],
      formLabelWidth: '100px',
      rules: {
        roleId: [{ required: true, message: '请输入角色编号', trigger: 'blur' }],
        rolename: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
      }
    }
  },
  methods:{
    saveRoleMenu(){
      this.$http.post("/userRole/roleMenu/"+this.nowRoleID,this.$refs.tree.getCheckedKeys()).then(res=>{
        if(res.statusCode == '200'){
          ElMessage({
            message: '成功！',
            type: 'success',
          })
          this.menuFormVisible=false
        }else {
          ElMessage.error('修改失败！')
        }
      }).catch(()=>{
        ElMessage.error('失败！')
      })
    },
    selectMenu(roleid){ //权限分配的菜单数据
      //获取菜单数据
      this.$http.get("/menu/menuAll").then(res=>{
        if(res.statusCode == '200'){
          this.menudata = res.data
        }
      }).catch(()=>{
        ElMessage.error('数据加载失败,请刷新！')
      })
      this.nowRoleID = roleid
      this.menuFormVisible = true

      //获取选择角色的已有权限
      this.$http.get("/userRole/roleMenu/"+roleid).then(res=>{
        if(res.statusCode == '200'){
          this.checkdata = res.data
        }
      }).catch(()=>{
        ElMessage.error('数据加载失败,请刷新！')
      })

    },
    confirmEvent(){ //下载导入模板
      this.$http.get("/userRole/exportMB", {
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
      this.$http.get("/userRole/export", {
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
        this.$http.post("/userRole/batch/del",ids).then(res=>{
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
        this.$http.delete("/userRole/del/"+id).then(res=>{
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
      this.form = row
      this.dialogFormVisible=true
    },
    saveData(resetForm){ //用于数据的添加和更新
      this.$refs[resetForm].validate((valid)=>{
        if(valid){
          this.$http.post("/userRole/save",this.form).then(res=>{
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
    addItem(){ //添加物品
      this.dialogFormVisible = true
      this.form = {}
    },
    handleSizeChange(val){ //页大小改变
      this.pageSize = val
      this.network()
    },
    handleCurrentChange(val){ //当前页改变
      this.pageNum = val
      this.network()
    },
    network(){ //分页查询
      this.searchtype = this.typeState
      this.$http.get("/userRole/pageAll",{
        params: {
          pageNum: this.pageNum,
          PageSize: this.pageSize,
          itemname: this.searchname,
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
    outmsg(){
      ElMessage({
        type: 'info',
        message: '角色编号请修改为对应ID！',
      })
    }
  },
  created() {
    this.network()
    this.outmsg()
  }
}
</script>

<style scoped>
.intemShow{
  margin-left: 15px;
}

</style>
