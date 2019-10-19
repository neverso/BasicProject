package com.android.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:DES加解密工具，CBC模式，CBC模式需要IV，DES密钥长度8位
 */
public class DESCBCUtil {

    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "DES";
    private static final String CHARSET = "UTF-8";

    /**
     * 对字符串进行DES加密
     *
     * @param plaintext 明文
     * @param password  密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String plaintext, String password) {
        try {
            //创建cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            //初始化cirpher
            SecretKeyFactory kf = SecretKeyFactory.getInstance(ALGORITHM);
            DESKeySpec keySpec = new DESKeySpec(password.getBytes(CHARSET));
            Key key = kf.generateSecret(keySpec);
            //加密模式
            IvParameterSpec iv = new IvParameterSpec(password.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            //加密
            byte[] encrypt = cipher.doFinal(plaintext.getBytes(CHARSET));
            //通过Base64解决乱码问题
            return Base64.encode(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对字符串进行DES解密
     *
     * @param ciphertext 密文
     * @param password   密钥
     * @return 解密得到的字符串
     */
    public static String decrypt(String ciphertext, String password) {
        try {
            //创建cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            //初始化cirpher
            SecretKeyFactory kf = SecretKeyFactory.getInstance(ALGORITHM);
            DESKeySpec keySpec = new DESKeySpec(password.getBytes(CHARSET));
            Key key = kf.generateSecret(keySpec);
            //解密模式
            IvParameterSpec iv = new IvParameterSpec(password.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            //解密
            byte[] decrypt = cipher.doFinal(Base64.decode(ciphertext));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("===============DES对称加密(CBC模式)===============");
        String data = "加密工具aBc123";
        String pwd = "12345678";
        String encryptData = encrypt(data, pwd);
        System.out.println("密文：" + encryptData);
        System.out.println("明文：" + decrypt(encryptData, pwd));
    }

}
