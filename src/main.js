import {createApp} from 'vue'
import './styles/style.css'
import App from './App.vue'
import router from './router';
import pinia from './stores'
import {QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import {Anchor as TAnchor, AnchorItem as TAnchorItem} from 'tdesign-vue-next';
import DividerHorizontal from "@/components/utility/DividerHorizontal.vue";

//引入TDesign组件库的少量全局样式变量
import 'tdesign-vue-next/es/style/index.css';

//primevue组件
import PrimeVue from "primevue/config";
import 'primevue/resources/themes/lara-light-blue/theme.css'
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';
import Dialog from 'primevue/dialog';


//naiveUI组件


// 实例化应用
const app = createApp(App)

//路由、仓库
app.use(router)
app.use(pinia)

//TDesign组件
app.use(TAnchor)
app.use(TAnchorItem)

//其它组件
app.component('QuillEditor', QuillEditor)
app.component('DividerHorizontal', DividerHorizontal)

//primevue插件、组件
app.use(PrimeVue)
app.use(ToastService);
app.component('Toast', Toast)
app.component('Dialog', Dialog)

// 应用挂载
app.mount('#app')

//*****

