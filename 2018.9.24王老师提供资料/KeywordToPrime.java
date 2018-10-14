package util;

import java.math.BigInteger;

public class KeywordToPrime {

    public static BigInteger FindPrime(String w){
        boolean foundprime = false;
        BigInteger prime = BigInteger.ZERO;
        byte[] prime_temp = new byte[17];
        //byte[] prime_temp = new byte[33];
        byte[] w_md5 = Hash.MD5(w.getBytes());

        for(int i=1; i<=prime_temp.length/2; i++){
            prime_temp[i+prime_temp.length/2] = 0;
        }

        for(int i=0; i<prime_temp.length/2; i++){
            prime_temp[i+1] = w_md5[i];
        }

        prime_temp[0] = 0;

        int r = 0;

        while(!foundprime){
            byte[] r_md5 = Hash.MD5(String.valueOf(r).getBytes());

            for(int i=0; i<prime_temp.length/2; i++){
                prime_temp[i + prime_temp.length/2 + 1] = r_md5[i];
            }

            prime = new BigInteger(prime_temp);

            if(prime.isProbablePrime(8))
                foundprime = true;

            r = r + 1;
        }

        return prime;
    }
}
