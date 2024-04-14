<script>
  import UseSubjectStore from "@/stores/useSubjectStore.js";
  import {NScrollbar} from "naive-ui";
  
  export default {
    data() {
      return {
        subjectStore: UseSubjectStore(),
      }
    },
    
    computed: {
      subjectRside() {
        return this.subjectStore.subjectsChoosen[0]
      }
    },
    
    mounted() {
    
    
    },
    components: {
      NScrollbar,
    },
  }
</script>

<template>
  
  <div>
    <NScrollbar style="max-height: calc(100vh - 80px - 1rem); ">
      <div v-if="subjectRside!==undefined" class="subject-rside">
        <div><span class="font-bold">科目ID：</span>{{ subjectRside.id }}</div>
        <div><span class="font-bold">科目名称：</span>{{ subjectRside.title }}</div>
        <div><span class="font-bold">设立时间：</span>{{ subjectRside.setTime }}</div>
        
        <div class="related-exams">
          <div class="font-bold mt-1 mb-1">相关考试(待考)</div>
          <div v-for="item in subjectStore.exams" v-show="item.subject===subjectRside.title"
               class="related-exam bg-accent "
               @click.prevent.stop="console.log('hello')">
            
            <div><span class="font-bold">考试ID：</span>{{ item.id }}</div>
            <div><span class="font-bold">考试名称：</span>{{ item.title }}</div>
            <div><span class="font-bold">参考人数：</span>{{ item.num }}</div>
            <div><span class="font-bold">考试时间：</span>{{ item.timeText }}</div>
            <!--          <div>
                        <div class="font-bold">参考班级</div>
                        <div v-for="iitem in item.classes">
                          <span class="mr-2">班级ID：{{iitem.id}}</span>
                          <span>班级名称：{{iitem.title}}</span>
                        </div>
                      </div>-->
          </div>
        </div>
      </div>
      
      <div v-else class="py-4 pl-16 break-all text-xl" style="margin-top:38.2vh; ">
        选中一个科目查看详情...
      </div>
    </NScrollbar>
  </div>

</template>

<style lang="scss" scoped>
  
  .subject-rside {
    padding: 1rem;
    
    > div {
      padding: 0.25rem 0.5rem;
      background-color: #fff;
      border-radius: 0.5rem;
      margin: 0 0 0.5rem;
    }
  }
  
  .related-exams {
    > .related-exam {
      cursor: pointer;
      padding: 0.25rem 0.75rem;
      border-radius: 0.5rem;
      margin: 0.5rem 0.25rem 0.75rem 0.25rem;
    }
    
    > .related-exam:hover {
      background-color: #bbf8ae;
    }
  }
  
  //强制断词换行
  div, span {
    word-break: break-all;
  }

</style>
