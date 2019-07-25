package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoTask {

    /**
     * 线程池实例名称
     */
    static final String TEST_THREAD_POOL_NAME = "xiaoxuetu";

    /**
     * 线程前缀
     */
    static final String TEST_THREAD_PREFIX = "xiaoxuetu-";

    /**
     * 可以异步执行的任务
     */
    @Async(TEST_THREAD_POOL_NAME)
    public void printTask() {
        log.info("demo print async");
    }
}
