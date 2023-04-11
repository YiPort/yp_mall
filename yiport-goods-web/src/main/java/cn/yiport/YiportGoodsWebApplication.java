package cn.yiport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class YiportGoodsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(YiportGoodsWebApplication.class,args);
    }
}
