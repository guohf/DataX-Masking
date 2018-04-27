package com.alibaba.datax.transport.transformer.maskingMethods.differentialPrivacy;

import com.alibaba.datax.transport.transformer.maskingMethods.AbstractMasking;


/**
 * Differential Privacy Masking.
 *
 * @author Wenyan Liu
 */
public abstract class DifferentialPrivacyMasking extends AbstractMasking {

    /**
     * Privacy budget: epsilon
     */
//    protected double epsilon;

    @Override
    public double evaluate() throws Exception {
        // TODO: Differential Privacy Evaluate
        return -1;
    }
}