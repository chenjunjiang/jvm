package com.chenjj.java.gc.algorithm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 弱引用：
 * 弱引用是一种比软引用还弱的引用类型。在系统GC时，只要发现弱引用，不管系统堆空间使用情况如何，都会将对象进行回收。但是，由于垃圾回收器的线程优先级
 * 通常很低，因此，并不一定能很快发现持有弱引用的对象。在这种情况下，弱引用可以存在较长时间。一旦一个弱引用对象被垃圾回收器回收，便会加入到一个注册
 * 的引用队列中。
 */
public class TestWeakReference {

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

    static ReferenceQueue<User> weakReferenceQueue = null;

    public static class CheckReferenceQueue extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("xxxxxxxxxxxxxxxxxx" + weakReferenceQueue);
                if (weakReferenceQueue != null) {
                    UserWeakReference userWeakReference = null;
                    try {
                        //Removes the next reference object in this queue, blocking until one becomes available.
                        // 这里会阻塞
                        userWeakReference = (UserWeakReference) weakReferenceQueue.remove();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (userWeakReference != null) {
                        System.out.println("yyyyyyyyyyy" + weakReferenceQueue);
                        System.out.println("user id " + userWeakReference.uid + " is delete");
                    }
                }
            }
        }
    }

    public static class UserWeakReference extends WeakReference<User> {
        int uid;

        public UserWeakReference(User referent, ReferenceQueue<? super User> q) {
            super(referent, q);
            uid = referent.id;
        }
    }

    /**
     * User{id=1, name='chenjj'}
     * After GC:
     * null
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new CheckReferenceQueue();
        // 这里如果不设置成守护线程，那么上面的while 循环就会一直执行，如果是守护线程，那么在main线程结束之后，虚拟机就退出了。
        // 因为虚拟机退出是在它里面只剩下守护线程的时候。
        thread.setDaemon(true);
        thread.start();

        User user = new User(1, "chenjj");
        weakReferenceQueue = new ReferenceQueue<User>();
        WeakReference<User> userWeakReference = new UserWeakReference(user, weakReferenceQueue);
        user = null;
        System.out.println(userWeakReference.get());
        System.gc();
        System.out.println("After GC:");
        System.out.println(userWeakReference.get());

        Thread.sleep(1000);
    }
}
