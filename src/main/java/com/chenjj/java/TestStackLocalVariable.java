package com.chenjj.java;

/**
 * 局部变量表保存函数的参数以及局部变量。局部变量只在当前函数调用中有效，当函数调用结束后，随着函数栈帧的销毁，局部变量表也会随之销毁。
 * 由于局部变量表在栈帧中，因此，如果函数的参数和局部变量较多，会使得局部变量表膨胀，从而每一次函数调用就会占用更多的空间，最终导致函数
 * 的嵌套调用次数减少。
 * 栈帧中的局部变量表中的槽位是可以重用的，如果一个局部变量过了其作用域，那么在其作用域之后申明的新局部变量就很有可能会复用过期局部变量的槽位，
 * 从而达到节约资源的目的。
 * 局部变量表中的变量也是重要的垃圾回收根节点，只要被局部变量表中直接或间接引用的对象都是不会被回收的。
 */
public class TestStackLocalVariable {
    private static int count = 0;

    /**
     * 使用jclasslib工具(idea中可以安装插件)可以进一步查看函数的局部变量信息(注意：查看之前请先编译)。
     * 该函数的最大局部变量表的大小为26个字。因为该函数包含的参数和局部变量
     * 一共是13个，且都为long型，long和double在局部变量表中需要占用2个字，其他比如int、short、byte、对象引用等占用1个字。
     * 字指的是计算机内存中占据一个单独的内存单元编号的一组二进制串。一般32位计算机上一个字为4个字节长度。
     *
     * @param a
     * @param b
     * @param c
     */
    public static void recursion(long a, long b, long c) {
        long e = 1, f = 2, g = 3, h = 4, i = 5, k = 6, q = 7, x = 8, y = 9, z = 10;
        count++;
        recursion(a, b, c);
    }

    public static void recursion() {
        count++;
        recursion();
    }

    /**
     * 该函数最大局部变量大小为3字，第0个槽位为函数的this引用（实例方法的第一个局部变量都是this引用），第1个槽位为变量a，第2个槽位为变量b，
     * 每个变量占1字，合计3字。
     */
    public void localVar1() {
        int a = 0;
        System.out.println(a);
        int b = 0;
    }

    /**
     * 该函数的最大局部变量大小为2字，虽然和localVar1()一样，拥有this、a、b三个局部变量，但b复用了a的槽位，因此在整个函数执行中，同时存在的最大
     * 局部变量为2字。
     */
    public void localVar2() {
        {
            int a = 0;
            System.out.println(a);
        }
        int b = 0;
    }

    public static void main(String[] args) {
        try {
            // recursion(0L, 0L, 0L);
            recursion();
        } catch (Throwable e) {
            System.out.println("deep of calling = " + count);
            e.printStackTrace();
        }
    }
}
