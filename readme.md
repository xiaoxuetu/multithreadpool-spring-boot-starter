# 背景
通过 `multithreadpool-spring-boot-starter`，我们可以动态根据配置文件进行多个线程池的初始化。

# 使用方法
1. 引入 `multithreadpool-spring-boot-starter` 
```xml
<dependency>
  <groupId>io.github.xiaoxuetu</groupId>
  <artifactId>multithreadpool-spring-boot-starter</artifactId>
  <version>0.0.1</version>
</dependency>
```

2. 在 `application.properties` 文件中按照以下格式进行配置:
    - 最简单的使用方式:
    ```properties
    # 线程池名称，同时会作为线程池对前缀，默认值: default
    spring.threadpools.executors[0].bean-name=default
    ```
    - 目前还支持以下线程池配置:
    ```properties
    # 线程池关闭时是否等待任务完成，默认值: true
    spring.threadpools.executors[0].wait-for-tasks-to-complete-on-shutdown=true
    
    # 线程池关闭等待时间，默认是30秒
    spring.threadpools.executors[0].await-termination-seconds=30s
    
    # 核心线程数，默认是1
    spring.threadpools.executors[0].core-size=1
    
    # 池子最大线程数，默认和cpu核心数一致
    spring.threadpools.executors[0].max-size=8
    
    # 空闲等待时长，超过之后会自动关闭线程，默认10秒
    spring.threadpools.executors[0].keep-alive-seconds=10s 
    
    # 是否允许核心线程空闲超时退出，默认是true
    spring.threadpools.executors[0].allow-core-thread-timeout=true
    
    # 任务队列的最大值，默认是Integer.MAX_VALUE,即：2147483647
    spring.threadpools.executors[0].queue-capacity=1000
    ```
3. 可以通过以下方式使用：
    - 可以应用在 `@Asnyc` 等相关注解中，eg: `@Async("xiaoxuetu")`
    - 通过 `MultiThreadPoolManager.get` 获取，eg：
    ```java
    @Resource
    private MultiThreadPoolManager multiThreadPoolManager;
    multiThreadPoolManager.get("xiaoxuetu").execute(runnable);
   ```

