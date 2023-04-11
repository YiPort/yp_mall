package cn.yiport.goods.listener;

import cn.yiport.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yiport.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "yiport.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void listenCreate(Long id) throws Exception {
        if (id == null) {
            return;
        }
        // 创建页面
        goodsHtmlService.createHtml(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yiport.delete.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "yiport.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void listenDelete(Long id) {
        if (id == null) {
            return;
        }
        // 删除页面
        goodsHtmlService.deleteHtml(id);
    }
}