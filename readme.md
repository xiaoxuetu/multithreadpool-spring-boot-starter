# 背景
通过 `multithreadpool-spring-boot-starter`，我们可以动态根据配置文件进行多个线程池的初始化。

# 使用方法
1. 引入 `multithreadpool-spring-boot-starter` （todo: 待发布到maven仓库）

2. 在 `application.conf` 文件中按照以下格式进行配置:
```properties
# 实例名称配置有点重复，待优化
# 线程池名称，同时会作为线程池对前缀，默认值: default
spring.threadpools.properties.{实例名称}.bean-name=default

# 线程池关闭时是否等待任务完成，默认值: true
spring.threadpools.properties.{实例名称}.wait-for-tasks-to-complete-on-shutdown=true

# 线程池关闭等待时间，默认是30秒
spring.threadpools.properties.{实例名称}.await-termination-seconds=30s

# 核心线程数，默认是1
spring.threadpools.properties.{实例名称}.core-size=1

# 池子最大线程数，默认和cpu核心数一致
spring.threadpools.properties.{实例名称}.max-size=8

# 空闲等待时长，超过之后会自动关闭线程，默认10秒
spring.threadpools.properties.{实例名称}.keep-alive-seconds=10s 

# 是否允许核心线程空闲超时退出，默认是true
spring.threadpools.properties.{实例名称}.allow-core-thread-timeout=true

# 任务队列的最大值，默认是Integer.MAX_VALUE,即：2147483647
spring.threadpools.properties.{实例名称}.queue-capacity=1000
```
3. 可以通过以下方式使用：
    - 可以应用在 `@Asnyc` 等相关注解中，eg: `@Async("xiaoxuetu")`
    - 通过 `MultiThreadPoolManager.get` 获取，eg：
    ```java
    @Resource
    private MultiThreadPoolManager multiThreadPoolManager;
    multiThreadPoolManager.get("xiaoxuetu").execute(runnable);
   ```

