package com.lakeqiu.design_mode.decorator.cache.computable;

/**
 * 计算接口
 * @author lakeqiu
 */
public interface Computable<A, V> {
    /**
     * 计算
     * @param arg
     * @return
     * @throws Exception
     */
    V compute(A arg) throws Exception;
}
