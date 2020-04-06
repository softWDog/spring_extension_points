package com.gethin.extension.example3;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/6 1:00
 * @description:
 */
public class MapperBean implements BeanFactoryPostProcessor {
    private String package2Scan;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        MapperScanner mapperScanner=new MapperScanner((BeanDefinitionRegistry) configurableListableBeanFactory);
        mapperScanner.doScan(package2Scan);
    }
    public void setPackage2Scan(String package2Scan) {
        this.package2Scan = package2Scan;
    }
}
