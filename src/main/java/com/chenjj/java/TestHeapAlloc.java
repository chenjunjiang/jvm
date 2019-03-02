package com.chenjj.java;

/**
 * -Xmx20m -Xms5m -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseSerialGC
 * -XX:InitialHeapSize=5242880 -XX:MaxHeapSize=20971520 -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseSerialGC
 * maxMemory=
 * 20316160 bytes
 * free mem=
 * 4835088 bytes
 * total mem=
 * 6094848 bytes
 * [GC (Allocation Failure) [DefNew: 1230K->192K(1856K), 0.0007503 secs] 1230K->359K(5952K), 0.0007647 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * 分配了1M空间给数组
 * maxMemory=
 * 20316160 bytes
 * free mem=
 * 4645856 bytes
 * total mem=
 * 6094848 bytes
 * [GC (Allocation Failure) [DefNew: 1247K->0K(1856K), 0.0007189 secs][Tenured: 1383K->1383K(4096K), 0.0009872 secs] 1415K->1383K(5952K), [Metaspace: 2845K->2845K(1056768K)], 0.0017452 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 * 分配了4M空间给数组
 * maxMemory=
 * 20316160 bytes
 * free mem=
 * 4676040 bytes
 * total mem=
 * 10358784 bytes
 * Heap
 * def new generation   total 1920K, used 131K [0x00000000fec00000, 0x00000000fee10000, 0x00000000ff2a0000)
 * eden space 1728K,   7% used [0x00000000fec00000, 0x00000000fec20ca0, 0x00000000fedb0000)
 * from space 192K,   0% used [0x00000000fedb0000, 0x00000000fedb0000, 0x00000000fede0000)
 * to   space 192K,   0% used [0x00000000fede0000, 0x00000000fede0000, 0x00000000fee10000)
 * tenured generation   total 8196K, used 5479K [0x00000000ff2a0000, 0x00000000ffaa1000, 0x0000000100000000)
 * the space 8196K,  66% used [0x00000000ff2a0000, 0x00000000ff7f9f48, 0x00000000ff7fa000, 0x00000000ffaa1000)
 * Metaspace       used 2922K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 322K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * 在实际工作中，也可以直接将初始化堆-Xms与最大堆-Xmx设置相等，这样的好处是可以减少程序运行时进行的垃圾回收次数，从而提高程序的性能。
 */
public class TestHeapAlloc {
    public static void main(String[] args) {
        System.out.println("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory() + " bytes");
        System.out.println("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory() + " bytes");
        System.out.println("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory() + " bytes");

        byte[] bytes = new byte[1 * 1024 * 1024];
        System.out.println("分配了1M空间给数组");

        System.out.println("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory() + " bytes");
        System.out.println("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory() + " bytes");
        System.out.println("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory() + " bytes");

        bytes = new byte[4 * 1024 * 1024];
        System.out.println("分配了4M空间给数组");

        System.out.println("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory() + " bytes");
        System.out.println("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory() + " bytes");
        System.out.println("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory() + " bytes");
    }
}
