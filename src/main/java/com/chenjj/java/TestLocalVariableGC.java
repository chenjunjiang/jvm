package com.chenjj.java;

/**
 * 使用-XX:+PrintGC执行上述几个函数，在输出日志中，可以看到垃圾回收前后堆的大小，进而推断byte数组是否被回收。
 */
public class TestLocalVariableGC {

    /**
     * 申请空间后，立即进行垃圾回收，很明显，byte数组被变量a引用，因此无法回收这块空间。
     */
    public void localVarGc1() {
        byte[] a = new byte[6 * 1024 * 1024];
        System.gc();
    }

    /**
     * 将变量a置为null，使byte数组失去强引用，故垃圾回收可以顺利回收byte数组。
     */
    public void localVarGc2() {
        byte[] a = new byte[6 * 1024 * 1024];
        a = null;
        System.gc();
    }

    /**
     * 虽然变量a已经离开了作用域，但是变量a依然存在于局部变量表中，并且也指向这块byte数组，故依然无法被回收。
     */
    public void localVarGc3() {
        {
            byte[] a = new byte[6 * 1024 * 1024];
        }
        System.gc();
    }

    /**
     * 先使变量a失效，然后申明了变量c，使得变量c复用了变量a的字，由于变量a此时被销毁，故可以顺利回收byte数组
     */
    public void localVarGc4() {
        {
            byte[] a = new byte[6 * 1024 * 1024];
        }
        int c = 10;
        System.gc();
    }

    /**
     * 调用localVarGc1()并没有回收byte数组，但在localVarGc1()返回后，它的栈帧被销毁，故byte数组失去引用，
     * 因此在localVarGc5()的垃圾回收中被回收。
     */
    public void localVarGc5() {
        localVarGc1();
        System.gc();
    }

    public static void main(String[] args) {
        TestLocalVariableGC testLocalVariableGC = new TestLocalVariableGC();
        testLocalVariableGC.localVarGc5();
    }
}
