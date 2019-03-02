package com.chenjj.java;

/**
 * vm options:-Xms10m -Xmx10m -Xmn4m -XX:+PrintGCDetails
 * GC (Allocation Failure) [PSYoungGen: 1414K->496K(3584K)] 5510K->4600K(9728K), 0.0010433 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) --[PSYoungGen: 2544K->2544K(3584K)] 6648K->6648K(9728K), 0.0010839 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Ergonomics) [PSYoungGen: 2544K->2431K(3584K)] [ParOldGen: 4104K->4097K(6144K)] 6648K->6528K(9728K), [Metaspace: 3003K->3003K(1056768K)], 0.0032787 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
 * [GC (Allocation Failure) --[PSYoungGen: 2431K->2431K(3584K)] 6528K->6528K(9728K), 0.0010329 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 2431K->2413K(3584K)] [ParOldGen: 4097K->4097K(6144K)] 6528K->6511K(9728K), [Metaspace: 3003K->3003K(1056768K)], 0.0028761 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * at com.chenjj.java.TestMinorGC.testAllocation(TestMinorGC.java:16)
 * at com.chenjj.java.TestMinorGC.main(TestMinorGC.java:7)
 * Heap
 * PSYoungGen      total 3584K, used 2629K [0x00000000ffc00000, 0x0000000100000000, 0x0000000100000000)
 * eden space 3072K, 85% used [0x00000000ffc00000,0x00000000ffe915a0,0x00000000fff00000)
 * from space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 * to   space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 * ParOldGen       total 6144K, used 4097K [0x00000000ff600000, 0x00000000ffc00000, 0x00000000ffc00000)
 * object space 6144K, 66% used [0x00000000ff600000,0x00000000ffa00558,0x00000000ffc00000)
 * Metaspace       used 3037K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 333K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * 加上-XX:+PrintHeapAtGC参数，可以在GC前后，都有详细的GC日志输出，分别表示GC回收前和GC回收后的堆信息。
 * 加上-XX:+PrintGCTimeStamps会在每次GC发生时，额外输出GC发生的时间，该输出时间为虚拟机启动后的时间偏移量，单位秒。
 * 由于GC会引起应用程序停顿，因此，可能还需要特别关注应用程序的执行时间和停顿时间。-XX:+PrintGCApplicationConcurrentTime可以打印应用程序
 * 的执行时间，-XX:+PrintGCApplicationStoppedTime可以打印应用程序由于GC而产生的停顿时间。
 * 如果想跟踪系统内的软引用、弱引用、虚引用和Finallize队列，则可以使用-XX:+PrintReferenceGC。
 * 默认情况下，GC日志会在控制台中输出，这不便于后续分析和定位问题，可以通过参数-Xloggc指定GC日志的输出位置，比如：-Xloggc:log/gc.log，
 * 在当前目录下的log文件夹下的gc.log文件中记录gc日志。
 */
public class TestMinorGC {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        testAllocation();
    }

    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;

        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[2 * _1MB];
    }
}
