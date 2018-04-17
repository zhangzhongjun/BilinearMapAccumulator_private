package com.zhong.accumulator;

import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;
import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;

/**
 * 基于双线性对的聚合器的公钥
 * <br>实际上应该有n个，g^s g^s^2  g^s^3 ....
 *
 * @author 张中俊
 **/
public class BilinearMapAccumulator_GongYao implements Serializable {
    /**
     * 公钥
     */
    private SerializableElement h = null;


    public BilinearMapAccumulator_GongYao(SerializableElement h) {
        this.h = h;
    }

    /**
     * 根据用户提供的私钥生成对应的公钥并序列化到文本文件中
     *
     * @param siYao
     *         私钥
     */
    public static void generateKey(BilinearMapAccumulator_SiYao siYao) {
        Element e = siYao.getG().getElement().duplicate().powZn(siYao.getS().getElement().duplicate()).duplicate();
        SerializableElement h = new SerializableElement(e);

        BilinearMapAccumulator_GongYao bk = new BilinearMapAccumulator_GongYao(h);

        String bilinearMapAccumulator_KeyFile = MyUtils.getFile("keys", "BilinearMapAccumulator_GongYao.key").getAbsolutePath();
        SerializationDemonstrator.serialize(bk, bilinearMapAccumulator_KeyFile);
    }

    /**
     * 从文本文件中反序列化出公钥
     *
     * @return 公钥
     */
    public static BilinearMapAccumulator_GongYao getKey() {
        String bilinearMapAccumulator_KeyFile = MyUtils.getFile("keys", "BilinearMapAccumulator_GongYao.key").getAbsolutePath();
        return SerializationDemonstrator.deserialize(bilinearMapAccumulator_KeyFile);
    }

    public SerializableElement getH() {
        return h;
    }
}
