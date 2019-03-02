package com.chenjj.java;

/**
 * 参数-Xmn可以用于设置新生代的大小。它的大小一般设置为整个堆空间的1/3到1/4左右。
 * XX:SurvivorRatio参数用来设置新生代中eden空间和from/to空间的比例关系，2个Survivor区和Eden区的比值
 * 8 表示 Eden:两个Survivor = 8:2 ，每个Survivor占 1/10 。
 * 2 表示 Eden:两个Survivor = 2:2 ，每个Survivor占 1/4 。
 * <p>
 * -Xmx20m -Xms20m -Xmn1m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
 * [GC (Allocation Failure) [DefNew: 510K->256K(768K), 0.0007664 secs] 510K->261K(20224K), 0.0007912 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [DefNew: 761K->77K(768K), 0.0005449 secs] 767K->337K(20224K), 0.0005602 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 * def new generation   total 768K, used 458K [0x00000000fec00000, 0x00000000fed00000, 0x00000000fed00000)
 * eden space 512K,  74% used [0x00000000fec00000, 0x00000000fec5f6b0, 0x00000000fec80000)
 * from space 256K,  30% used [0x00000000fec80000, 0x00000000fec934c8, 0x00000000fecc0000)
 * to   space 256K,   0% used [0x00000000fecc0000, 0x00000000fecc0000, 0x00000000fed00000)
 * tenured generation   total 19456K, used 10500K [0x00000000fed00000, 0x0000000100000000, 0x0000000100000000)
 * the space 19456K,  53% used [0x00000000fed00000, 0x00000000ff7412a8, 0x00000000ff741400, 0x0000000100000000)
 * Metaspace       used 3011K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 330K, capacity 388K, committed 512K, reserved 1048576K
 * 由于eden区无法容纳任何一个程序中分配的1MB数组，故发生了一次新生代GC，堆eden区进行了部分回收，同时，这个偏小的新生代无法为1MB数组预留空间，
 * 故所有的数组都分配在老年代，老年代最终占用10500K空间。
 *
 * <p>
 * Java HotSpot(TM) 64-Bit Server VM warning: NewSize (1536k) is greater than the MaxNewSize (1024k). A new max generation size of 1536k will be used.
 * -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=1048576 -XX:NewSize=1048576 -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:SurvivorRatio=2 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC
 * [GC (Allocation Failure) [PSYoungGen: 510K->352K(1024K)] 510K->352K(19968K), 0.0011363 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [PSYoungGen: 857K->384K(1024K)] 857K->384K(19968K), 0.0017160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 * PSYoungGen      total 1024K, used 755K [0x00000000ffe80000, 0x0000000100000000, 0x0000000100000000)
 * eden space 512K, 72% used [0x00000000ffe80000,0x00000000ffedce90,0x00000000fff00000)
 * from space 512K, 75% used [0x00000000fff80000,0x00000000fffe0020,0x0000000100000000)
 * to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 * ParOldGen       total 18944K, used 10240K [0x00000000fec00000, 0x00000000ffe80000, 0x00000000ffe80000)
 * object space 18944K, 54% used [0x00000000fec00000,0x00000000ff6000a0,0x00000000ffe80000)
 * Metaspace       used 3011K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 330K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * JVM在不同的收集器下，SurvivorRatio都设为2，eden和servivor的比例不一样，在UseParallelGC下，Xmn的大小必须是偶数大小，这样计算出来的
 * eden和servivor比例才正确，如果是奇数，eden和servivor的比例不正确。
 * 具体原因再研究，猜想是不是不同的垃圾收集器导致虚拟机底层的最小分配单位不同。
 * <p>
 * -Xmx20m -Xms20m -Xmn7m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC -XX:+PrintCommandLineFlags
 * -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=7340032 -XX:NewSize=7340032 -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:SurvivorRatio=2 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseSerialGC
 * [GC (Allocation Failure) [DefNew: 3542K->1418K(5376K), 0.0014140 secs] 3542K->1418K(18688K), 0.0014337 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * [GC (Allocation Failure) [DefNew: 4558K->1024K(5376K), 0.0009958 secs] 4558K->1409K(18688K), 0.0010116 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [DefNew: 4156K->1024K(5376K), 0.0002815 secs] 4542K->1409K(18688K), 0.0002937 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 * def new generation   total 5376K, used 3207K [0x00000000fec00000, 0x00000000ff300000, 0x00000000ff300000)
 * eden space 3584K,  60% used [0x00000000fec00000, 0x00000000fee21f88, 0x00000000fef80000)
 * from space 1792K,  57% used [0x00000000ff140000, 0x00000000ff240010, 0x00000000ff300000)
 * to   space 1792K,   0% used [0x00000000fef80000, 0x00000000fef80000, 0x00000000ff140000)
 * tenured generation   total 13312K, used 385K [0x00000000ff300000, 0x0000000100000000, 0x0000000100000000)
 * the space 13312K,   2% used [0x00000000ff300000, 0x00000000ff360728, 0x00000000ff360800, 0x0000000100000000)
 * Metaspace       used 3011K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 330K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * -Xmx20m -Xms20m -Xmn15m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC -XX:+PrintCommandLineFlags
 * -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=15728640 -XX:NewSize=15728640 -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:SurvivorRatio=2 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseSerialGC
 * [GC (Allocation Failure) [DefNew: 7680K->1418K(11520K), 0.0013110 secs] 7680K->1418K(16640K), 0.0013312 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 * def new generation   total 11520K, used 5740K [0x00000000fec00000, 0x00000000ffb00000, 0x00000000ffb00000)
 * eden space 7680K,  56% used [0x00000000fec00000, 0x00000000ff0384b8, 0x00000000ff380000)
 * from space 3840K,  36% used [0x00000000ff740000, 0x00000000ff8a2be8, 0x00000000ffb00000)
 * to   space 3840K,   0% used [0x00000000ff380000, 0x00000000ff380000, 0x00000000ff740000)
 * tenured generation   total 5120K, used 0K [0x00000000ffb00000, 0x0000000100000000, 0x0000000100000000)
 * the space 5120K,   0% used [0x00000000ffb00000, 0x00000000ffb00000, 0x00000000ffb00200, 0x0000000100000000)
 * Metaspace       used 3010K, capacity 4496K, committed 4864K, reserved 1056768K
 * class space    used 330K, capacity 388K, committed 512K, reserved 1048576K
 * <p>
 * 不同的堆分布情况，堆系统执行会产生一定影响。在实际工作中，应该根据系统的特点做合理的设置，基本策略是：尽可能将对象预留在新生代，
 * 减少老年代GC的次数。
 *
 * -Xmx20m -Xms20m -XX:NewRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC -XX:+PrintCommandLineFlags
 *-XX:NewRatio用于设置新生代和老年代的比例。-XX:NewRatio=老年代/新生代。
 */
public class TestNewSize {
    public static void main(String[] args) {
        byte[] bytes = null;
        for (int i = 0; i < 10; i++) {
            bytes = new byte[1 * 1024 * 1024];
        }
    }
}
