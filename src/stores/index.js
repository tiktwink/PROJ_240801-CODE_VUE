import {createPinia} from "pinia" //引入pinia实例创建方法
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate' //持久化插件

const pinia = createPinia() //实例化pinia

pinia.use(piniaPluginPersistedstate)

export default pinia //导出pinia
