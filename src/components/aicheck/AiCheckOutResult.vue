<script>
  import {NResult} from "naive-ui";
  import UseCheckoutStore from "@/stores/useCheckoutStore.js";
  import useExamStore from "@/stores/useExamStore.js";
  
  export default {
    data() {
      return {
        mode: 'exam',
        checkoutStore: UseCheckoutStore(),
      }
    },
    components: {
      NResult,
    },
    mounted() {
      this.mode = this.$route.query.mode
      
      document.getElementById('checkout').style.display = 'block'
      
      setTimeout(() => {
        document.getElementById('loading').style.display = 'none'
        document.getElementById('checkoutmask').style.minHeight = '0'
        
      }, 1500)
      
    },
    unmounted() {
      //在此处将阅卷历史进行记录
      useExamStore().examsChoosen = []
    },
    methods: {
      enterCheckout() {
        this.$router.push(`/checkout`)
        
      }
    },
  }

</script>

<template>
  <span id="loading" class="loading loading-dots loading-lg" style="height:200px; padding:100px 0 0;"></span>
  
  <div id='checkoutmask' class=" w-full " style=" min-height: 100vh; transition: min-height 2s; "></div>
  
  <div id="checkout" class="pt-16 px-16 bg-[#f5f5f5] " style=" height: calc(100vh - 80px - 0.5rem); width: 100%;  "
       @click="enterCheckout">
    <n-result description="" status="success" title="评阅成功">
      <template #footer>
        
        <div class="stats shadow">
          
          <div class="stat">
            <div class="stat-figure text-secondary">
              <svg class="icon" height="32" p-id="7490" t="1710148007011"
                   version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M408.551619 97.52381a73.142857 73.142857 0 0 1 51.736381 21.430857L539.306667 197.973333A73.142857 73.142857 0 0 0 591.067429 219.428571H804.571429a73.142857 73.142857 0 0 1 73.142857 73.142858v333.165714l-125.878857 124.001524-69.729524-68.803048-119.881143 121.465905L687.932952 926.47619H219.428571a73.142857 73.142857 0 0 1-73.142857-73.142857V170.666667a73.142857 73.142857 0 0 1 73.142857-73.142857h189.123048z m490.886095 575.390476l51.321905 52.102095-198.948571 195.974095-120.588191-118.979047 51.370667-52.077715 69.266286 68.33981 147.577904-145.359238zM341.333333 633.904762h-73.142857v73.142857h73.142857v-73.142857z m195.047619 0h-146.285714v73.142857h146.285714v-73.142857z m-195.047619-195.047619h-73.142857v73.142857h73.142857v-73.142857z m414.476191 0H390.095238v73.142857h365.714286v-73.142857z"
                    fill="#05aefc" p-id="7491"></path>
              </svg>
            
            </div>
            <div class="stat-title font-bold text-xl text-black ">共计评阅</div>
            <div v-if="mode==='paper'" class="stat-value " style="margin-bottom: 0.5rem; color:#05aefc; ">
              {{ checkoutStore.papers.length }}份
            </div>
            <div v-else-if="mode==='exam'" class="stat-value " style="margin-bottom: 0.5rem; color:#05aefc; ">
              {{ checkoutStore.exams.reduce((sum, item) => sum + item.num, 0) }}份
            </div>
            <div class="stat-desc font-bold text-xl text-black">试卷</div>
          </div>
          
          <!--          <div class="stat">-->
          <!--            <div class="stat-figure text-secondary">-->
          <!--              <svg class="icon" height="32" p-id="7812" t="1710148194564"-->
          <!--                   version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg">-->
          <!--                <path-->
          <!--                    d="M591.62819 121.904762c7.68 0 21.187048 1.633524 31.524572 4.973714 50.907429 16.505905 80.213333 70.582857 68.339809 124.708572l-1.219047 5.071238-45.372953 170.179047h179.736381c21.820952 0 43.398095 8.094476 58.026667 24.283429 16.335238 18.090667 22.698667 42.032762 17.846857 65.267809l-1.219047 4.973715-94.768762 337.432381a59.294476 59.294476 0 0 1-52.662857 43.154285l-4.388572 0.146286H268.190476V444.001524c97.084952-21.016381 239.079619-279.28381 239.079619-279.28381C527.457524 137.337905 551.936 121.904762 591.62819 121.904762zM121.904762 438.857143h73.142857v463.238095H121.904762z"-->
          <!--                    fill="#05aefc" p-id="7813"></path>-->
          <!--              </svg>-->
          <!--            </div>-->
          <!--            <div class="stat-title font-bold text-xl text-black">高分<span class="text-base">(总分90%起)</span></div>-->
          <!--            <div class="stat-value " style="margin-bottom: 0.5rem; color:#05aefc; ">22份</div>-->
          <!--            <div class="stat-desc font-bold text-xl text-black">试卷</div>-->
          <!--          </div>-->
          <!--          -->
          <!--          <div class="stat">-->
          <!--            <div class="stat-figure text-secondary">-->
          <!--              <svg class="icon" height="32" p-id="8074" t="1710148225576"-->
          <!--                   version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg">-->
          <!--                <path-->
          <!--                    d="M512 97.52381c228.912762 0 414.47619 185.563429 414.47619 414.47619s-185.563429 414.47619-414.47619 414.47619S97.52381 740.912762 97.52381 512 283.087238 97.52381 512 97.52381z m-191.488 559.664761l41.642667 60.14781c109.372952-75.727238 190.317714-75.727238 299.690666 0l41.642667-60.14781c-134.436571-93.037714-248.539429-93.037714-382.976 0zM414.47619 341.333333a48.761905 48.761905 0 0 0-48.761904 48.761905v73.142857a48.761905 48.761905 0 1 0 97.523809 0v-73.142857a48.761905 48.761905 0 0 0-48.761905-48.761905z m195.04762 0a48.761905 48.761905 0 0 0-48.761905 48.761905v73.142857a48.761905 48.761905 0 1 0 97.523809 0v-73.142857a48.761905 48.761905 0 0 0-48.761904-48.761905z"-->
          <!--                    fill="#05aefc" p-id="8075"></path>-->
          <!--              </svg>-->
          <!--            -->
          <!--            </div>-->
          <!--            <div class="stat-title font-bold text-xl text-black">不及格<span class="text-base">(含无效卷)</span></div>-->
          <!--            <div class="stat-value" style="margin-bottom: 0.5rem; color:#05aefc; ">12份</div>-->
          <!--            <div class="stat-desc font-bold text-xl text-black">试卷</div>-->
          <!--          </div>-->
          <!--        -->
        </div>
        
        <div class="flex gap-x-8 justify-center my-8">
          <div class="btn bg-[#fff] hover:bg-secondary w-[200px] text-lg rounded-lg  btn-sm border-none "
               @click="$router.push('/')">返回首页
          </div>
          <div class="btn bg-[#fff] hover:bg-secondary w-[200px] text-lg rounded-lg  btn-sm border-none "
               @click="$router.go(-1)">继续阅卷
          </div>
        </div>
      
      </template>
    </n-result>
  </div>
</template>

<style lang="scss" scoped>
  .stats {
    margin-bottom: 1rem;
    
    .stat {
      display: flex;
      flex-direction: column;
      cursor: pointer;
      min-width: 200px;
      
      
      div {
        margin: 0.25rem 0 0.25rem 0;
      }
    }
    
    .stat:hover {
      background-color: #aee79f;
    }
  }


</style>
