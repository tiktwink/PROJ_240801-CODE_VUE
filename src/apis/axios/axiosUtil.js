import useUserStore from "@/stores/useUserStore.js";
import axios from "axios";

const config = {
  headers: {
    // "Content-Type": "application/json",
    Authorization: useUserStore().token || '',
  }
}

export const axiosUtil = axios.create(config)

/*
  使用方式：localhost:8080会依据vite.config.js中的proxy代理规则自动添加到首部
  axiosJson.get('/api/paper/index?id=9001')
*/
