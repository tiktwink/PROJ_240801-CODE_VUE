<template>
  <div class="w-full min-h-[400px] flex flex-col items-center gap-y-4 justify-center border">
    <div class="item text-base">
      <p class="mb-4 font-bold text-lg">这个文件上传及访问功能
        <text class="text-red-500">不涉及文件存储到数据库</text>
      </p>
      <p class="mb-2  border-l-2 pl-2">
        vue服务与SpringBoot服务最好在同一局域网中，否则可能需要对两个项目做一些额外配置</p>
      <p class="mb-2  border-l-2 pl-2">SpringBoot接收到vue发送的文件后转存到同机服务器中，返回给vue一个相对路径</p>
      <p class="mb-2  border-l-2 pl-2">
        SpringBoot中需要配置文件存储路径，便于转存，再配置静态资源访问路径(允许多个，按顺序排定优先级)，方便前端访问资源</p>
      <p class="mb-2  border-l-2 pl-2">同时不要忘记在SpringBoot中对特定路径开放拦截器(或查看token有效后放行也可)</p>
      <p class="mb-2  border-l-2 pl-2">
        vue接收到后端返回的相对路径后，在其前面加上特定的前缀(这里是'http://'，具体视前后端情况而定)</p>
      <p class="mb-2  border-l-2 pl-2">
        点击这个链接即可前往访问刚刚上传的文件(如果SpringBoot采取了token查验可能还需要携带token)</p>
      <p class="mb-4  border-l-2 pl-2">本案例中用到primevue的Toast组件和ToastService，将其全局注册，以调用$toast</p>
      <p class="text-sm font-bold my-1">⚫ 1分钟之内上传的相同文件会被覆盖</p>
      <p class="text-sm font-bold my-1">⚫ 文件大小不能超过1M</p>
      <p class="text-sm font-bold my-1">⚫ 1s内只能上传1次</p>
      <p class="text-sm font-bold my-1">⚫ 1次只能上传1个文件</p> <!-- <p>⚫刷接口会被拉黑</p>-->
    </div>
    <div class="item">
      <!--   参考文档：https://element-plus.org/zh-CN/component/upload.html -->
      <!--   参数   multiple:是否支持多选  limit：一次上传的最大文件数-->
      <!--   下方的action被http-request指向的方法覆盖，在此已不生效-->
      <!--   :on-exceed绑定上传文件数超出限制时的处理方法，非必需-->
      <el-upload
          ref="upload"
          v-model:file-list="fileList"
          :auto-upload="false"
          :http-request="picUpload"
          :limit="1"
          :on-success="handleSuccess"
          action="/api/upload"
          class="upload-demo">
        <el-button slot="trigger" size="large" type="primary">选取文件</el-button>
        <el-button :disabled="banUnload" size="large" style="margin-left: 20px;" type="success"
                   @click.prevent.stop="submitUpload">
          上传文件
        </el-button>
      </el-upload>
    </div>
    <div class="item">
      <p class="text-lg font-bold">刚刚上传文件的访问路径：</p>
      <a :href="'http://'+imageUrl" class="text-sm">{{ imageUrl }}</a>
    </div>
  </div>
</template>


<script>
  import axios from "axios";
  import {ElButton, ElUpload} from "element-plus";
  
  export default {
    data() {
      return {
        fileList: [],
        imageUrl: '',
        banUnload: false,
        resList: [], //记录每个文件返回的存储地址
      }
    },
    methods: {
      handleSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      submitUpload() {
        if (this.fileList.length === 0) {
          this.$toast.add({
            severity: 'warn',
            summary: '尚未选择文件',
            life: 3000
          })
          return
        }
        //注意这里使用了$refs
        this.$refs.upload.submit();
        this.fileList = [];
        this.banUnload = true
        setTimeout(() => {
          this.banUnload = false
        }, 1000)
      },
      picUpload(f) {
        let params = new FormData()
        //注意在这里一个坑f.file
        params.append("file", f.file);
        axios({
          method: 'post',
          url: '/api/upload',
          data: params,
          headers: {
            'content-type': 'multipart/form-data'
          }
        })
        .then(resp => {
          if (resp.data.code === 0) {
            this.$toast.add({
              severity: 'success',
              summary: '上传成功',
              life: 1500
            })
            // this.imageUrl = resp.data.data;
            this.resList.push(resp.data.data)
          } else {
            this.$toast.add({
              severity: 'warn',
              summary: '上传失败',
              life: 3000
            })
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: 'error',
            summary: '上传异常',
            life: 3000
          })
        })
      },
      
    },
    components: {
      ElButton,
      ElUpload
    },
    
  }
</script>


<style lang="scss" scoped>

</style>
