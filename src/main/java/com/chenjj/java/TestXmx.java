package com.chenjj.java;

/**
 * idea中在configurations里面的VM Options指定jvm参数(-Xmx32m),java命令通过java -Xmx32m 指定
 * 指定堆内存最大为32m
 */
public class TestXmx {
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            for (int i = 0, size = args.length; i < size; i++) {
                System.out.println("参数" + (i+1) + ":" + args[i]);
            }
        }
        System.out.println("-Xmx" + Runtime.getRuntime().maxMemory() / 1000 / 1000 + "M");
    }
}
