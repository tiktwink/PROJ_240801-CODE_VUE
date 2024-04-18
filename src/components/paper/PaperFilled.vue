<script>
  
  import {axiosJson} from "@/apis/axios/axiosJson.js";
  import useAppStore from "@/stores/useAppStore.js";
  import usePaperStore from "@/stores/usePaperStore.js";
  
  export default {
    name: "PaperFilled",
    data() {
      return {
        appStore: useAppStore(),
        paperStore: usePaperStore(),
        imgWidth: 50,
        // reImportOn: false,
      }
    },
    props: {
      paper: {
        type: Object,
        default: ''
      },
    },
    mounted() {
    
    },
    
    watch: {
      imgWidth(newValue, oldValue) {
        if (newValue > 100) this.imgWidth = 100
        else if (newValue < 30) this.imgWidth = 30
      },
      paperfilleduploaddone(newValue, oldValue) {
        if (newValue) {
          //以下1行：用于“重新导入”功能，因为重新导入的试卷原本存在url，由于后台存储策略的问题，即使图像变了，url字面值也不会变，因此不会触发前端自动刷新
          //因此要手动令其触发前端的自动刷新，这里采用的办法是先让其url变为空值（有一点需要注意，不需要url字面值变化才能触发自动刷新，实际上只要paper的任何属性变了，都会触发自动刷新，而不仅仅是url，但对于原本存在url的试卷，重新导入后其hasUrl属性本身仍旧不变，因此只能采用令其url变的策略来触发，当然，也可以手动控制hasUrl变化，即先让hasUrl为0，后面再令其为1
          this.paper.url = '' //这里最好给一个显示“导入中...”的图片地址
          
          axiosJson.get(`/api/paper/index?id=${this.paper.id}`)
          .then(res => {
            if (res.data.code === 0) {
              this.paper.hasUrl = 1
              this.paper.url = res.data.data[0].url
              console.log('子组件PaperFilled.vue修改了props的部分属性')
            }
          })
        }
      },
      'appStore.sideOn'(newValue, oldValue) {
        if (!newValue) {
          //仅在当前界面下，才执行以下动作
          if (this.$route.name === '试卷管理')
            this.appStore.sidePart = '' //当无论因为什么原因，侧栏被关闭时(尤其是手动关闭时)，都要让侧栏恢复默认版块
        }
      }
    },
    computed: {
      reImportOn() {
        return this.paperStore.reImportOn
      },
      paperfilleduploaddone() {
        return this.appStore.paperfilleduploaddone
      },
      
    },
    
    methods: {
      handleWheel(event) {
        if (event.deltaY <= -15) {
          let scroll = document.getElementById('scroll')
          let newWidth = Number(scroll.style.width.replace('%', '')) - event.deltaY / 30
          if (newWidth >= 99.99) newWidth = 100
          
          this.imgWidth = newWidth
          scroll.style.width = newWidth + '%'
          
        } else if (event.deltaY >= 15) {
          let scroll = document.getElementById('scroll')
          let newWidth = Number(scroll.style.width.replace('%', '')) - event.deltaY / 30
          if (newWidth <= 30) newWidth = 30
          
          this.imgWidth = newWidth
          scroll.style.width = newWidth + '%'
        }
      },
      resetImgWidth() {
        document.getElementById('scroll').style.width = '50%'
        this.imgWidth = 50
      },
      importPaper() {
        // axiosJson.post(`/api/paper/singleupload/${this.paper.id}`)
        this.appStore.sideOn = true
        this.appStore.sidePart = 'paperfilledupload'
        this.appStore.sidePaperId = this.paper.id
        this.appStore.paperfilleduploaddone = false //监控，由PaperUpload方修改
      },
      reImportPaper() {
        this.paperStore.reImportOn = !this.paperStore.reImportOn
        if (this.paperStore.reImportOn) {
          this.importPaper()
        } else {
          this.appStore.sidePart = ''
          this.appStore.sideOn = false
          this.appStore.sidePaperId = ''
          this.appStore.paperfilleduploaddone = false //监控，由PaperUpload方修改
        }
        
      }
    },
  }
</script>

<template>
  <div class="w-full h-full px-2">
    <div class="relative">
      <p class="font-bold text-xl mb-3 relative">查看答卷
        <span
            class="absolute left-[21rem] top-12 text-sm btn btn-ghost btn-sm ml-4 mb-2 font-normal border-none bg-white hover:bg-secondary"
            @click="resetImgWidth">
        <svg class="icon" height="16" p-id="3785" t="1713191560562" version="1.1"
             viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
            d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
            fill="#515151" p-id="3786"></path></svg>重置放缩({{ imgWidth.toFixed(0) }}%)
      </span>
        <span v-if="paper.hasUrl===1" :class="{'item-choosen':appStore.sideOn&&appStore.sidePart==='paperfilledupload'}"
              class="absolute text-sm left-[30.5rem] top-12 btn btn-ghost btn-sm ml-4 mb-2 font-normal border-none bg-white hover:bg-secondary"
              @click="reImportPaper">
        <svg v-if="appStore.sideOn&&appStore.sidePart==='paperfilledupload'" class="icon" height="16" p-id="15557"
             t="1713359640135"
             version="1.1" viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
            d="M878.08 731.274667a32 32 0 0 1-54.88-32.938667A360.789333 360.789333 0 0 0 874.666667 512c0-200.298667-162.368-362.666667-362.666667-362.666667S149.333333 311.701333 149.333333 512s162.368 362.666667 362.666667 362.666667a360.789333 360.789333 0 0 0 186.314667-51.445334 32 32 0 0 1 32.928 54.88A424.778667 424.778667 0 0 1 512 938.666667C276.362667 938.666667 85.333333 747.637333 85.333333 512S276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667c0 78.293333-21.152 153.568-60.586667 219.274667zM555.232 512l86.474667 86.474667a30.570667 30.570667 0 1 1-43.232 43.232L512 555.232l-86.474667 86.474667a30.570667 30.570667 0 1 1-43.232-43.232L468.768 512l-86.474667-86.474667a30.570667 30.570667 0 1 1 43.232-43.232L512 468.768l86.474667-86.474667a30.570667 30.570667 0 1 1 43.232 43.232L555.232 512z"
            fill="#ffffff" p-id="15558"></path></svg>
          <svg v-else class="icon" height="16" p-id="3785" t="1713191560562" version="1.1"
               viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
              d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
              fill="#515151" p-id="3786"></path></svg>
          <text v-if="appStore.sideOn&&appStore.sidePart==='paperfilledupload'">取消导入</text>
          <text v-else>重新导入</text>
      </span>
      </p>
      
      <p class="absolute top-1 left-[21rem] font-bold text-xl mb-3 ">管理答卷</p>
      
      <span class="ml-2 text-lg inline-flex items-center"><text class="font-bold mr-1">试卷类型</text><text
          class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5">
              答卷
            </text></span>
      <span class="ml-4 text-lg inline-flex items-center"><text class="font-bold mr-1">试卷ID</text><text
          class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5">
              {{ paper.id }}
            </text></span>
      
      <div class="mt-4 mb-4 py-1 px-1.5 w-fit" style="border:dashed 1px #ccc;">
        <p class="text-[#999] mt-0 mb-2 text-sm pl-0">在
          <text class="text-info">图像</text>
          区域滑动鼠标滑轮放大(上滑)或缩小(下滑)图像
        </p>
        <p class="text-[#999] mt-0 mb-0 text-sm pl-0">在
          <text class="text-info">留白</text>
          区域滑动鼠标滑轮可以滚动视图(当图像过大时)
        </p>
      </div>
      
      
      <div v-if="paper.hasUrl===1" class="flex justify-center">
        <n-scrollbar id="scroll" class="border-none cursor-pointer"
                     style="max-height: calc(100vh - 80px - 12.5rem); width:50%;min-width:30%; max-width:100%; ">
          <div class="flex justify-center items-center bg-white rounded-lg w-full">
            <img :src="'http://'+paper.url" alt="答卷图像"
                 style="width:94%; margin-top:3%; margin-bottom:3%; border-radius:0.5rem; border:1px #ccc solid;"
                 @wheel="handleWheel">
          </div>
        </n-scrollbar>
      </div>
      <div v-else class="flex flex-col items-center gap-y-4 pt-4">
        <p class="text-red-500 text-xl font-bold">当前答卷尚未导入图像...o_O</p>
        <span class="btn btn-ghost border-none bg-white btn-sm hover:bg-secondary font-bold " @click="importPaper">导入答卷图像 =></span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .item-choosen {
    background-color: #05aeec;
    color: #fff;
  }
</style>
