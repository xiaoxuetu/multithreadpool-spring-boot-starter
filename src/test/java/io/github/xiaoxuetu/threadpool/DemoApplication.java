package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 开发过程中的测试入口
 */
@Slf4j
@SpringBootApplication
@EnableAsync
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        // 获取线程池
        ThreadPoolTaskExecutor executor = context.getBean(DemoTask.TEST_THREAD_POOL_NAME, ThreadPoolTaskExecutor.class);
        // 输出线程池前缀
        // 线程池前缀校验
        Assert.assertEquals("线程名不正确", DemoTask.TEST_THREAD_PREFIX, executor.getThreadNamePrefix());
        // 验证异步任务执行情况
        context.getBean(DemoTask.class).printTask();
        log.info(executor.getThreadNamePrefix());
        log.info("finish");
    }

}
