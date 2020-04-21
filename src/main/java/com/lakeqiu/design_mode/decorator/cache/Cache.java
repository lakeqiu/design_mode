package com.lakeqiu.design_mode.decorator.cache;

import com.lakeqiu.design_mode.decorator.cache.computable.Computable;
import com.lakeqiu.design_mode.decorator.cache.computable.ExpensiveFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lakeqiu
 */
public class Cache<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    /**
     * 负责计算
     */
    private Computable<A, V> computable;

    public Cache(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        System.out.println("进入缓存机制");
        while (true) {
            // 1、获取缓存
            Future<V> result = cache.get(arg);
            // 缓存中没有，计算出来
            if (result == null) {
                // 使用FutureTask避免有一个线程在计算时
                // 另外一个线程带着同样的key过来导致的重复计算问题
                FutureTask<V> futureTask = new FutureTask<V>(() -> computable.compute(arg));
                // 尝试加入Map中
                Future<V> f = cache.putIfAbsent(arg, futureTask);
                // 说明是第一次加入，前面没有线程加入Map，可以计算了
                if (f == null) {
                    // result等于futureTask，futureTask放入缓存，这样，
                    // 另外的线程带着同样的key过来就会在1处陷入等待
                    result = futureTask;
                    // 开始计算
                    futureTask.run();
                }
                // 如果f不为null的话，说明有线程加入Map中了，且正在计算
                // 那么就没必要计算了
            }

            try {
                return result.get();
            } catch (CancellationException | InterruptedException e) {
                // 移除计算失败的结果，防止缓存被污染
                cache.remove(arg);
                // 被取消异常
                throw e;
            } catch (ExecutionException e) {
                // 移除计算失败的结果，防止缓存被污染
                cache.remove(arg);
                // 计算失败的异常，重试就可以了
                System.out.println("计算失败，重试中");
            }
        }
    }

    // 定期执行任务的线程池，帮助缓存过期时删除缓存
    private final static ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(5);

    /**
     * 有缓存时间的计算方法
     * @param arg
     * @param expire 缓存时间
     * @return
     */
    public V compute(A arg, long expire) throws InterruptedException {
        // 删除缓存
        if (expire > 0) {
            EXECUTOR.schedule(() -> expire(arg), expire, TimeUnit.MILLISECONDS);
        }
        // 计算
        return compute(arg);
    }

    /**
     * 删除缓存
     * @param arg
     */
    private synchronized void expire(A arg) {
        Future<V> future = cache.get(arg);
        if (future != null) {
            // 有线程在等待缓存，通知其过期了
            if (!future.isDone()) {
                future.cancel(true);
            }


            System.out.println("缓存过期");
            cache.remove(arg);
        }
    }

    public static void main(String[] args) throws Exception {
        Cache<String, Integer> cache = new Cache<>(new ExpensiveFunction());
        String s = "666";
        Integer compute = cache.compute(s);
        System.out.println(compute);
        System.out.println(cache.compute(s));
    }
}
