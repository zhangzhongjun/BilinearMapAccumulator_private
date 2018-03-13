package com.zhong;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-03-13 11:28
 **/
public class BilinearMapAccumulatorTest {
    @Test
    public void t1() throws Exception {
        ArrayList<String> X = new ArrayList<String>();
        X.add("hello1");
        X.add("hello2");
        X.add("hello3");
        X.add("hello4");
        BilinearMapAccumulator bma = new BilinearMapAccumulator(X);
        Witness wit = bma.getWitness("hello1");
        System.out.println(bma.testFunc_isInSet("hello1",wit));

        wit = bma.getWitness("world");
        System.out.println(bma.testFunc_isInSet("world",wit));
    }

    @Test
    public void t2() throws Exception {
        ArrayList<String> X = new ArrayList<String>();
        X.add("hello1");
        X.add("hello2");
        X.add("hello3");
        X.add("hello4");

        ArrayList<String> Y = new ArrayList<String>();
        Y.add("hello1");
        Y.add("hello2");

        BilinearMapAccumulator bma = new BilinearMapAccumulator(X);
        Witness wit = bma.getWitness(Y);
        System.out.println(bma.testFunc_isSubSet(Y,wit));

        Y.clear();
        Y.add("hello1");
        Y.add("world");
        wit = bma.getWitness(Y);
        System.out.println(bma.testFunc_isSubSet(Y,wit));
    }


}
