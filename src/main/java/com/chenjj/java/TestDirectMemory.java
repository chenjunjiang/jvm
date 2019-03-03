package com.chenjj.java;

import java.nio.ByteBuffer;

/**
 * NIO的Buffer提供了一个可以不经过JVM内存直接访问系统物理内存的类——DirectBuffer。
 * DirectBuffer类继承自ByteBuffer，但和普通的ByteBuffer不同，普通的ByteBuffer仍在JVM堆上分配内存，其最大内存受到最大堆内存的限制；
 * 而DirectBuffer直接分配在物理内存中，并不占用堆空间，其可申请的最大内存受操作系统限制。
 * <p>
 * 直接内存的读写操作比普通Buffer快，但它的创建、销毁比普通Buffer慢。
 * <p>
 * 因此直接内存使用于需要大内存空间且频繁访问的场合，不适用于频繁申请释放内存的场合。
 * DirectBuffer并没有真正向OS申请分配内存，其最终还是通过调用Unsafe的allocateMemory()来进行内存分配。
 * 不过JVM对Direct Memory可申请的大小也有限制，可用-XX:MaxDirectMemorySize=1M设置，这部分内存不受JVM垃圾回收管理。
 * <p>
 * DirectBuffer的创建过程会通过Unsafe接口直接通过os::malloc来分配内存，然后将内存的起始地址和大小存到java.nio.DirectByteBuffer对象里
 * （相当于是DirectByteBuffer引用着这些内存的信息），
 * 这样就可以直接操作这些内存。这些内存只有在DirectByteBuffer回收掉之后才有机会被回收，
 * 因此如果这些对象大部分都移到了old，但是一直没有触发CMS GC或者Full GC，那么悲剧将会发生，因为你的物理内存被他们耗尽了，因此为了避免这种悲剧的发生，
 * 通过-XX:MaxDirectMemorySize来指定最大的堆外内存大小，当使用达到了阈值的时候将调用System.gc来做一次full gc，
 * 以此来回收掉没有被使用的堆外内存。
 * <p>
 * https://www.jianshu.com/p/5da55f7dd53d
 * 这篇文章介绍了可以通过((DirectBuffer)byteBuffer).cleaner().clean()手动释放直接内存。
 * 如果不手动释放，那么当jvm发现直接内存MaxDirectMemorySize=1024M所剩无几的时候，就会触发fullGC。如果此时禁用System.gc()，
 * 参数 -XX:-+DisableExplicitGC就有可能会导致直接内存得不到回收，发生OOM。因为在这种不手动释放的情况下，
 * 只有发生堆内存回收的时候，才会顺带的回收直接内存。
 * https://www.cnblogs.com/xll1025/p/6517871.html
 * <p>
 * 访问性能测试：
 * access_nondirect:176
 * access_direct:101
 * <p>
 * 分配性能测试：
 * allocate_nondirect:187
 * allocate_direct:563
 * <p>
 * 从结果可以看出，虽然在读写上直接内存有较大的优势，但是在内存空间申请时，直接内存毫无优势可言。所以，直接内存适合申请次数较少、访问较频繁的场合。
 * 如果内存空间本身需要频繁申请，则不适合使用直接内存。
 */
public class TestDirectMemory {
    // 分配堆内存
    public static void bufferAccess() {
        long startTime = System.currentTimeMillis();
        ByteBuffer b = ByteBuffer.allocate(500);
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 99; j++)
                b.putInt(j);
            b.flip();
            for (int j = 0; j < 99; j++)
                b.getInt();
            b.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("access_nondirect:" + (endTime - startTime));
    }

    // 直接分配内存
    public static void directAccess() {
        long startTime = System.currentTimeMillis();
        ByteBuffer b = ByteBuffer.allocateDirect(500);
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 99; j++)
                b.putInt(j);
            b.flip();
            for (int j = 0; j < 99; j++)
                b.getInt();
            b.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("access_direct:" + (endTime - startTime));
    }

    public static void bufferAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            ByteBuffer.allocate(1000);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("allocate_nondirect:" + (endTime - startTime));
    }

    public static void directAllocate() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            ByteBuffer.allocateDirect(1000);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("allocate_direct:" + (endTime - startTime));
    }

    public static void main(String args[]) {
        System.out.println("访问性能测试：");
        bufferAccess();
        directAccess();

        System.out.println();

        System.out.println("分配性能测试：");
        bufferAllocate();
        directAllocate();
    }
}
