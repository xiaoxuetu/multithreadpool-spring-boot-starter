package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(MultiThreadPoolProperties.class)
public class MultiThreadPoolAutoConfigure {

    /**
     * 线程池缓存
     */
    private Map<String, ThreadPoolTaskExecutor> threadPoolTaskExecutorCache = new HashMap<>();

    /**
     * 线程池
     */
    private MultiThreadPoolProperties multiThreadPoolProperties;

    private ApplicationContext applicationContext;

    private final ObjectProvider<TaskDecorator> taskDecorator;

    public MultiThreadPoolAutoConfigure(MultiThreadPoolProperties multiThreadPoolProperties,
                                        ApplicationContext applicationContext,
                                        ObjectProvider<TaskDecorator> taskDecorator) {
        this.multiThreadPoolProperties = multiThreadPoolProperties;
        this.applicationContext = applicationContext;
        this.taskDecorator = taskDecorator;
    }

    @PostConstruct
    private void init() {
        for (Map.Entry<String, MultiThreadPoolProperties.ThreadPoolProperties> entry
                : multiThreadPoolProperties.getProperties().entrySet()) {
            String beanName = entry.getKey();
            MultiThreadPoolProperties.ThreadPoolProperties properties = entry.getValue();
            ThreadPoolTaskExecutor executor = ManualRegistBeanUtil
                    .registerBean((ConfigurableApplicationContext) applicationContext, beanName, ThreadPoolTaskExecutor.class);
            initExecutorProperties(executor, properties);
            threadPoolTaskExecutorCache.put(beanName, executor);
        }
    }

    private void initExecutorProperties(ThreadPoolTaskExecutor taskExecutor, MultiThreadPoolProperties.ThreadPoolProperties properties) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties::getQueueCapacity).to(taskExecutor::setQueueCapacity);
        map.from(properties::getCoreSize).to(taskExecutor::setCorePoolSize);
        map.from(properties::getMaxSize).to(taskExecutor::setMaxPoolSize);
        map.from(properties::getKeepAliveSeconds).asInt(Duration::getSeconds).to(taskExecutor::setKeepAliveSeconds);
        map.from(properties::isAllowCoreThreadTimeout).to(taskExecutor::setAllowCoreThreadTimeOut);
        map.from(properties::getBeanName).whenHasText().to(taskExecutor::setThreadNamePrefix);
        map.from(taskDecorator::getIfUnique).to(taskExecutor::setTaskDecorator);
        taskExecutor.initialize();
    }
}
