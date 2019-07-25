package io.github.xiaoxuetu.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(MultiThreadPoolProperties.class)
public class MultiThreadPoolAutoConfigure {

    /**
     * 线程池属性
     */
    private MultiThreadPoolProperties multiThreadPoolProperties;

    /**
     * 应用上线文
     */
    private ApplicationContext applicationContext;

    /**
     * 任务修饰器
     */
    private final ObjectProvider<TaskDecorator> taskDecorator;

    /**
     * 线程池缓存
     */
    @Resource
    private MultiThreadPoolManager multiThreadPoolManager;

    public MultiThreadPoolAutoConfigure(MultiThreadPoolProperties multiThreadPoolProperties,
                                        ApplicationContext applicationContext,
                                        ObjectProvider<TaskDecorator> taskDecorator) {
        this.multiThreadPoolProperties = multiThreadPoolProperties;
        this.applicationContext = applicationContext;
        this.taskDecorator = taskDecorator;;
    }

    @PostConstruct
    public void init() {
        for (Map.Entry<String, MultiThreadPoolProperties.ThreadPoolProperties> entry
                : multiThreadPoolProperties.getProperties().entrySet()) {
            String beanName = entry.getKey();
            MultiThreadPoolProperties.ThreadPoolProperties properties = entry.getValue();

            multiThreadPoolManager.createThreadPoolTaskExecutor((ConfigurableApplicationContext) applicationContext
                    , beanName, properties, taskDecorator.getIfUnique());
        }
    }


    @Bean
    @ConditionalOnMissingBean
    public MultiThreadPoolManager multiThreadPoolManager() {
        return new MultiThreadPoolManager();
    }
}
