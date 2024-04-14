<template>
  <!--导航守卫-->
  <div>
    <router-link to="/editor">editor</router-link>
    <ul>
      <li>
        <router-link to="#section1">Section 1</router-link>
      </li>
      <li>
        <router-link to="#section2">Section 2</router-link>
      </li>
      <li>
        <router-link to="#section3">Section 3</router-link>
      </li>
    </ul>
    
    <div id="section1">Section 1</div>
    <div id="section2">Section 2</div>
    <div id="section3">Section 3</div>
  </div>
</template>
<style>
  #section1, #section2, #section3 {
    height: 600px;
    width: 600px;
    border: 1px solid red;
    margin: 5px;
  }
</style>

<script>
  import {useRouter} from 'vue-router';
  
  export default {
    setup() {
      const router = useRouter();
      
      router.beforeEach((to, from) => {
        if (to.hash) {
          console.log(to.hash)
          const element = document.querySelector(to.hash);
          if (element) {
            element.scrollIntoView({behavior: 'smooth'});
          }
        }
      });
    },
  };
</script>

