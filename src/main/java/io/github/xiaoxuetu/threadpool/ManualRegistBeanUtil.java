package io.github.xiaoxuetu.threadpool;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Bean注册工具
 */
public class ManualRegistBeanUtil {

    /**
     * 主动向Spring容器中注册bean
     *
     * @param applicationContext Spring容器
     * @param name               BeanName
     * @param clazz              注册的bean的类性
     * @param args               构造方法的必要参数，顺序和类型要求和clazz中定义的一致
     * @param <T>                需要注册的类型
     * @return 返回注册到容器中的bean对象
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, Object... args) {
        // bean存在，则直接返回现有的对象
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("Duplicate bean named '" + name + "'.");
            }
        }

        // 定义并注册bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }
}
