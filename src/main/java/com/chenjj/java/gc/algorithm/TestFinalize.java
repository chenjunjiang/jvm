package com.chenjj.java.gc.algorithm;

/**
 * 垃圾回收基本思想是考察一个对象的可触及性，即从根节点开始是否可以访问到这个对象，如果可以，则说明当前对象正在使用，如果从根节点都无法访问到某个对象，
 * 说明对象已经不再使用了，一般来说，此对象需要被回收。但事实上，一个无法触及的对象有可能在某一个条件下“复活”自己，如果这样，那么对它的回收就是不合理
 * 的，为此，需要给出一个对象可触及性状态的定义，并规定在什么状态下，才可以安全回收对象。
 * 以下这些对象可作为根节点：
 * 1、虚拟机栈的栈帧的局部变量表所引用的对象；
 * 2、本地方法栈的JNI所引用的对象；
 * 3、方法区的静态变量和常量所引用的对象；
 * 参考：http://www.cnblogs.com/hzzjj/p/6268432.html
 * <p>
 * 可触及性包含以下三种状态：
 * 1、可触及的：从根节点开始，可以到达这个对象。
 * 2、可复活的：对象的所有引用都被释放，但是对象有可能在finalize()函数中复活。
 * 3、你可触及的：对象的finalize()函数被调用，并且没有复活，那么就会进入不可触及状态，不可触及的对象不可能被复活，因为finalize()函数只会被调用一次。
 * <p>
 * 从执行结果可以看出：
 * 第一次GC时，在finalize()函数调用之前，虽然系统中的引用已经被清除，但是在调用实例方法finalize()的时候，对象的this引用依然会被传入方法内部，
 * 如果引用外泄，对象就会复活，此时，对象又变为可触及状态。而finalize()函数只会被调用一次，因此，第二次清除对象时，对象就再无机会复活，因此就会被回收。
 * 注意：
 * finalize()函数是一个非常糟糕的模式，不推荐使用finalize()函数释放资源。因为finalize()函数有可能发生引用外泄，在无意中复活对象。由于
 * finalize()是被系统调用的，调用时间是不明确的，因此不是一个好的资源释放方案，推荐在try-catch-finally语句中进行资源的释放。
 */
public class TestFinalize {
    public static TestFinalize testFinalize;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("TestFinalize finalize called");
        testFinalize = this;
        System.out.println(testFinalize+"yyy");
    }

    /*@Override
    public String toString() {
        return "I am TestFinalize";
    }*/

    /**
     * TestFinalize finalize called
     * testFinalize可用
     * 第二次gc
     * testFinalize 是 null
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        testFinalize = new TestFinalize();
        System.out.println(testFinalize+"xxx");
        testFinalize = null;
        System.gc();
        Thread.sleep(1000);
        if (testFinalize == null) {
            System.out.println("testFinalize 是 null");
        } else {
            System.out.println("testFinalize可用");
        }
        System.out.println("第二次gc");
        testFinalize = null;
        System.gc();
        Thread.sleep(1000);
        if (testFinalize == null) {
            System.out.println("testFinalize 是 null");
        } else {
            System.out.println("testFinalize可用");
        }
    }
}
