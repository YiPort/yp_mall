package cn.yiport.sms.ye.rly.listener;

import cn.yiport.sms.ye.rly.utils.RlySmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class RlySmsListener {

    @Autowired
    private RlySmsUtils rlySmsUtils;

    final String validateTime="10"; //信息有效时间,与yiport-user微服务下的redis一样默认设置为5,单位分钟

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yiport.sms.queue", durable = "true"),
            exchange = @Exchange(value = "yiport.sms.exchange",
                                 ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg) throws Exception {
        if (msg == null || msg.size() <= 0) {
            // 放弃处理
            return ;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            // 放弃处理
            return ;
        }
        // 发送消息
        this.rlySmsUtils.smsSend(phone,code,validateTime);

        
    }
}