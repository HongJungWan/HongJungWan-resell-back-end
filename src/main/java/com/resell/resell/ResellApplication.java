package com.resell.resell;

import com.resell.resell.common.properties.AppProperties;
import com.resell.resell.common.properties.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {AppProperties.class, AwsProperties.class})
public class ResellApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResellApplication.class, args);
    }
}
