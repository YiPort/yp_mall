package cn.yiport.search.client;


import cn.yiport.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
