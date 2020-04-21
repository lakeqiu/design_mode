package com.lakeqiu.design_mode.decorator.cache.computable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author lakeqiu
 */
public class ExpensiveFunction implements Computable<String, Integer> {

    @Override
    public Integer compute(String arg) throws Exception {
        // 模拟计算过程中出现异常
        if (Math.random() > 0.8) {
            throw new IOException("找不到文件");
        }

        // 模拟计算过程
        TimeUnit.SECONDS.sleep(3);
        return Integer.valueOf(arg);
    }
}
