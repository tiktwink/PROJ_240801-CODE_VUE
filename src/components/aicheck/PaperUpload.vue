<template>
  <div class="w-full pt-2 border-none rounded-lg bg-white flex flex-col items-center gap-y-1 justify-center border">
    <div class="item text-base">
      <!--      <p class="text-base font-bold my-1">⚫ 1分钟之内上传的相同文件会被覆盖</p>-->
      <p class="text-base font-bold my-1">⚫ 单个文件大小不能超过2M</p>
      <!--      <p class="text-base font-bold my-1">⚫ 1s内只能上传1次</p>-->
      <p class="text-base font-bold my-1">⚫ 1次只能上传1个文件</p> <!-- <p>⚫刷接口会被拉黑</p>-->
      <p class="text-base font-bold my-1">⚫ 已上传过的试卷再次上传会被覆盖</p>
    </div>
    
    <div class="item">
      <!--   参考文档：https://element-plus.org/zh-CN/component/upload.html -->
      <!--   参数   multiple:是否支持多选  limit：一次上传的最大文件数-->
      <!--   下方的action被http-request指向的方法覆盖，在此已不生效-->
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
    <!--    <div class="item">-->
    <!--      <p class="text-lg font-bold">刚刚上传文件的访问路径：</p>-->
    <!--      <a :href="'http://'+imageUrl" class="text-sm">{{ imageUrl }}</a>-->
    <!--    </div>-->
  </div>
</template>


<script>
  import axios from "axios";
  import {ElButton, ElUpload} from "element-plus";
  import useUserStore from "@/stores/useUserStore.js";
  
  export default {
    props: {
      paperId: {
        type: String,
        default: '9001'
      },
    },
    data() {
      return {
        fileList: [],
        imageUrl: '',
        banUnload: false,
        resList: [],
      }
    },
    methods: {
      handleSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        console.log(this.imageUrl)
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
        this.$refs.upload.submit()
        
        this.fileList = [];
        this.banUnload = true
        setTimeout(() => {
          this.banUnload = false
        }, 1000)
      },
      picUpload(f) {
        console.log('picUpload')
        let params = new FormData()
        //注意在这里一个坑f.file
        params.append("file", f.file);
        console.log(this.paperId)
        axios({
          method: 'post',
          url: `/api/paper/singleupload/${this.paperId}`,
          data: params,
          headers: {
            username: useUserStore().username,
            token: useUserStore().token,
            'content-type': 'multipart/form-data'
          }
        })
        .then(resp => {
          if (resp.data.code === 0) {
            this.$emit('uploadsuccess')
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
