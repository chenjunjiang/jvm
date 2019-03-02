package com.chenjj.java;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用参数-verbose:class跟踪类的加载和卸载，也可以单独使用-XX:+TraceClassLoading跟踪类的加载，使用参数-XX:+TraceClassUnLoading跟踪类
 * 的卸载。
 * <p>
 * 使用ASM动态生成名为Example的类，并将其反复加载到系统。
 * http://www.cnblogs.com/yuyutianxia/p/4750752.html
 * 如果以一个L开头的描述符，就是类描述符，它后紧跟着类的字符串，然后分号“；”结束。比如"Ljava/lang/String;"就是表示类型String；
 * <p>
 * 参数-XX:PrintVMOptions可以在程序运行时，打印虚拟机接受到的命令行显式参数。
 * 参数-XX:PrintCommandLineFlags可以打印传递给虚拟机的显式和隐式参数，隐式参数未必是通过命令行直接给出的，它可能是由虚拟机启动时自动设置的。
 * 参数-XX:PrintFlagsFinal，它会打印所有的系统参数的值。
 */
public class UnloadClass implements Opcodes {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC, "Example", null, "java/lang/Object", null);
        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mw.visitInsn(RETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
        mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mw.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mw.visitLdcInsn("Hello world!");
        mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        mw.visitInsn(RETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
        byte[] code = cw.toByteArray();

        for (int i = 0; i < 10; i++) {
            UnloadClassLoader loader = new UnloadClassLoader();
            Method m = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            m.setAccessible(true);
            m.invoke(loader, "Example", code, 0, code.length);
            m.setAccessible(false);
            // Full GC，释放上一次循环加载的类，因此，这一过程会涉及类的加载和卸载。
            System.gc();
        }
    }
}
