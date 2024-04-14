<template>
  <div id="myChart2" class="" :style="{width: '80%', height: '600px'}" />
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
        const myChart = echarts.init(document.getElementById('myChart2'))
        myChart.setOption({
          title: [
            {
              text: 'Radial Polar Bar Label Position (middle)'
            }
          ],
          polar: {
            radius: [30, '80%']
          },
          radiusAxis: {
            max: 4
          },
          angleAxis: {
            type: 'category',
            data: ['a', 'b', 'c', 'd'],
            startAngle: 75
          },
          tooltip: {},
          series: {
            type: 'bar',
            data: [2, 1.2, 2.4, 3.6],
            coordinateSystem: 'polar',
            label: {
              show: true,
              position: 'middle',
              formatter: '{b}: {c}'
            }
          },
          backgroundColor: '#fff',
          animation: false
        })
      }
    }
  }
</script>

