package com.zhong.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TimeTest {

    //Zr域中的元素做乘法的时间
    @Test
    public void multipleTimeTest() throws UnsupportedEncodingException {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        String filenamePrefix = "ind";
        int nums[] = {10, 100, 1000, 10000, 100000, 1000000};
        for (int num : nums) {
            ArrayList<Element> elements = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                String filename = filenamePrefix + i;
                Element e = pairing.getZr().newElementFromBytes(filename.getBytes("utf-8"));
                elements.add(e);
            }

            Element res = pairing.getZr().newOneElement();
            long t1 = System.currentTimeMillis();
            for (Element e : elements) {
                res = res.duplicate().mul(e.duplicate());
            }
            long t2 = System.currentTimeMillis();
            System.out.println(num + " 个元素相乘所需时间：" + (t2 - t1) + " ms");
        }
    }


    //G域中的元素做乘法的时间
    @Test
    public void multipleTimeTest2() throws UnsupportedEncodingException {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        String filenamePrefix = "ind";
        int nums[] = {10, 100, 1000, 10000, 100000, 1000000};
        for (int num : nums) {
            ArrayList<Element> elements = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                String filename = filenamePrefix + i;
                Element e = pairing.getG1().newElementFromBytes(filename.getBytes("utf-8"));
                elements.add(e);
            }

            Element res = pairing.getG1().newOneElement();
            long t1 = System.currentTimeMillis();
            for (Element e : elements) {
                res = res.duplicate().mul(e.duplicate());
            }
            long t2 = System.currentTimeMillis();
            System.out.println(num + " 个元素相乘所需时间：" + (t2 - t1) + " ms");
        }
    }

    //G域中的元素做乘法的时间
    @Test
    public void multipleTimeTest3() throws UnsupportedEncodingException {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        Element e = pairing.getG1().newRandomElement();
        int nums[] = {10, 100, 1000, 10000, 100000, 1000000};
        for (int num : nums) {
            Element res = pairing.getG1().newOneElement();
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                res = e.duplicate().mul(res.duplicate());
            }
            long t2 = System.currentTimeMillis();
            System.out.println(num + " 个元素相乘所需时间：" + (t2 - t1) + " ms");
        }
    }
}
