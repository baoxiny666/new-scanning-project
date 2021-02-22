package com.tglh.newscanningproject.utils;

import java.security.MessageDigest;

public class Md5 {
	 public static String encrypt32(String encryptStr) {
	        MessageDigest md5;
	        try {
	            md5 = MessageDigest.getInstance("MD5");
	            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
	            StringBuffer hexValue = new StringBuffer();
	            for (int i = 0; i < md5Bytes.length; i++) {
	                int val = ((int) md5Bytes[i]) & 0xff;
	                if (val < 16)
	                    hexValue.append("0");
	                hexValue.append(Integer.toHexString(val));
	            }
	            encryptStr = hexValue.toString();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return encryptStr;
	    }


	    public static String encrypt16(String encryptStr) {
	        return encrypt32(encryptStr).substring(8, 24);
	    }

	    public static void main(String[] args) {
	        String encryptStr = "123456";
	        System.out.println(Md5.encrypt32(encryptStr));//39596dfee3b2b8409147bff7d9a6269b
	        System.out.println(Md5.encrypt16(encryptStr));//e3b2b8409147bff7
	    }
}
