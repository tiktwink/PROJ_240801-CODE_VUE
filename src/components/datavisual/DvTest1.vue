<template>
  <div>
    <div id="myChart1" class=" " :style="{width: '800%', height: '600px'}"/>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  
  export default {
    mounted() {
      this.drawLine()
    },
    methods: {
      drawLine() {
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('myChart1'))
        myChart.setOption({
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: [150, 230, 224, 218, 135, 147, 260],
              type: 'line'
            }
          ]
        })
      }
    },
  }
</script>

<style lang="scss" scoped>

</style>
