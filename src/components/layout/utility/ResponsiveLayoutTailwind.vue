<template>
  <div :class="{ 'wide-with-side':wideWithSide, 'narrow-with-side':narrowWithSide }"
       class="test-wrapper ">
    
    <div class="test-main-wrapper flex flex-col items-center justify-between"
         style="height:calc(100vh - 100px - 0.125rem);">
      
      <div class="test-main flex justify-center items-center "
           style="border:#ccc dashed 1px; ">
        <button v-show="mode==='wide'" class="btn border-round-lg text-lg  hover:bg-info border-none bg-primary"
                style="padding:0.5rem 1rem; min-height:40px; margin-bottom: 200px;"
                @click="wideWithSide=!wideWithSide">点击开关侧栏
        </button>
        <button v-show="mode!=='wide'" class="btn border-round-lg text-lg  hover:bg-info border-none bg-primary"
                style="padding:0.5rem 1rem; min-height:40px; margin-bottom: 200px;"
                @click="narrowWithSide=!narrowWithSide">点击开关侧栏
        </button>
      </div>
    </div>
    
    <div class="test-rside fixed right-0"></div>
    
    <div class="test-mask" @click="handleMaskTap"></div>
  </div>
</template>

<script>
  export default {
    
    data() {
      return {
        wideWithSide: true,
        narrowWithSide: false,
        mode: 'wide'
      }
    },
    mounted() {
      if (screen.width >= 991) {
        this.mode = 'wide'
        this.wideWithSide = true
        this.narrowWithSide = false
      } else {
        this.mode = 'narrow'
        this.wideWithSide = false
        this.narrowWithSide = false
      }
    },
    methods: {
      handleMaskTap() {
        if (this.mode === 'wide') {
          this.wideWithSide = !this.wideWithSide
        } else {
          this.narrowWithSide = !this.narrowWithSide
        }
      }
    },
    
  }
</script>

<style lang="scss" scoped>
  .test-wrapper {
    min-height: 50vh; /*optional, none-required*/
    position: relative;
    
    .test-main-wrapper {
      margin-top: 10px;
      margin-left: 10px;
      margin-bottom: 0;
      margin-right: 10px;
      min-height: 600px;
      border: 1px blue solid;
      transition: margin-right 0.5s;
      
      .test-main {
        margin: 1rem;
        padding: 1rem;
      }
    }
    
    .test-rside {
      position: fixed;
      top: 100px;
      right: 0;
      border: 1px red solid;
      width: 220px;
      min-height: calc(100vh - 100px - 0.125rem);
      
    }
    
    .test-mask {
      background-color: rgba(189, 186, 186, 0.7);
      
      position: fixed;
      top: 0;
      left: 0;
    }
  }
  
  @media screen and (min-width: 992px) {
    .test-wrapper {
      &.wide-with-side {
        .test-main-wrapper {
          margin-right: 240px;
        }
        
        .test-rside {
          right: 10px;
          transition: right 0.8s;
          
        }
      }
      
      .test-main-wrapper {
        margin-right: 10px;
      }
      
      .test-rside {
        /*transform : translateX(100%);*/
        right: -240px;
        transition: right 0.5s;
      }
      
      .test-mask {
        display: none !important;
      }
    }
  }
  
  @media screen and (max-width: 991px) {
    .test-wrapper {
      &.narrow-with-side {
        .test-rside {
          transform: translateX(0);
          z-index: 999;
        }
        
        .test-mask {
          width: 100vw !important;
          height: 100vh !important;
          z-index: 998 !important;
        }
        
      }
      
      .test-rside {
        transform: translateX(100%);
      }
      
    }
    
  }
</style>
