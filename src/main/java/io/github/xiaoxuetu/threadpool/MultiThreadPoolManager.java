package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.HashMap;

@Slf4j
public class MultiThreadPoolManager extends HashMap<String, ThreadPoolTaskExecutor> {

    public void createThreadPoolTaskExecutor(ConfigurableApplicationContext applicationContext,
                                             String beanName,
                                             MultiThreadPoolProperties.ThreadPoolProperties properties,
                                             TaskDecorator taskDecorator) {
        if (containsKey(beanName)) {
            log.info("Threadpool '{}' was exists...");
            return;
        }

        ThreadPoolTaskExecutor executor = ManualRegistBeanUtil
                .registerBean(applicationContext, beanName, ThreadPoolTaskExecutor.class);
        initExecutorProperties(executor, properties, taskDecorator);
        put(beanName, executor);
    }

    private void initExecutorProperties(ThreadPoolTaskExecutor taskExecutor,
                                        MultiThreadPoolProperties.ThreadPoolProperties properties,
                                        TaskDecorator taskDecorator) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties::getQueueCapacity).to(taskExecutor::setQueueCapacity);
        map.from(properties::getCoreSize).to(taskExecutor::setCorePoolSize);
        map.from(properties::getMaxSize).to(taskExecutor::setMaxPoolSize);
        map.from(properties::getKeepAliveSeconds).asInt(Duration::getSeconds).to(taskExecutor::setKeepAliveSeconds);
        map.from(properties::isAllowCoreThreadTimeout).to(taskExecutor::setAllowCoreThreadTimeOut);

        String prefix = properties.getBeanName();
        if (!StringUtils.isEmpty(prefix) && !prefix.endsWith("-")) {
            prefix = prefix + "-";
        }
        taskExecutor.setThreadNamePrefix(prefix);
        taskExecutor.setTaskDecorator(taskDecorator);
        taskExecutor.initialize();
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroy multi thread pools...");
        clear();
    }
}
