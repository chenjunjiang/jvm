package com.chenjj.java;

/**
 * idea中在configurations里面的VM Options指定jvm参数(-Xmx32m),java命令通过java -Xmx32m 指定
 * 指定堆内存最大为32m
 * <p>
 * JVM在启动的时候会自动设置Heap size的值，初始空间(即-Xms)是物理内存的1/64，最大空间(-Xmx)是物理内存的1/4。
 * 所以可以根据自己的情况进行修改JVM的-Xmn -Xms -Xmx等选项。
 */
public class TestXmx {
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0, size = args.length; i < size; i++) {
                System.out.println("参数" + (i + 1) + ":" + args[i]);
            }
        }
        System.out.println("-Xmx" + Runtime.getRuntime().maxMemory() / 1000 / 1000 + "M");
    }
}
