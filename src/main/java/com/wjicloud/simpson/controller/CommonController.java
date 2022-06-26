package com.wjicloud.simpson.controller;

import com.wjicloud.simpson.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${simpson.path}")
    private String basePath;
    /**
     * 图片上传响应
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 使用uuid作为文件名，防止重名
        String fileName = UUID.randomUUID().toString();
        // 截取图片格式
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 如果保存地址没有目录，就创建一个
        File dir = new File(basePath);
        if(!dir.exists()){
            // 目录不存在，创建
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath+fileName+suffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName+suffix);
    }

    /**
     * 图片下载响应
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            // 输入流读取指定位置文件
            FileInputStream inputStream = new FileInputStream(new File(basePath+name));
            // 因为要写回数据要页面，所以需要从response中调用输出流
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置写回的数据
            response.setContentType("image/jpeg");
            // 准备字节桶和读取数据计数
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = inputStream.read(bytes))!=-1){
                // 读到多少就写出多少
                outputStream.write(bytes,0,len);
                // 输出流刷新
                outputStream.flush();
            }
            // 关闭输入流和输出流
            inputStream.close();
            outputStream.close();
            // 输出流进行输出
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
