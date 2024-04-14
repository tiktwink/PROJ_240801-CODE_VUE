import axios from "axios";

const config = {
  // url: '/query/allnovels',
  
  method: 'get',
  
  baseURL: '/api/query/allnovels', // baseURL将自动加在url前面，除非url是一个绝对URL
  
  headers: {'X-Requested-With': 'XMLHttpRequest'}, // headers是自定义请求头
  
  params: { // params是与请求一起发送的URL参数，必须是简单对象或URLSearchParams对象
    // ID: 12345
    username: 1001
  },
  
  data: { // data是作为请求体被发送的数据,仅适用 'PUT', 'POST', 'DELETE 和 'PATCH' 请求方法
    // firstName: 'Fred'
  },
  
  timeout: 1000,


//其它选项(见官方文档)
}

//导出实例
export const novelAuthor = axios.create(
    config
)
