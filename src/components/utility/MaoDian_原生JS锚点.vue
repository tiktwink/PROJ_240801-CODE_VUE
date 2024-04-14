<template>
  <!--原生JS方式-->
  <div>
    <ul>
      <li><a href="#section1" @click="scrollToAnchor">Section 1</a></li>
      <li><a href="#section2" @click="scrollToAnchor">Section 2</a></li>
      <li><a href="#section3" @click="scrollToAnchor">Section 3</a></li>
    </ul>
    
    <div id="section1">Section 1</div>
    <div id="section2">Section 2</div>
    <div id="section3">Section 3</div>
  </div>
</template>

<script>
  export default {
    methods: {
      scrollToAnchor(event) {
        const href = event.target.getAttribute('href');
        
        //非定制滚动行为
        // window.location.hash = href
        
        //定制滚动行为（滚动位置）
        const targetElement = document.querySelector(href);
        window.scrollTo({top: targetElement.offsetTop, behavior: 'smooth'});
      },
    },
  };
</script>

