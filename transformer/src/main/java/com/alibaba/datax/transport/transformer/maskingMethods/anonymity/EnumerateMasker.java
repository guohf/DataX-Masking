package com.alibaba.datax.transport.transformer.maskingMethods.anonymity;

/**
 * Created by Liu Kun on 2018/5/8.
 */
public class EnumerateMasker {
    public static long mask(long origin, long offset)throws Exception{
        double expand = (offset * offset * offset * 168) % 1024 ;
        return (int)(expand * (origin + offset));
    }
}
