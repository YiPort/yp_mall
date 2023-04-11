package cn.yiport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class YiportUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiportUploadApplication.class,args);
    }
}
