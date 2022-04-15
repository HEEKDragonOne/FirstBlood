<template>
  <div id="showData" style="width: 100%;height: 500px">

  </div>

</template>

<script>
import * as echarts from 'echarts';
import {ElMessage} from "element-plus";
export default {
  name: "index",
  mounted() {
    var option = {
      color: ['#17abd5'],
      title: {
        text: '物品类型数据展示',
        textStyle:{
          color: '#2fe0b2'
        },
        left: 500, //距左
        top: 12 //距上
      },
      toolbox:{
        feature:{
          saveAsImage:{},
          dataView:{},
          magicType:{
            type: ['bar','line']
          }
        }
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis:{
        type: 'category',
        data: []
      },
      yAxis:{
        type: 'value'
      },
      series:[
        {
          name: '数量',
          type: 'bar',
          itemStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0,
                color: 'red' //0%处的颜色
              },
                {
                  offset: 1,
                  color: 'blue' //100%处的颜色
                }
              ]
            }
          },
          markPoint: {
            data: [
              {
                type: 'max',
                name: '这是最大值'
              },
              {
                type: 'min',
                name: '这是最小值'
              }
            ]
          },
          markLine: {
            data: [
              {
                type: 'average',
                name: '这是平均值' //这个name并不会在页面中显示
              }
            ]
          },
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)'
          },
          data: []
        }
      ]
    }

    var chartDom = document.getElementById('showData');
    var myChart = echarts.init(chartDom);

    this.$http.get("/item/getTypeName").then(res=>{
      if(res.statusCode == '200'){
        console.log(res.data)
        option.xAxis.data = res.data.x
        option.series[0].data = res.data.y
        myChart.setOption(option);
      }
    }).catch(()=>{
      ElMessage.error('数据加载失败！')
    })
    option && myChart.setOption(option);
  }
}
</script>

<style scoped>
#showData{
  margin-top: 13px;
}
</style>
