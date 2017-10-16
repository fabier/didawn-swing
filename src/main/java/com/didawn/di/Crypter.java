package com.didawn.di;

import com.didawn.Constants;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Crypter {

    private static final Logger log = Logger.getLogger(Crypter.class.getName());
    private static final MessageDigest md5digest;

    protected static byte[] getBlowfishKey(Long songId) {
        if (songId < 0L) {
            songId = songId * -1L;
        }

        String hash = md5(songId.toString());
        String part1 = hash.substring(0, 16);
        String part2 = hash.substring(16, 32);
        String[] data = new String[] { Constants.KEY1, part1, part2 };
        String keyStr = getXor(data, 16);
        return keyStr.getBytes();
    }

    private static String getXor(String[] data, int len) {
        String result = "";

        for (int i = 0; i < len; ++i) {
            int character = data[0].charAt(i);

            for (int j = 1; j < data.length; ++j) {
                character ^= data[j].charAt(i);
            }

            result = result + (char) character;
        }

        return result;
    }

    protected static byte[] decryptBlowfish(byte[] data, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/CBC/NoPadding");
            cipher.init(2, keySpec, new IvParameterSpec(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 }));
            return cipher.doFinal(data);
        } catch (Exception var4) {
            var4.printStackTrace();
            return new byte[data.length];
        }
    }

    public static String md5(String s) {
        try {
            return bytesToHexString(md5digest.digest(s.getBytes("ISO-8859-1")));
        } catch (Exception var2) {
            throw new RuntimeException("no UTF-8 decoder available", var2);
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                result.append('0');
            }

            result.append(hex);
        }

        return result.toString();
    }

    public static String getDownloadURL(String puid, int format, String id, int mediaVersion) {
        String proxyLetter = puid.substring(0, 1);
        String separator = "¤";
        String data = puid + separator + format + separator + id + separator + mediaVersion;
        String dataHash = md5(data);
        data = aes(dataHash + separator + data + separator);
        return data != null ? MessageFormat.format("http://e-cdn-proxy-{0}." + Constants.dawnDotCom() + "/mobile/1/{1}", proxyLetter, data) : null;
    }

    private static String aes(String clearText) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Constants.KEY2.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(clearText.getBytes("ISO-8859-1"));
            return DatatypeConverter.printHexBinary(encrypted).toLowerCase();
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | UnsupportedEncodingException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    static {
        try {
            md5digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            throw new RuntimeException("no MD5 digester", var1);
        }
    }
}
