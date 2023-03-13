package com.riverify.singleton;

import java.io.Serial;
import java.io.Serializable;

/**
 * FileName: Singleton1.java
 * Date: 2023/3/13
 * Time: 14:59
 * Author: river
 */

// 饿汉式单例
public class Singleton1 implements Serializable {
    // 私有化静态实例
    private static final Singleton1 INSTANCE  = new Singleton1();

    // 私有化构造方法
    private Singleton1() {
        if (INSTANCE != null) {
            throw new RuntimeException("单例模式禁止反射调用构造方法");
        }
        System.out.println("Singleton1");
    }

    // 提供静态方法获取实例
    public static Singleton1 getInstance() {
        return INSTANCE;
    }

    public static void otherMethod() {
        System.out.println("otherMethod");
    }

    // 防止反序列化破坏单例
    @Serial
    private Object readResolve() {
        return INSTANCE;
    }
}
