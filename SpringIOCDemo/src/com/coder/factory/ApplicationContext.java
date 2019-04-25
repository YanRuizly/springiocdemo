package com.coder.factory;

public interface ApplicationContext {
    Object getBean(String name);
}