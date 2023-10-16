package com.dyhhd.mdq.spring.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 动态注入 bean
 *
 * @author lv ning
 */
class ImportMdqRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableMdq.class.getName()));

        if (null == attributes) {
            throw new IllegalArgumentException(
                    "@EnableMdq is not present on importing class " + importMetadata.getClassName());
        }

        if (!registry.containsBeanDefinition(MdqAutoBeanFactoryPostProcessor.BEAN_NAME)) {
            boolean autoAck = attributes.getBoolean("autoAck");
            int retryTotal = attributes.getNumber("retryTotal");
            RootBeanDefinition def = new RootBeanDefinition(MdqAutoBeanFactoryPostProcessor.class);

            // set
            def.getPropertyValues().add("autoAck", autoAck);
            def.getPropertyValues().add("retryTotal", retryTotal);

            def.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(MdqAutoBeanFactoryPostProcessor.BEAN_NAME, def);
        }
    }
}
