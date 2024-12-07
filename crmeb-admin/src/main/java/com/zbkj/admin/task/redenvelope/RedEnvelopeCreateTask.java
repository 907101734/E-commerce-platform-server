package com.zbkj.admin.task.redenvelope;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.UserRedEnvelopeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RedEnvelopeCreateTask
 * @author wtj
 * @date 2024/11/09
 */
@Component
@Configuration //读取配置
@EnableScheduling // 2.开启定时任务
public class RedEnvelopeCreateTask {

    private static final Logger logger = LoggerFactory.getLogger(RedEnvelopeCreateTask.class);

    @Autowired
    private UserRedEnvelopeRecordService userRedEnvelopeRecordService;

    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点红包生成红包
    public void create() {
        logger.info("---RedEnvelopeCreateTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            logger.info("开始生成每日红包");
            userRedEnvelopeRecordService.autoCreateRedEnv();
            logger.info("结束生成每日红包");
        } catch (Exception e) {
            logger.error("生成红包失败");
            e.printStackTrace();
        }
    }
}
