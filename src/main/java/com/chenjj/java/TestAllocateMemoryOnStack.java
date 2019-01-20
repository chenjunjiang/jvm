package com.chenjj.java;

/**
 * jvm参数：-server -Xmx10m -Xms10m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:-UseTLAB -XX:+EliminateAllocations
 * <p>
 * 一、标量替换
 * 1.标量和聚合量
 * 标量即不可被进一步分解的量，而JAVA的基本数据类型就是标量（如：int，long等基本数据类型以及reference类型等），标量的对立就是可以被进一步分解的量，
 * 而这种量称之为聚合量。而在JAVA中对象就是可以被进一步分解的聚合量。
 * 2.替换过程
 * 通过逃逸分析确定该对象不会被外部访问，并且对象可以被进一步分解时，JVM不会创建该对象，而会将该对象成员变量分解若干个被这个方法使用的成员变量所代替。
 * 这些代替的成员变量在栈帧或寄存器上分配空间。
 * 通过-XX:+EliminateAllocations可以开启标量替换(默认开启)， -XX:+PrintEliminateAllocations查看标量替换情况（Server VM 非Product版本支持）
 * <p>
 * 二 、栈上分配
 * 我们通过JVM内存分配可以知道JAVA中的对象都是在堆上进行分配，当对象没有被引用的时候，需要依靠GC进行回收内存，如果对象数量较多的时候，
 * 会给GC带来较大压力，也间接影响了应用的性能。为了减少临时对象在堆内分配的数量，JVM通过逃逸分析确定该对象不会被外部访问。
 * 那就通过标量替换将该对象分解在栈上分配内存，这样该对象所占用的内存空间就可以随栈帧出栈而销毁，就减轻了垃圾回收的压力。
 * 通过-XX:-DoEscapeAnalysis关闭逃逸分析，在JDK1.8是默认开启逃逸分析。
 * <p>
 * DoEscapeAnalysis和EliminateAllocations必须同时开启才能在栈上分配。
 * <p>
 * 如果下面的程序没有开启DoEscapeAnalysis或EliminateAllocations，就会打印大量的GC信息。
 * <p>
 * https://www.jianshu.com/p/580f17760f6e
 */
public class TestAllocateMemoryOnStack {
    public static class User {
        public int id = 0;
        public String name = "";
    }

    public static void alloc() {
        User user = new User();
        user.id = 5;
        user.name = "zhangsan";
    }

    public static void main(String[] args) {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            alloc();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);
    }
}
