package com.chenjj.java.gc.algorithm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 强引用：
 * 强引用可以直接访问目标对象。
 * 强引用所指向的对象在任何时候都不会被系统回收， 虚拟机宁愿排除OOM异常，也不会回收强引用所指向的对象。
 * 强引用可能导致内存泄露。
 * <p>
 * 软引用：
 * 一个对象只持有软引用，GC未必会回收软引用的对象，但是，当堆空间不足时，就会被回收，所以软引用对象不会引起内存溢出。
 * 每一个软引用都可以附带一个引用队列，当对象的可达性状态发生改变时（由可达变为不可达），软引用对象就会进入引用队列。通过这个队列，可以跟踪对象的回收
 * 情况。
 */
public class TestSoftReference {

    public static class User {
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int id;
        public String name;

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

    }

    static ReferenceQueue<User> softReferenceQueue = null;

    public static class CheckReferenceQueue extends Thread {
        @Override
        public void run() {
            while (true) {
                if (softReferenceQueue != null) {
                    UserSoftReference userSoftReference = null;
                    try {
                        userSoftReference = (UserSoftReference) softReferenceQueue.remove();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (userSoftReference != null) {
                        System.out.println("user id" + userSoftReference.uid + "is delete");
                    }
                }
            }
        }
    }

    public static class UserSoftReference extends SoftReference<User> {
        int uid;

        public UserSoftReference(User referent, ReferenceQueue<? super User> q) {
            super(referent, q);
            uid = referent.id;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new CheckReferenceQueue();
        thread.setDaemon(true);
        thread.start();

        User user = new User(1, "chenjj");
        softReferenceQueue = new ReferenceQueue<User>();
        SoftReference<User> userSoftReference = new UserSoftReference(user, softReferenceQueue);
        user = null;
        System.out.println(userSoftReference.get());
        System.gc();
        System.out.println("After GC:");
        System.out.println(userSoftReference.get());

        System.out.println("try to create byte array and GC");
        byte[] bytes = new byte[1024 * 925 * 7];
        System.gc();
        System.out.println(userSoftReference.get());

        Thread.sleep(1000);
    }
}
