package com.zhong.accumulator;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

public class PolyTest {
    @Test
    public void t1() {
        /**
         * 双线性对
         */
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        //Element t = pairing.getFieldAt()
    }
}
