package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@SpringBootApplication
@EnableAsync
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        ThreadPoolTaskExecutor executor = context.getBean(DemoTask.TEST_THREAD_POOL_NAME, ThreadPoolTaskExecutor.class);
        log.info(executor.getThreadNamePrefix());
        Assert.assertEquals("线程名不正确", DemoTask.TEST_THREAD_PREFIX, executor.getThreadNamePrefix());

        context.getBean(DemoTask.class).printTask();

        log.info("finish");
    }

}
