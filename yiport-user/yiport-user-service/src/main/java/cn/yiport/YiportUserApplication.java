package cn.yiport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.yiport.user.mapper")
public class YiportUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiportUserApplication.class, args);
    }
}