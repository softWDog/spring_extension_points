package com.gethin.extension.example3;

import com.gethin.extension.example3.annotation.Mapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/6 1:06
 * @description: 扫描包并进行Bean的注册
 */
public class MapperScanner extends ClassPathBeanDefinitionScanner {

    public MapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
        addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> mapperBeanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder mapperBeanDefinitionHolder : mapperBeanDefinitionHolders) {
            GenericBeanDefinition mapperBeanDefinition= (GenericBeanDefinition) mapperBeanDefinitionHolder.getBeanDefinition();
            MutablePropertyValues propertyValues = mapperBeanDefinition.getPropertyValues();
            propertyValues.add("mapperInterface", mapperBeanDefinition.getBeanClassName());
            mapperBeanDefinition.setBeanClass(MapperFactoryBean.class);
        }
        return mapperBeanDefinitionHolders;
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isInterface() && metadata.isIndependent());
    }
}
