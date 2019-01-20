package com.chenjj.java;

import java.util.HashMap;

/**
 * 方法区是一块所有线程共享的内存区域，它用于保存系统的类信息，比如：类的字段、方法、常量池等。
 * 在JDK1.6、JDK1.7中，方法区可以理解为永久区。默认情况下，-XX:MaxPermSize为64MB。如果系统使用了一些动态代理，那么有可能会在运行时生成大量的类。
 * 在JDK1.8中，永久区已经被彻底移除。取而代之的是元数据区，元数据区大小可以使用-XX:MaxMetaspaceSize指定，这是一块堆外的直接内存。如果不指定大小，
 * 默认虚拟机会耗尽所有的可用系统内存。
 * <p>
 * java方法区是存在GC的
 * 方法区即为永久代，主要回收两部分内容：废弃常量和无用类。
 * <p>
 * 满足以下3个条件的类称之为无用类
 * <p>
 * 1、该类所所有的对象实例已经被回收，也就是java堆中不存在该类的任何实例
 * <p>
 * 2、加载该类的ClassLoader已经被回收
 * <p>
 * 3、该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。
 * <p>
 * 在大量使用反射、动态代理、CGLib等ByteCode框架、动态生成JSP以及OSGI这类频繁自定义ClassLoader的场景都需要虚拟机具备类卸载的功能，
 * 以保证永久带不会溢出。
 */
public class TestMetaspaceSize {
    public static void main(String[] args) {
        int i = 0;
        try {
            for (i = 0; i < 100000; i++) {
                CglibBean cglibBean = new CglibBean("com.chenjj.java" + i, new HashMap());
            }
        } catch (Exception e) {
            System.out.println("total create count:" + i);
            e.printStackTrace();// Caused by: java.lang.OutOfMemoryError: Metaspace
        }
    }
}
