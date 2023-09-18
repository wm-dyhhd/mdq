package com.dyhhd.mdq.spring.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 动态注入 bean
 *
 * @author lv ning
 */
class ImportMdqRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(MdqListenerMethodProcessor.BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition(MdqListenerMethodProcessor.class);
            def.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(MdqListenerMethodProcessor.BEAN_NAME, def);
        }
    }
}
