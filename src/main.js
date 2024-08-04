//naiveUI组件
import {NCheckbox, NRadio, NScrollbar, NSelect, NTooltip} from "naive-ui";


//primevue组件
import PrimeVue from "primevue/config";
import 'primevue/resources/themes/lara-light-blue/theme.css'
import Dialog from 'primevue/dialog';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';
import {createApp} from 'vue'
import './styles/style.scss'
import App from './App.vue'
import router from './router';
import pinia from './stores'

// 实例化应用
const app = createApp(App)

//路由、仓库
app.use(router)
app.use(pinia)

//其它组件
app.component('NScrollbar', NScrollbar)
app.component('NTooltip', NTooltip)
app.component('NCheckbox', NCheckbox)
app.component('NRatio', NRadio)
app.component('NSelect', NSelect)

//primevue插件、组件
app.use(PrimeVue)
app.use(ToastService);
app.component('Toast', Toast)
app.component('Dialog', Dialog)

// 应用挂载
app.mount('#app')

//*****

