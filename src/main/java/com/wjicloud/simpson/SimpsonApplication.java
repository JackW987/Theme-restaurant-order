package com.wjicloud.simpson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class SimpsonApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpsonApplication.class,args);
        log.info("run the application");
    }
}
