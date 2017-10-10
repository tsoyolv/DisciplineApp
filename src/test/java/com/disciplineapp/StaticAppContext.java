package com.disciplineapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * OLTS on 29.04.2017.
 */
public class StaticAppContext {

    private static ApplicationContext appContextFromXml =
            new ClassPathXmlApplicationContext("/testAppContext.xml");

    public static <T> T getBean(String beanName) {
        return (T) appContextFromXml.getBean(beanName);
    }
}
