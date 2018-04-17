package com.zhong.utils;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;

/**
 * Util
 *
 * @author 张中俊
 */
public class MyUtils {
    /**
     * 双线性对
     */
    final public static Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");
    /**
     * AES加密时候使用的初始向量
     */
    private static final byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 获得项目某文件夹下的某文件
     *
     * @param dirname
     *         文件夹名
     * @param fileName
     *         文件名
     *
     * @return 该文件File对象
     */
    public static File getFile(String dirname, String fileName) {
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path, fileName);
        return file;
    }

    /**
     * 一个随机函数 {0,1}^lambda X {0,1}^lambda -&gt; {0,1}^lambda
     *
     * @param K
     *         密钥
     * @param msg
     *         字符串
     *
     * @return 随机映射的结果，长度为16的byte数组
     *
     * @throws Exception
     *         异常
     */
    public static byte[] F(final byte[] K, final String msg) throws Exception {
        return CryptoPrimitives.generateCmac(K, msg);
    }

    public static byte[] F(final byte[] K, final Element e) throws UnsupportedEncodingException {
        byte[] res = CryptoPrimitives.generateCmac(K, e.toString());
        return res;
    }

    /**
     * 一个随机函数 {0,1}^lambda X {0,1}^lambda -&gt; Zp*
     *
     * @param KI
     *         密钥
     * @param id
     *         文件名
     *
     * @return 随机映射的结果，长度为16的byte数组
     *
     * @throws Exception
     *         异常
     */
    public static Element Fp(final byte[] KI, String id) throws Exception {
        byte[] res = CryptoPrimitives.generateCmac(KI, id);
        Element e = pairing.getZr().newElement();
        e.setFromBytes(res);
        return e;
    }

    /**
     * 将对象转化为数组
     *
     * @param o
     *         待转化的对象
     *
     * @return byte[]数组
     */
    public static byte[] msg2Byte(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 将byte[]数组转化为对象
     *
     * @param bytes
     *         待转化的数组
     *
     * @return 转化为的对象
     */
    public static Object byte2Msg(byte[] bytes) {
        ByteArrayInputStream bais;
        ObjectInputStream in = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);

            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获得一个测试数据集<br>
     * keyword1: keyword1_filename1,keyword1_filename2,keyword1_filename3<br>
     * keyword2: keyword2_filename1,keyword2_filename2,keyword2_filename3<br>
     * keyword3: keyword3_filename1,keyword3_filename2,keyword3_filename3
     *
     * @return 测试数据集
     */
    public static Map<String, Collection<String>> getAToyDB() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        kf.put("keyword1", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword2", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        kf.put("keyword3", filenames);

        return kf;
    }

    /**
     * 获得测试数据集
     *
     * @return 测试数据集
     */
    public static Map<String, Collection<String>> getAToyDB2() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        kf.put("keyword1", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword2", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword3", filenames);

        return kf;
    }

    /**
     * 获得测试数据集
     *
     * @return 测试数据集
     */
    public static Map<String, Collection<String>> getAToyDB3() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("b80aee33-0aa9-4909-9e69-7f14a1fa0be5.txt");
        filenames.add("bac0ade3-d75a-46b1-a649-4539818c696b.txt");
        filenames.add("368ff846-e651-47cd-9091-e40b6a4e786e.txt");
        kf.put("zalk", filenames);

        filenames = new ArrayList<String>();
        filenames.add("4915af53-89f8-45c3-93b9-560202171899.txt");
        filenames.add("b0f910a5-e242-4649-809b-cb6a89fff76f.txt");
        filenames.add("31e3a41d-ddd9-4966-bffa-070551b66d63.txt");
        filenames.add("c120b771-cc8f-4f22-b8c8-1bfcd83dd890.txt");
        kf.put("zamarippa", filenames);

        return kf;
    }

    /**
     * 使用CBC模式下的AES加密算法加密
     *
     * @param Ke
     *         密钥 16 byte
     * @param mingWen
     *         要加密的明文
     *
     * @return 加密之后得到的密文
     *
     * @throws IOException
     *         异常
     * @throws InvalidAlgorithmParameterException
     *         异常
     * @throws NoSuchAlgorithmException
     *         异常
     * @throws NoSuchPaddingException
     *         异常
     * @throws NoSuchProviderException
     *         异常
     * @throws InvalidKeyException
     *         异常
     */
    public static byte[] encrypt_AES_CBC(byte[] Ke, String mingWen) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException {
        final byte[] miWen = CryptoPrimitives.encryptAES_CBC(Ke, ivBytes, mingWen.getBytes("utf-8"));
        return miWen;
    }

    /**
     * 使用CBC模式下的AES加密算法解密
     *
     * @param Ke
     *         解密密钥
     * @param miWen
     *         需要解密的密文
     *
     * @return 解密之后得到的明文
     *
     * @throws InvalidAlgorithmParameterException
     *         异常
     * @throws NoSuchAlgorithmException
     *         异常
     * @throws NoSuchPaddingException
     *         异常
     * @throws NoSuchProviderException
     *         异常
     * @throws InvalidKeyException
     *         异常
     * @throws IOException
     *         异常
     */
    public static String decrypt_AES_CBC(byte[] Ke, byte[] miWen) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        byte[] s = CryptoPrimitives.decryptAES_CBC(miWen, Ke);
        return new String(s, "utf-8");
    }

}
