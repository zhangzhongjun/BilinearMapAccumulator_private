package com.zhong;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Test;

public class SerializableElementTest {
	@Test
	public void t1() {
		/**
		 * 双线性对
		 */
		Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
		Element e1 = pairing.getZr().newRandomElement();
		Element e2 = pairing.getG1().newRandomElement();
		Element e3 = pairing.getG2().newRandomElement();
		Element e4 = pairing.getGT().newRandomElement();

		SerializableElement se = new SerializableElement(e1);
		SerializationDemonstrator.serialize(se, "F:\\a.txt");
		SerializableElement sese = SerializationDemonstrator.deserialize("F:\\a.txt");
		System.out.println(e1.isEqual(sese.getElement()));

		se = new SerializableElement(e2);
		SerializationDemonstrator.serialize(se, "F:\\a.txt");
		sese = SerializationDemonstrator.deserialize("F:\\a.txt");
		System.out.println(e2.isEqual(sese.getElement()));

		se = new SerializableElement(e3);
		SerializationDemonstrator.serialize(se, "F:\\a.txt");
		sese = SerializationDemonstrator.deserialize("F:\\a.txt");
		System.out.println(e3.isEqual(sese.getElement()));

		se = new SerializableElement(e4);
		SerializationDemonstrator.serialize(se, "F:\\a.txt");
		sese = SerializationDemonstrator.deserialize("F:\\a.txt");
		System.out.println(e4.isEqual(sese.getElement()));

	}

	@Test
	public void t3() {
		/**
		 * 双线性对
		 */
		Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
		Element e = pairing.getZr().newRandomElement();
		System.out.println(e);
		byte[] arr = e.toBytes();
		System.out.println(arr.length);
		Element e1 = pairing.getZr().newRandomElement();
		e1.setFromBytes(arr);
		System.out.println(e1);
	}
}
