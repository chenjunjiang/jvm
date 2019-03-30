package com.chenjj.java.gc.algorithm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用是所有引用类型中最弱的一个。一个持有虚引用的对象，和没有引用几乎是一样的，随时都可能被垃圾回收器回收。当试图通过虚引用的get()方法取得强引用
 * 时，总是会失败。并且，虚引用必须和引用队列一起使用，它的作用在于跟踪垃圾回收过程。
 */
public class TestPhantomReference {
    public static TestPhantomReference testPhantomReference;
    static ReferenceQueue<TestPhantomReference> referenceQueue = null;

    public static class CheckReferenceQueue extends Thread {
        @Override
        public void run() {
            while (true) {
                if (referenceQueue != null) {
                    PhantomReference<TestPhantomReference> phantomReference = null;
                    try {
                        phantomReference = (PhantomReference<TestPhantomReference>) referenceQueue.remove();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (phantomReference != null) {
                        System.out.println("TestPhantomReference is delete by GC");
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "I am TestPhantomReference";
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("TestPhantomReference finalize called");
        // 复活对象
        testPhantomReference = this;
    }

    /**
     * TestPhantomReference finalize called
     * testPhantomReference可用
     * 第二次gc
     * TestPhantomReference is delete by GC
     * testPhantomReference is null
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new CheckReferenceQueue();
        thread.setDaemon(true);
        thread.start();

        referenceQueue = new ReferenceQueue<TestPhantomReference>();
        testPhantomReference = new TestPhantomReference();
        PhantomReference<TestPhantomReference> phantomReference = new PhantomReference<>(testPhantomReference, referenceQueue);
        testPhantomReference = null;
        System.gc();
        Thread.sleep(1000);
        if (testPhantomReference == null) {
            System.out.println("testPhantomReference is null");
        } else {
            System.out.println("testPhantomReference可用");
        }
        System.out.println("第二次gc");
        testPhantomReference = null;
        System.gc();
        Thread.sleep(1000);
        if (testPhantomReference == null) {
            System.out.println("testPhantomReference is null");
        } else {
            System.out.println("testPhantomReference可用");
        }
    }
}
