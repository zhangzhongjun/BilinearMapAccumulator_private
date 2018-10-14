package com.zhong.accumulator;


import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author 张中俊
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
        System.out.println("g = "+siYao.getG());

        BilinearMapAccumulator_GongYao.generateKey(siYao,10);

        BilinearMapAccumulator_GongYao gongYao = BilinearMapAccumulator_GongYao.getKey();
        System.out.println("hs[0] = " + gongYao.getHs().get(0));
        System.out.println("hs[1] = " + gongYao.getHs().get(1));
        System.out.println("hs[2] = " + gongYao.getHs().get(2));
        System.out.println("hs[3] = " + gongYao.getHs().get(3));
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

        for (String y : new String[]{"a1", "a2", "a3", "a4"}) {
            Witness witness = bma.getElementWitness(y);
            Boolean b = bma.testElementWitness(y, witness);
            Assert.assertTrue(b);
        }

        for(String y : new String[]{"a5", "a6"}){
            Witness witness = bma.getElementWitness(y);
            Boolean b = bma.testElementWitness(y, witness);
            Assert.assertFalse(b);
        }
    }


    //测试某个元素的witness
    @Test
    public void testElementWitness2() throws Exception {
        ArrayList<String> s = new ArrayList<>();
        s.add("a1");
        s.add("a2");
        s.add("a3");
        s.add("a4");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(s);

        for (String y : new String[]{"a1", "a2", "a3", "a4"}) {
            Witness witness = bma.getElementWitness2(y);
            Boolean b = bma.testElementWitness(y, witness);
            Assert.assertTrue(b);
        }

        for(String y : new String[]{"a5", "a6"}){
            Witness witness = bma.getElementWitness2(y);
            Boolean b = bma.testElementWitness(y, witness);
            Assert.assertFalse(b);
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

        for (String y : new String[]{"a1", "a2", "a3", "a4"}) {
            NonWitness witness = bma.getElementNonWitness(y);
            Boolean b = bma.testElementNonWitness(y, witness);
            Assert.assertFalse(b);
        }
        for(String y : new String[]{"a5", "a6"}){
            NonWitness witness = bma.getElementNonWitness(y);
            Boolean b = bma.testElementNonWitness(y, witness);
            Assert.assertTrue(b);
        }
    }

    //测试某个元素的non-witness
    @Test
    public void testElementNonWitness2() throws Exception {
        ArrayList<String> s = new ArrayList<>();
        s.add("a1");
        s.add("a2");
        s.add("a3");
        s.add("a4");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(s);

        for (String y : new String[]{"a1", "a2", "a3", "a4"}) {
            NonWitness witness = bma.getElementNonWitness2(y);
            Boolean b = bma.testElementNonWitness(y, witness);
            Assert.assertFalse(b);
        }
        for(String y : new String[]{"a5", "a6"}){
            NonWitness witness = bma.getElementNonWitness2(y);
            Boolean b = bma.testElementNonWitness(y, witness);
            Assert.assertTrue(b);
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
        Witness witness = bma.getSubsetWitness(subSet);
        Boolean b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        witness = bma.getSubsetWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        witness = bma.getSubsetWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3,a4}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        subSet.add("a5");
        witness = bma.getSubsetWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3,a4,a5}是子集 " + b);
        Assert.assertFalse(b);

        subSet = new ArrayList<>();
        subSet.add("a6");
        subSet.add("a7");
        witness = bma.getSubsetWitness(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a6,a7}是子集 " + b);
        Assert.assertFalse(b);
    }


    //集合的witness
    @Test
    public void testSubSetWitness2() throws Exception {
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
        Witness witness = bma.getSubsetWitness2(subSet);
        Boolean b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        witness = bma.getSubsetWitness2(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        witness = bma.getSubsetWitness2(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3,a4}是子集 " + b);
        Assert.assertTrue(b);

        subSet = new ArrayList<>();
        subSet.add("a1");
        subSet.add("a2");
        subSet.add("a3");
        subSet.add("a4");
        subSet.add("a5");
        witness = bma.getSubsetWitness2(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a1,a2，a3,a4,a5}是子集 " + b);
        Assert.assertFalse(b);

        subSet = new ArrayList<>();
        subSet.add("a6");
        subSet.add("a7");
        witness = bma.getSubsetWitness2(subSet);
        b = bma.testSubsetWitness(subSet, witness);
        //System.out.println("{a6,a7}是子集 " + b);
        Assert.assertFalse(b);
    }
}
