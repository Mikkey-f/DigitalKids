package com.digital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.digital.mapper")
public class DigitalApplication {
    public static void main(String[] args){
        SpringApplication.run(DigitalApplication.class, args);
    }

}
