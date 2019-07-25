package io.github.xiaoxuetu.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程池配置
 */
@Data
@ConfigurationProperties(prefix = "spring.threadpools")
public class MultiThreadPoolProperties {

    /**
     * 默认线程池名称
     */
    public static final String DEFAULT_THREAD_POOL_NAME = "default";

    /**
     * 线程池配置集合
     */
    private Map<String, ThreadPoolProperties> properties = new HashMap<>();

    /**
     * 线程池配置类
     */
    @Data
    static class ThreadPoolProperties {

        /**
         * 线程池名称，同时会作为线程池对前缀
         */
        private String beanName = DEFAULT_THREAD_POOL_NAME;

        /**
         * 线程池ShutDown时，是否等待任务完成
         *
         * 默认值: @see java.lang.Boolean#TRUE, 表示需要等待任务完成再关闭
         */
        private boolean waitForTasksToCompleteOnShutdown = Boolean.TRUE;

        /**
         * 线程池关闭等待时间
         *
         * 默认值: 30，表示等待任务完成时长为30秒
         */
        private Duration awaitTerminationSeconds = Duration.ofSeconds(30L);

        /**
         * 核心线程数
         *
         * 默认值: 1，表示该线程池最少保留1个线程
         */
        private int coreSize = 1;

        /**
         * 池子最大线程数
         *
         * 默认值为CPU核心数量
         */
        private int maxSize = Runtime.getRuntime().availableProcessors();

        /**
         * 线程空转保留时长
         *
         * 默认值: 60，表示线程空转时长为60秒
         */
        private Duration keepAliveSeconds = Duration.ofSeconds(60L);

        /**
         * 当线程空转超过保留时长时，是否允许释放
         *
         * 默认值：true，表示释放
         */
        private boolean allowCoreThreadTimeout = Boolean.TRUE;

        /**
         * 队列最大数量
         *
         * 默认值: @see java.lang.Interger#MAX_VALUE
         */
        private int queueCapacity = Integer.MAX_VALUE;
    }
}
