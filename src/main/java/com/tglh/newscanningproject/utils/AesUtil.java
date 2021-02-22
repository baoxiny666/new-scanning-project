package com.tglh.newscanningproject.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AesUtil {
    public static final String KEY = "87a1ec63db4c1d34";

    //偏移量，AES 128位数据块对应偏移量为16位
    public static final String VIPARA = "fe8a5d47f0bdf4f0";   //AES 128位数据块对应偏移量为16位

    //AES：加密方式   CBC：工作模式   PKCS5Padding：填充模式
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    private static final String AES = "AES";

    //编码方式
    public static final String CODE_TYPE = "UTF-8";


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {

        if (content == null || "".equals(content)) {
            return content;
        }

        try {
            /*
             * 新建一个密码编译器的实例，由三部分构成，用"/"分隔，分别代表如下
             * 1. 加密的类型(如AES，DES，RC2等)
             * 2. 模式(AES中包含ECB，CBC，CFB，CTR，CTS等)
             * 3. 补码方式(包含nopadding/PKCS5Padding等等)
             * 依据这三个参数可以创建很多种加密方式
             */
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);

            //偏移量
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));

            byte[] byteContent = content.getBytes(CODE_TYPE);

            //使用加密秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CODE_TYPE), AES);
            //SecretKeySpec skeySpec = getSecretKey(key);

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, zeroIv);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }


    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) {
        if (content == null || "".equals(content)) {
            return content;
        }

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CODE_TYPE), AES);
            //SecretKeySpec skeySpec = getSecretKey(key);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, zeroIv);

            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, CODE_TYPE);
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(AES);

            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(key.getBytes()));

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), AES);// 转换为AES专用密钥
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        String content = "hello, my name is mia, i like programming!";
        String key = KEY;
        System.out.println("content:" + content);
        String s1 = AesUtil.encrypt(content, key);
        System.out.println("result1:" + s1);
        System.out.println("result2:" + AesUtil.decrypt(s1, key));
        System.out.println("key : " + key);
        System.out.println("VIPARA-" + VIPARA);

    }

}

