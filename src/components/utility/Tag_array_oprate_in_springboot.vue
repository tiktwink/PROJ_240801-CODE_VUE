<script>
  import axios from "axios";
  //防抖辅助函数
  //节流辅助函数
  import {useThrottleFn} from "@vueuse/core";
  
  export default {
    data() {
      return {
        addTagTags: '',
        updateTagId: '',
        updateTagTags: "",
        delTagTd: '',
        queryTagId: '',
        queriedTag: '',
        tagsInDb: [],
      }
    },
    
    mounted() {
      this.queryTagsInDb()
      
      this.debounceQueryTagsInDb = useThrottleFn(() => {
        this.queryTagsInDb()
      }, 2000)
      
      this.debounceAddTag = useThrottleFn(() => {
        this.addTag()
      }, 2000)
      
      this.debounceQueryTag = useThrottleFn(() => {
        this.queryTag()
      }, 2000)
    },
    
    methods: {
      addTag() {
        let tags = this.addTagTags
        tags = tags.split(' ').filter(item => item.trim() !== '') //去除空标签
        
        if (tags.length > 10) {
          alert('超过10个标签啦，不要太多')
          return
        }
        
        if (this.addTagTags.length > 200) {
          alert('这个标签的信息量有点大哦')
          return
        }
        console.log('tags tobe added: ' + tags)
        
        axios.put('/api/tag/index', {
          tags
        }).then(res => {
          console.log(res)
          if (res.data.code === 0) {
            this.queryTagsInDb()
          } else if (res.data.msg.indexOf('1000') >= 0) {
            alert('已经有1000个标签啦，不能再多啦')
          }
        }).catch(err => {
          console.log(err)
        })
      },
      
      queryTag() {
        axios.get(`/api/tag/${this.queryTagId}`)
        .then(res => {
          if (res.data.code === 0) {
            console.log('查询成功: ')
            console.log(res.data.data)
            this.queriedTag = res.data.data
          } else {
            console.log(res)
          }
        })
      },
      
      testlog() {
      
      },
      
      clickToQuery(e) {
        this.queryTagId = e.target.textContent
        this.queryTag()
      },
      
      queryTagsInDb() {
        axios.get('/api/tag/all')
        .then(res => {
          if (res.data.code === 0) {
            console.log('查询已有tag成功: ')
            console.log(res.data.data)
            this.tagsInDb = res.data.data
          }
        })
      }
      
    },
  }
</script>

<template>
  <div class="flex justify-center pt-8">
    <div class="wrapper flex flex-col gap-y-4">
      <div class="item flex flex-col items-center gap-y-2 min-w-[240px] ">
        <input v-model.trim="queryTagId" class="text-sm w-full rounded-lg p-1 add-new-tag"
               placeholder="查询一个tag，此处是其ID"
               type="text">
        <span class="w-full bg-base-300 hover:bg-secondary btn-sm btn btn-ghost" @click="debounceQueryTag">查询</span>
        <div class="text-xs">查询结果(仅展示标签数组，完整信息在开发者工具控制台查看):</div>
        <span class="text-base bg-secondary rounded-lg p-1 w-full h-[30px] text-center">{{ queriedTag.tags }}</span>
      </div>
      <div class="item flex flex-col items-center gap-y-2 min-w-[240px] ">
        <input v-model.trim="addTagTags" class="text-sm w-full rounded-lg p-1 add-new-tag"
               placeholder="新增一个tag，用空格分隔不同标签"
               type="text">
        <span class="w-full bg-base-300 hover:bg-secondary btn-sm btn btn-ghost" @click="debounceAddTag">新增</span>
      </div>
      <div class="item flex flex-col items-center gap-y-2 min-w-[240px] ">
        <input v-model.trim="updateTagId" class="text-sm w-full rounded-lg p-1 update-tag"
               placeholder="更新一个已存在的tag，此处是其ID" type="text">
        <input v-model.trim="updateTagTags" class="text-sm w-full rounded-lg p-1 update-tag"
               placeholder="用空格分隔不同标签"
               type="text">
        <span class="w-full bg-base-300 hover:bg-secondary btn-sm btn btn-ghost" @click="updateTag">更新</span>
      </div>
      <div class="item flex flex-col items-center gap-y-2 min-w-[240px] ">
        <input v-model.trim="delTagTd" class="text-sm w-full rounded-lg p-1 add-new-tag"
               placeholder="删除一个已存在的tag，此处是其ID"
               type="text">
        <span class="w-full bg-base-300 hover:bg-secondary btn-sm btn btn-ghost" @click="delTag">删除</span>
      </div>
    </div>
    
    <div class="wrapper wrapper-right relative">
      <div class="font-bold text-center mt-1 mb-3">数据库中存在的tag(ID)</div>
      <span class="btn btn-xs btn-ghost bg-base-300 hover:bg-secondary absolute top-[0.75rem] right-[0.75rem]"
            @click="debounceQueryTagsInDb">刷新</span>
      <span v-for="item in tagsInDb"
            class="cursor-pointer tag font-bold min-w-[30px] h-[30px] bg-base-300 hover:bg-secondary px-1.5 py-0.5 rounded-lg"
            @click="clickToQuery($event)">{{ item.id }}</span>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .wrapper {
    margin: 1rem;
    
    &.wrapper-right {
      width: 300px;
      height: 480px;
      padding: 0.5rem;
      border: 1px deepskyblue dashed;
      border-radius: 0.5rem;
      overflow-x: hidden;
      overflow-y: auto;
      
      .tag {
        margin: 0.25rem 0.4rem;
        display: flex;
        justify-content: center;
        align-items: center;
        float: left; //向左浮动
        max-width: 100px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }
    }
    
    > .item {
      padding: 0.5rem;
      border: 1px deepskyblue dashed;
      border-radius: 0.5rem;
      overflow: hidden;
    }
  }
  
  .wrapper-right::-webkit-scrollbar {
    width: 8px !important;
    background-color: #dddddd;
    border-radius: 15px;
  }
  
  .wrapper-right::-webkit-scrollbar-thumb {
    border-radius: 15px;
    -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0.4);
    background-color: #bcf8ae;
    border: none;
  }

</style>
