package com.zhong.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

public class ZrPrime {
    @Test
    public void t1() {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        for (int i = 0; i < 10; i++) {
            Element e = pairing.getZr().newRandomElement();
            System.out.println(e);
        }
    }
}
