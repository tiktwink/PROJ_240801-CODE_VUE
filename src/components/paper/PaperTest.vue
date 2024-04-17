<!--编写一个日志组件，仿照CheckLog.vue-->
<template>
  <div class="paper-test">
    <h1 class="paper-title">{{ paper.title }}</h1>
    <div class="paper-info">
      <span>试卷类型：{{ paper.type }}</span>
      <span>总分：{{ paper.totalScore }}</span>
      <!-- 可添加其他试卷基本信息 -->
    </div>
    
    <section class="sections">
      <h2>底卷</h2>
      <ul class="questions-list">
        <li v-for="(question, index) in paper.draftSheet" :key="index">
          <h3>{{ question.title }}</h3>
          <p v-html="question.content"></p>
          <!-- 显示题目选项、图片等额外信息 -->
        </li>
      </ul>
    </section>
    
    <section class="sections">
      <h2>答卷</h2>
      <ul class="answers-list">
        <li v-for="(answer, index) in paper.answerSheet" :key="index">
          <h3>{{ answer.questionTitle }}</h3>
          <p v-html="answer.userAnswer"></p>
          <!-- 显示用户提交的答案、得分、评语等 -->
        </li>
      </ul>
    </section>
    
    <section class="sections">
      <h2>答案</h2>
      <ul class="correct-answers-list">
        <li v-for="(correctAnswer, index) in paper.correctAnswers" :key="index">
          <h3>{{ correctAnswer.questionTitle }}</h3>
          <p v-html="correctAnswer.answerContent"></p>
          <!-- 显示标准答案、解析等 -->
        </li>
      </ul>
    </section>
    
    <!-- 可添加底部导航或其他交互元素，如切换试卷类型按钮 -->
  </div>
</template>

<script>
  export default {
    props: {
      // 如果是从父组件传递过来的试卷数据
      paperId: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        paper: null,
      };
    },
    async created() {
      const response = await this.fetchPaperData(this.paperId);
      this.paper = response.data;
    },
    methods: {
      async fetchPaperData(paperId) {
        // 替换为实际的API调用
        const url = `/api/papers/${paperId}`;
        const response = await this.$http.get(url);
        return response;
      },
    },
  };
</script>

<style scoped>
  .paper-test {
    /* 添加样式规则 */
  }
  
  /* ... 其他样式定义 */
</style>
