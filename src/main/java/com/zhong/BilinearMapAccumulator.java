package com.zhong;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 证据，由两部分组成
 *
 * @author 张中俊
 */
class Witness implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SerializableElement Uy;
    private SerializableElement Wy;

    public Witness(SerializableElement uy, SerializableElement wy) {
        super();
        Uy = uy;
        Wy = wy;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public SerializableElement getUy() {
        return Uy;
    }

    public void setUy(SerializableElement uy) {
        Uy = uy;
    }

    public SerializableElement getWy() {
        return Wy;
    }

    public void setWy(SerializableElement wy) {
        Wy = wy;
    }

    @Override
    public String toString() {
        return "Uy= " + this.Uy.toString() + "\r\nWy= " + this.Wy.toString();
    }
}


/**
 * 基于双线性对的聚合器
 * <br>不管是什么类型的数据，都可以转化为string类型，所以我只需要处理String类型的数据即可
 *
 * @author zhang
 */
public class BilinearMapAccumulator implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 原始的集合  不管是什么类型的数据，都可以转化为string类型，所以我只需要处理String类型的数据即可
     */
    private List<String> Y = null;

    /**
     * 原始的集合转化为element集合 要处理的集合 是Element类型的
     */
    private List<SerializableElement> X = null;
    /**
     * 群的生成元
     */
    private SerializableElement g = null;
    /**
     * 计算得到的accumulatorValue
     */
    private SerializableElement accumulatorValue = null;
    /**
     * 该集合的FxK
     */
    private SerializableElement FxK = null;
    /**
     * 私钥
     */
    private SerializableElement s = null;

    public BilinearMapAccumulator(List<String> Y) throws Exception {
        this.Y = Y;
        this.X = new ArrayList<SerializableElement>();
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        // 从G1群中随机选一个元素
        g = new SerializableElement(pairing.getG1().newRandomElement());
        // 从Zr群中随机选择一个元素
        s = new SerializableElement(pairing.getZr().newRandomElement());
        // 将FxK初始化为Zr中的单位元
        Element temp = pairing.getZr().newOneElement();
        // 计算FxK
        for (String y : Y) {
            Element e = pairing.getZr().newElementFromHash(y.getBytes("utf-8"), 0, y.getBytes("utf-8").length);
            this.X.add(new SerializableElement(e));
            temp = temp.duplicate().mul(e.duplicate().add(s.getElement()));
        }
        FxK = new SerializableElement(temp);
    }

    /**
     * 计算accumulator value
     * @return 计算得到的accumulator值
     */
    public Element calAccumulatorValue() {
        if (accumulatorValue != null) {
            return accumulatorValue.getElement();
        }
        Element temp = g.getElement().duplicate().powZn(FxK.getElement());
        accumulatorValue = new SerializableElement(temp);
        return accumulatorValue.getElement();
    }

    /**
     * 计算一个元素的evidence
     *
     * @param y 要计算的元素
     * @return 该元素的evidence
     */
    private Witness getWitness(Element y) {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        Element temp = pairing.getZr().newOneElement();
        for (SerializableElement x : X) {
            temp = temp.duplicate().mul(x.getElement().duplicate().sub(y));
        }
        Element zeroElem = pairing.getZr().newZeroElement();
        Element Uy = zeroElem.duplicate().sub(temp);
        Element Wy = g.getElement().duplicate().powZn(FxK.getElement().duplicate().add(Uy).duplicate().div(y.duplicate().add(s.getElement())));
        return new Witness(new SerializableElement(Uy), new SerializableElement(Wy));
    }

    /**
     * 获得一个集合的evidence
     *
     * @param y 集合
     * @return 集合的evidence
     * @throws Exception 异常
     */
    public Witness getWitness(List<String> y) throws Exception {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        List<String> strs = new ArrayList<String>();
        strs.addAll(this.Y);
        strs.removeAll(y);

        Element temp = pairing.getZr().newOneElement();
        for (String str : strs) {
            Element e = pairing.getZr().newElementFromHash(str.getBytes("utf-8"), 0, str.getBytes("utf-8").length);
            temp = temp.duplicate().mul(e.duplicate().add(s.getElement()));
        }
        Element Wy = g.getElement().duplicate().powZn(temp.duplicate());
        return new Witness(null, new SerializableElement(Wy));
    }

    /**
     * 计算一个元素的evidence
     *
     * @param y 要计算的元素
     * @return 该元素的evidence
     */
    public Witness getWitness(String y) {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        Element e = pairing.getZr().newRandomElement();
        e.setFromHash(y.getBytes(), 0, y.getBytes().length);
        return getWitness(e);
    }

    /**
     * 通过evidence检验y是不是在集合内部
     *
     * @param y       要检验的元素
     * @param witness 该元素的evidence
     * @return "invalid" if the evidence is not valid;"in the set" if y is in
     * the set;"not in the set" if y is not in the set
     */
    private String testFunc(Element y, Witness witness) {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        Element h = g.getElement().duplicate().powZn(s.getElement());
        Element e1 = pairing.pairing(witness.getWy().getElement(), g.getElement().duplicate().powZn(y).duplicate().mul(h));
        Element e2 = pairing.pairing(calAccumulatorValue().duplicate().mul(g.getElement().duplicate().powZn(witness.getUy().getElement())),
                g.getElement().duplicate());
        if (!e1.isEqual(e2)) {
            return "invalid";
        }

        if (witness.getUy().getElement().isEqual(pairing.getZr().newZeroElement())) {
            return "in the set";
        } else {
            return "not in the set ";
        }
    }

    /**
     * 判断集合Y是否是X的子集
     *
     * @param Y    要判断的集合
     * @param witness 要判断的集合的witness
     * @return Y是否是子集
     * @throws Exception 异常
     */
    public boolean testFunc_isSubSet(List<String> Y, Witness witness) throws Exception {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        Element temp = pairing.getZr().newOneElement();
        for (String str : Y) {
            Element e = pairing.getZr().newElementFromHash(str.getBytes("utf-8"), 0, str.getBytes("utf-8").length);
            temp = temp.duplicate().mul(e.duplicate().add(s.getElement()));
        }
        Element Wy = g.getElement().duplicate().powZn(temp.duplicate());
        Element left = pairing.pairing(witness.getWy().getElement(), Wy);
        Element right = pairing.pairing(calAccumulatorValue(), g.getElement());

        return left.isEqual(right);
    }

    /**
     * 判断元素y是否在集合X中
     * @param y 要判断的元素
     * @param witness 证据
     * @return "invalid" if the evidence is not valid;"in the set" if y is in
     * the set;"not in the set" if y is not in the set
     */
    public String testFunc_isInSet(String y, Witness witness) {
        // 从文件a1.properties中读取参数初始化双线性群
        Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

        Element e = pairing.getZr().newRandomElement();
        e.setFromHash(y.getBytes(), 0, y.getBytes().length);
        return testFunc(e, witness);
    }
}
