import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {fileURLToPath, URL} from 'node:url';
import ElementPlus from 'unplugin-element-plus/vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), ElementPlus()],
  resolve: {
    alias: {
      //配置资源根别名，在引用资源时，用@/表示./src/
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    
    //反向代理
    proxy: {
      '/api': {
        target: 'http://localhost:8080/',
        changeOrigin: true,
        //rewrite决定如何处理代理路径
        //将请求路径中的'api'替换为''
        rewrite: (path) => path.replace(/^\/api/, '') //将不需要的'/api'去掉
      }
    }
    
  }
})
