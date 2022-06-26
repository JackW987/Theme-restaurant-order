package com.wjicloud.simpson.controller;

import com.wjicloud.simpson.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

//        try {
//            file.transferTo(new File("E:\simpsonImg\"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println(file);
        return R.success("上传成功");
    }
}
