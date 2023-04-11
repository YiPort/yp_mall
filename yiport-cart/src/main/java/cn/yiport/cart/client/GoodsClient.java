package cn.yiport.cart.client;

import cn.yiport.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
