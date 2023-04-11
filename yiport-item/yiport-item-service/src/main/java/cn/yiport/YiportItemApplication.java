package cn.yiport;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.yiport.item.mapper")
public class YiportItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiportItemApplication.class,args);
    }
}
