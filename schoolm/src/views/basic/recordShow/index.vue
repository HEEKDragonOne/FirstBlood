<template>
  <div class="recordShow">
    <!--    搜索框-->
    <div style="padding: 10px 0">
      <el-input style="width: 250px" placeholder="请输入名称" :suffix-icon="Search" v-model="searchname"></el-input>


      <el-button type="primary" style="margin-left: 5px" @click="network">
        <el-icon style="vertical-align: middle;">
          <search />
        </el-icon>
        <span style="vertical-align: middle;"> 搜索 </span>
      </el-button>
    </div>
    <!--127.0.0.1-->
    <div style="padding: 10px 0">
      <el-button type="primary" @click="addItem">新增 <el-icon style="margin-left: 3px"><circle-plus /></el-icon></el-button>
      <el-button type="danger" @click="Batchdele">批量删除 <el-icon style="margin-left: 3px"><delete /></el-icon></el-button>
      <el-button type="success" @click="itemexport" style="margin-left: 5px">导出 <el-icon style="margin-left: 3px"><download /></el-icon></el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" width="40" align="center"></el-table-column>

      <el-table-column fixed prop="id" label="id" width="100" sortable align="center"></el-table-column>
      <el-table-column prop="recordName" label="借出名称" width="250" align="center"></el-table-column>

      <el-table-column prop="isShow" label="借出状态" width="150" align="center">

        <template v-slot="scope">
          <el-switch
              v-model="scope.row.isShow"
              class="ml-2"
              inline-prompt
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="Y"
              inactive-text="N"
              @change="rowKG(scope.row)"
          />
        </template>
      </el-table-column>

      <el-table-column prop="getCode" label="领取码" width="200" sortable align="center"></el-table-column>
      <el-table-column prop="recordMan" label="领取者" width="200"  align="center"></el-table-column>
      <el-table-column prop="description" label="备注" width="250" align="center"></el-table-column>
      <el-table-column prop="recordAddTime" label="添加时间" width="200" sortable align="center"></el-table-column>
      <el-table-column prop="recordUpdateTime" label="修改时间" width="200" sortable align="center"></el-table-column>


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

    <el-dialog title="借出信息" v-model="dialogFormVisible" width="50%">
      <el-form :model="form" size="medium" :rules="rules" ref="form">
        <el-form-item label="ID" :label-width="formLabelWidth">
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="借出名称" :label-width="formLabelWidth" prop="recordName">
          <el-input v-model="form.recordName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="领取者" :label-width="formLabelWidth" prop="recordMan">
          <el-input v-model="form.recordMan" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth">
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
      dialogFormVisible: false, //增加的弹窗
      form: {}, //弹窗中的数据
      typeForm: [], //物品类型列表
      typeState: 0, //选中的物品类型编号
      multipleSelection: [], //存储批量删除的数据id
      formLabelWidth: '100px',
      rules: {
        recordName: [{ required: true, message: '请输入借出名称', trigger: 'blur' }],
        recordMan:[{required: true, message: '请输入领取者', trigger: 'blur'}]
      }
    }
  },
  methods:{
    itemexport(){ //导出
      this.$http.get("/record/export", {
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
        this.$http.post("/record/batch/del",ids).then(res=>{
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
        this.$http.delete("/record/del/"+id).then(res=>{
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
          this.$http.post("/record/save",this.form).then(res=>{
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
    rowKG(row){ //状态改变
      ElMessageBox.confirm(
          '确定要改变状态吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      ).then(() => {
        // row.isShow = !row.isShow //点击后，默认已经改变了，所以无需再手动改变
        this.$http.post("/record/changeShow",row).then((res)=>{
          if(res.statusCode == '200'){
            ElMessage({
              message: '成功！',
              type: 'success',
            })
            this.network()
          }else {
            this.network()
            ElMessage.error('失败！')
          }
        }).catch(()=>{
          this.network()
          ElMessage.error('失败！')
        })
      }).catch(() => {
        this.network()
        ElMessage({
          type: 'info',
          message: '已取消！',
        })
      })

    },
    addItem(){ //添加物品
      this.dialogFormVisible = true
      this.form = {}
    },
    network(){ //分页查询
      this.$http.get("/record/pageAll",{
        params: {
          pageNum: this.pageNum,
          PageSize: this.pageSize,
          itemname: this.searchname
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
  }
}
</script>

<style scoped>
.recordShow{
  margin-left: 15px;
}

</style>
