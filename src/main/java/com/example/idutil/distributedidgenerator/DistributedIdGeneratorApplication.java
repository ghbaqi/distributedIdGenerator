package com.example.idutil.distributedidgenerator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.example.idutil.distributedidgenerator.mapper")
@SpringBootApplication
public class DistributedIdGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedIdGeneratorApplication.class, args);
    }

}
