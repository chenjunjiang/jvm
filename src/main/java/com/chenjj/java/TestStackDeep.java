package com.chenjj.java;

/**
 * java栈是一块线程私有的内存空间。和线程执行密切相关。线程执行的基本行为是函数调用，每次函数调用的数据都是通过java栈传递的。
 * java栈中保存的主要内容是栈帧，一个栈帧中，至少要包含局部变量表、操作数栈、和帧数据区几个部分。
 * 由于每次函数调用都会生成对应的栈帧，从而占用一定的栈空间。
 * The stack size specified is too small, Specify at least 228k
 * 函数嵌套调用的层次在很大程度上由栈的大小决定，栈越大，函数可以支持的嵌套调用次数就越多
 */
public class TestStackDeep {
    private static int count = 0;

    public static void recursion() {
        count++;
        recursion();
    }

    /**
     * 最后会抛出java.lang.StackOverflowError
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            recursion();
        } catch (Throwable e) {
            System.out.println("deep of calling = " + count);
            e.printStackTrace();
        }
    }
}
