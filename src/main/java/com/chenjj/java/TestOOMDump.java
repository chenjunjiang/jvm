package com.chenjj.java;

import java.util.Vector;

/**
 * -Xmx20m -Xms5m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/chenjunjiang/workspace/jvm/a.dump
 * -XX:+HeapDumpOnOutOfMemoryError参数可以在内存溢出时导出整个堆信息。
 * <p>
 * java.lang.OutOfMemoryError: Java heap space
 * Dumping heap to /home/chenjunjiang/workspace/jvm/a.dump ...
 * Heap dump file created [15768920 bytes in 0.016 secs]
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * at com.chenjj.java.TestOOMDump.main(TestOOMDump.java:12)
 * 可以使用MAT等工具堆dump文件进行分析。
 * 除了导出堆信息外，虚拟机还允许在发生错误时执行一个脚本文件。该文件可以用于崩溃程序的自救（重启）、报警或通知，也可以帮助开发人员获取更多的系统信息
 * ，如完整的线程转存（即Thread Dump或Core Dump）文件。下面是一个在发生OOM后导出线程转存的例子：
 * 准备printstack.sh脚本：
 * jstack -F %1 > /home/chenjunjiang/workspace/jvm/a.txt
 * 使用以下参数执行代码：
 * -Xmx20m -Xms5m "-XX:OnOutOfMemoryError=printstack.sh %p"
 * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/chenjunjiang/workspace/jvm/a.dump
 * 在程序异常退出时，就会在指定位置生成a.txt文件，里面保存着线程转存信息。
 */
public class TestOOMDump {
    public static void main(String[] args) {
        Vector vector = new Vector();
        for (int i = 0; i < 25; i++) {
            vector.add(new byte[1 * 1024 * 1024]);
        }
    }
}
