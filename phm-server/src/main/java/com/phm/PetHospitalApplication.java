package com.phm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetHospitalApplication {
    private static final Logger log = LoggerFactory.getLogger(PetHospitalApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PetHospitalApplication.class, args);
        log.info("宠物医院管理系统启动");
        log.info("http://localhost:8085");
    }
}
