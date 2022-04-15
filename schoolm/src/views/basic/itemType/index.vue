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
      <el-upload action="http://127.0.0.1:8081/itemType/import" style="display: inline-block;margin-left: 5px" :on-success="successimport" :on-error="errimport" :show-file-list="false">
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
      <el-table-column prop="typeId" label="类型编号" width="250" align="center"></el-table-column>
      <el-table-column prop="typename" label="类型名称" width="250" align="center"></el-table-column>
      <el-table-column prop="description" label="类型描述" width="500" align="center"></el-table-column>

      <el-table-column fixed="right" label="操作" width="300"  align="center">
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
            :current-page="pageNum"
            :page-sizes="[5, 15, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </el-config-provider>


    <el-dialog title="类型信息" v-model="dialogFormVisible" width="50%">
      <el-form :model="form" size="medium" :rules="rules" ref="form">
        <el-form-item label="ID" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="类型编号" :label-width="formLabelWidth" prop="typeId">
          <el-input v-model="form.typeId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型名称" :label-width="formLabelWidth" prop="typename">
          <el-input v-model="form.typename" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型描述" :label-width="formLabelWidth">
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
      Search,
      locale:zhCn,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      searchname: "",
      dialogFormVisible: false,
      form: {},
      multipleSelection: [],
      formLabelWidth: '100px',
      rules: {
        typeId: [{ required: true, message: '请输入类型编号', trigger: 'blur' }],
        typename: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
      }
    }
  },
  methods:{
    confirmEvent(){ //下载导入模板
      this.$http.get("/itemType/exportMB", {
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
      this.$http.get("/itemType/export", {
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
        this.$http.post("/itemType/batch/del",ids).then(res=>{
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
        this.$http.delete("/itemType/del/"+id).then(res=>{
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
          this.$http.post("/itemType/save",this.form).then(res=>{
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
      this.$http.get("/itemType/pageAll",{
        params: {
          pageNum: this.pageNum,
          PageSize: this.pageSize,
          itemname: this.searchname,
        }
      }).then(res=>{
        if(res.statusCode == '200'){
          this.total = res.data.total
          this.tableData = res.data.records
          console.log(res.data)
        }
      }).catch(()=>{
        ElMessage.error('数据加载失败,请刷新！')
      })
    },
  },
  created() {
    this.network()
  }
}
</script>

<style scoped>
.intemShow{
  margin-left: 15px;
}

</style>
