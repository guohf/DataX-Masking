package com.alibaba.datax.transport.transformer.maskingMethods.anonymity;

import com.alibaba.datax.transport.transformer.maskingMethods.AbstractMasking;

/**
 * Anonymity Masking.
 *匿名技术
 * @author Wenyan Liu
 */
public abstract class AnonymityMasking extends AbstractMasking {

    protected void setup() throws Exception {
    }

    @Override
    public double evaluate() throws Exception {
        // TODO: Anonymity Evaluate
        return -1;
    }
}