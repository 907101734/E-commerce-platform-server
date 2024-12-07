package com.zbkj.admin.task.brokerage;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * BrokerageTransferInTask 佣金自动转余额
 * @author wtj
 * @date 2024/11/23
 */
@Component
@Configuration //读取配置
@EnableScheduling // 2.开启定时任务
public class BrokerageTransferInTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(BrokerageTransferInTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    @Scheduled(fixedDelay = 1000 * 60L) //1分钟同步一次数据
    public void init() {
        logger.info("---BrokerageTransferInTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.autoTransferIn();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BrokerageTransferInTask.task" + " | msg : " + e.getMessage());
        }

    }
}
