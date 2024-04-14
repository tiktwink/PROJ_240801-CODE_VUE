package com.mybatisflex.demo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CallPythonScript {
  public static void main(String[] args) {
  
  }
  
  
  public static Boolean singleCheckOnPaper(String paperPath, String paperCheckPath) {
    String pythonScriptPath = "D:/Projects/python/test/s5.py"; // Python脚本路径

//    ProcessBuilder processBuilder = new ProcessBuilder("D:/CodeEnvironments/Python/python.exe", pythonScriptPath, paperPath, paperCheckPath);
    
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("D:/CodeEnvironments/paddle_env/paddle_env/python.exe", pythonScriptPath, paperPath, paperCheckPath);
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();
//      return true;
      
      //test
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      
      int k = process.waitFor();
      if (k == 0) {
        System.out.println("处理成功");
        return true;
      } else {
        System.out.println("处理失败");
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }


//    try {
//      String[] arg = new String[]{"D:/CodeEnvironments/Python/python.exe", "D:/Projects/python/test/testIO.py",paperPath,paperCheckPath};
//      Process proc = Runtime.getRuntime().exec(arg);
//      BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//      String line = null;
//      while ((line = in.readLine()) != null) {
//        System.out.println(line);
//      }
//      in.close();
//     int i = proc.waitFor();
//      System.out.println(i);
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
    
    return true;
  }
}
