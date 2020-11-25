package java0.conc0303;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class CountDownLatchMethod {

    private volatile   Integer value  = 0;
    private CountDownLatch latch;

    public  CountDownLatchMethod(CountDownLatch latch){
        this.latch = latch;
    }
    public static void main(String[] args) throws  Exception{
        
        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatchMethod countDownLatchMethod = new CountDownLatchMethod(latch);
        Thread t1 = new Thread(()->{
            try {
                countDownLatchMethod.sum(36);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t1.join();
        int result = countDownLatchMethod.value();
        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        // 然后退出main线程
        System.exit(0);
    }



    private  void sum(int sum) throws InterruptedException {
        value = fibo(sum);
        latch.countDown();

    }
    private int value() throws InterruptedException {
        latch.await();
        return value;
    }
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
