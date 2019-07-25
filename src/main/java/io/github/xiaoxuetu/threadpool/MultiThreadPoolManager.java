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

/**
 * 线程池管理器
 */
@Slf4j
public class MultiThreadPoolManager extends HashMap<String, ThreadPoolTaskExecutor> {

    /**
     * 创建线程池
     * @param applicationContext 应用上下文
     * @param beanName           线程池实例名称，同时也是线程池前缀
     * @param properties         线程池配置
     * @param taskDecorator      任务修饰器
     */
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

    /**
     * 调整线程池配置
     * @param taskExecutor   线程池
     * @param properties     线程池配置
     * @param taskDecorator  任务修饰器
     */
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

    /**
     * 应用退出时，清空管理器中的缓存
     */
    @PreDestroy
    public void destroy() {
        log.info("Destroy multi thread pools...");
        clear();
    }
}
