package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class Hash {
	
	public static byte[] MD5(byte[] keyword){
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5"); // The output of MD5 are 16 bytes (128 bits).
	            md.update(keyword);
	            return md.digest();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	public static Element HashToZr(Pairing pairing, byte[] m){
		Element result = pairing.getZr().newElementFromHash(m, 0, m.length);
		return result;
	}
	
	public static byte[] HashToL(byte[] m){		//md5 output 16bytes
		 try {
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            mdInst.update(m);
	            byte[] md = mdInst.digest();
	            return md;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	public static Element HashToG1(Pairing pairing, byte[] m){
		Element result = pairing.getG1().newElementFromHash(m, 0, m.length);
		return result;
	}
	
    /**
     * 将字节数组转换成16进制字符串
     * @param buf
     * @return
     */
    public static String ByteToHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)//小于十前面补零
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    /**
     * 将16进制字符串转换成字节数组
     * @param src
     * @return
     */
    public static byte[] HexToByte(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);//取高位字节
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);//取低位字节
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }
}
