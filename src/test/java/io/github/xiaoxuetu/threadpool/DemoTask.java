package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoTask {

    public static final String TEST_THREAD_POOL_NAME = "xiaoxuetu";
    public static final String TEST_THREAD_PREFIX = "xiaoxuetu-";

    @Async(TEST_THREAD_POOL_NAME)
    public void printTask() {
        log.info("demo print async");
    }
}
