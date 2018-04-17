package com.zhong.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-13 16:22
 **/
public class BilinearMapAccumulatorTest {

    //生成私钥测试
    @Test
    public void generateSiYaoTest() {
        BilinearMapAccumulator_SiYao.generateKey();
    }

    //生成公钥测试
    @Test
    public void generateGongYaoTest() {
        BilinearMapAccumulator_SiYao siYao = BilinearMapAccumulator_SiYao.getKey();
        BilinearMapAccumulator_GongYao.generateKey(siYao);
    }

    //测试某个元素的witness
    @Test
    public void testElementWitness() throws Exception {
        ArrayList<String> s = new ArrayList<>();
        s.add("a1");
        s.add("a2");
        s.add("a3");
        s.add("a4");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(s);

        for (String y : new String[]{"a1", "a2", "a3", "a4", "a5", "a6"}) {
            // 从文件a1.properties中读取参数初始化双线性群
            Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
            Element e = pairing.getZr().newElementFromHash(y.getBytes("utf-8"), 0, y.getBytes("utf-8").length);
            Witness witness = bma.getWitness(e);
            Boolean b = bma.testWitness(e, witness);
            System.out.println(y + "在集合中 " + b);
        }
    }

    //测试某个元素的non-witness
    @Test
    public void testElementNonWitness() throws Exception {
        ArrayList<String> s = new ArrayList<>();
        s.add("a1");
        s.add("a2");
        s.add("a3");
        s.add("a4");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(s);

        for (String y : new String[]{"a1", "a2", "a3", "a4", "a5", "a6"}) {
            // 从文件a1.properties中读取参数初始化双线性群
            Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
            Element e = pairing.getZr().newElementFromHash(y.getBytes("utf-8"), 0, y.getBytes("utf-8").length);
            NonWitness witness = bma.getNonWitness(e);
            Boolean b = bma.testNonWitness(e, witness);
            System.out.println(y + "不在集合中 " + b);
        }
    }

    //集合的witness
    @Test
    public void testSubSetWitness() throws Exception {
        ArrayList<String> s = new ArrayList<>();
        s.add("a1");
        s.add("a2");
        s.add("a3");
        s.add("a4");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(s);

        ArrayList<String> subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
        Witness witness = bma.getWitness(subSet);
        Boolean b = bma.testSubsetWitness(subSet, witness);
        System.out.println("{a1,a2}是子集 " + b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        witness = bma.getWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        System.out.println("{a1,a2，a3}是子集 " + b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        witness = bma.getWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        System.out.println("{a1,a2，a3,a4}是子集 " + b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        subSet.add("a5");
        witness = bma.getWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        System.out.println("{a1,a2，a3,a4,a5}是子集 " + b);

        subSet = new ArrayList<>();
        subSet.add("a6");
        subSet.add("a7");
        witness = bma.getWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        System.out.println("{a6,a7}是子集 " + b);
    }

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
}
