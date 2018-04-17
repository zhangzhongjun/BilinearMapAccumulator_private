package com.zhong.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于双线性对的聚合器
 * <br>不管是什么类型的数据，都可以转化为string类型，所以我只需要处理String类型的数据即可
 * <br>对于群中的元素 首先使用toString转化为字符串
 *
 * @author 张中俊
 */
public class BilinearMapAccumulator implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static BilinearMapAccumulator_SiYao BM_siYao;
    private static BilinearMapAccumulator_GongYao BM_gongYao;
    private static Pairing pairing;

    static {
        BM_siYao = BilinearMapAccumulator_SiYao.getKey();
        BM_gongYao = BilinearMapAccumulator_GongYao.getKey();
        // 从文件a1.properties中读取参数初始化双线性群
        pairing = PairingFactory.getPairing("params/curves/a.properties");
    }

    /**
     * 要处理的集合
     */
    private List<SerializableElement> X = null;
    /**
     * 要处理的集合
     */
    private List<String> Y = null;
    /**
     * 计算得到的accumulatorValue
     */
    private SerializableElement accumulatorValue = null;
    /**
     * 该集合的 聚合值
     */
    private SerializableElement AkX = null;
    /**
     * 该集合的 累加值 是计算聚合值的一个中间结果
     */
    private SerializableElement FxK;

    /**
     *
     * 构造方法
     * <br>既然任何类型的数据都能转化为字符串类型，那么我们只需要处理字符串类型即可
     * <br>传入一个字符串类型的集合，构造出他的聚合器
     * @param Y
     *         要处理的集合
     *
     * @throws Exception
     *         异常
     */
    public BilinearMapAccumulator(List<String> Y) throws Exception {
        this.Y = Y;
        this.X = new ArrayList<SerializableElement>();

        // 将FxK初始化为Zr中的单位元
        Element temp = pairing.getZr().newOneElement();
        // 计算FxK
        for (String y : Y) {
            Element e = pairing.getZr().newElementFromHash(y.getBytes("utf-8"), 0, y.getBytes("utf-8").length);
            this.X.add(new SerializableElement(e));
            temp = temp.duplicate().mul(e.duplicate().add(BM_siYao.getS().getElement()));
        }
        FxK = new SerializableElement(temp);
        AkX = new SerializableElement(BM_siYao.getG().getElement().duplicate().powZn(FxK.getElement().duplicate()));
    }

    /**
     * 获得原始的集合
     *
     * @return 原始的集合，是一个String 类型的数组
     */
    public List<String> getY() {
        return Y;
    }

    /**
     * 获得元素y的witness
     * <br>使用该方法之前，要将需要检测的字符串映射到Zr域中的一个元素上
     *
     * @param y Zr域中的一个元素
     */

    /**
     * 获得元素y的witness
     * <br>使用该方法之前，要将需要检测的字符串映射到Zr域中的一个元素上
     *
     * @param y
     *         Zr域中的一个元素
     *
     * @return 该元素的witness
     */
    public Witness getWitness(Element y) {
        Element temp = pairing.getZr().newOneElement();
        for (SerializableElement x : X) {
            temp = temp.duplicate().mul(x.getElement().duplicate().sub(y.duplicate()));
        }
        Element zeroElem = pairing.getZr().newZeroElement();
        Element Uy = zeroElem.duplicate().sub(temp);
        Element Wy = BM_siYao.getG().getElement().duplicate().powZn(FxK.getElement().duplicate().add(Uy).duplicate().div(y.duplicate().add(BM_siYao.getS().getElement())));
        return new Witness(new SerializableElement(Uy), new SerializableElement(Wy));
    }

    /**
     * 测试元素y是否在集合中
     *
     * @param y
     *         要测试的元素，在Zr上
     * @param witness
     *         y对应的证据
     *
     * @return 是否在集合中
     */
    public boolean testWitness(Element y, Witness witness) {
        Element left = witness.getWy().getElement().duplicate();
        Element right = BM_siYao.getG().getElement().duplicate().powZn(y.duplicate()).duplicate().mul(BM_gongYao.getH().getElement().duplicate());

        Element e1 = pairing.pairing(left.duplicate(), right.duplicate());
        Element e2 = pairing.pairing(AkX.getElement().duplicate(), BM_siYao.getG().getElement().duplicate());

        return e1.isEqual(e2) && witness.getUy().getElement().isEqual(pairing.getZr().newZeroElement());
    }

    /**
     * 计算一个元素的nonwitness
     *
     * @param y
     *         要计算的元素，在Zr域中
     *
     * @return 该元素的nonwitness
     */
    public NonWitness getNonWitness(Element y) {
        Element temp = pairing.getZr().newOneElement();
        for (SerializableElement x : X) {
            temp = temp.duplicate().mul(x.getElement().duplicate().sub(y.duplicate()));
        }
        Element zeroElem = pairing.getZr().newZeroElement();
        Element Uy = zeroElem.duplicate().sub(temp);
        Element Wy = BM_siYao.getG().getElement().duplicate().powZn(FxK.getElement().duplicate().add(Uy).duplicate().div(y.duplicate().add(BM_siYao.getS().getElement())));
        return new NonWitness(new SerializableElement(Uy), new SerializableElement(Wy));
    }

    /**
     * 检测一个nonWitness是否成立
     *
     * @param y
     *         要检测的元素
     * @param witness
     *         证明
     *
     * @return 证据是否成立
     */
    public boolean testNonWitness(Element y, NonWitness witness) {
        Element left = witness.getWy().getElement().duplicate();
        Element right = BM_siYao.getG().getElement().duplicate().powZn(y.duplicate()).duplicate().mul(BM_gongYao.getH().getElement().duplicate());

        Element e1 = pairing.pairing(left.duplicate(), right.duplicate());
        Element e2 = pairing.pairing(AkX.getElement().duplicate(), BM_siYao.getG().getElement().duplicate());

        return !(e1.isEqual(e2) && witness.getUy().getElement().isEqual(pairing.getZr().newZeroElement()));
    }

    /**
     * 获得一个子集的evidence
     *
     * @param y
     *         要计算的集合
     *
     * @return 集合的evidence
     *
     * @throws Exception
     *         异常
     */
    public Witness getWitness(List<String> y) throws Exception {
        List<String> strs = new ArrayList<String>();
        // 求出y的差集
        strs.addAll(this.Y);
        strs.removeAll(y);

        Element temp = pairing.getZr().newOneElement();
        for (String str : strs) {
            Element e = pairing.getZr().newElementFromHash(str.getBytes("utf-8"), 0, str.getBytes("utf-8").length);
            temp = temp.duplicate().mul(e.duplicate().add(BM_siYao.getS().getElement()));
        }
        Element Wy = BM_siYao.getG().getElement().duplicate().powZn(temp.duplicate());
        return new Witness(null, new SerializableElement(Wy));
    }

    /**
     * 检测一个集合的nonWitness是否成立
     *
     * @param strs
     *         要判断的集合
     * @param witness
     *         要判断的集合的witness
     *
     * @return strs是否是子集
     *
     * @throws Exception
     *         异常
     */
    public boolean testSubsetWitness(List<String> strs, Witness witness) throws Exception {
        Element temp = pairing.getZr().newOneElement();
        for (String str : strs) {
            Element e = pairing.getZr().newElementFromHash(str.getBytes("utf-8"), 0, str.getBytes("utf-8").length);
            temp = temp.duplicate().mul(e.duplicate().add(BM_siYao.getS().getElement()));
        }
        Element Wy = BM_siYao.getG().getElement().duplicate().powZn(temp.duplicate());
        Element left = pairing.pairing(witness.getWy().getElement(), Wy);
        Element right = pairing.pairing(AkX.getElement(), BM_siYao.getG().getElement());

        return left.isEqual(right);
    }

}
