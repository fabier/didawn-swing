package com.didawn.di;

import static com.didawn.Constants.dawnDotCom;
import static com.didawn.Constants.key1;
import static com.didawn.Constants.key2;
import static java.text.MessageFormat.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.crypto.Cipher.getInstance;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author fabier
 */
public class Crypter {

    private static final Logger logger = getLogger(Crypter.class.getName());

    /**
     *
     * @param songId
     * @return
     */
    protected static byte[] getBlowfishKey(Long songId) {
        if (songId < 0L) {
            songId *= -1L;
        }

        String hash = md5Hex(iso88591bytes(songId.toString()));
        String part1 = hash.substring(0, 16);
        String part2 = hash.substring(16, 32);
        String[] data = new String[]{key1(), part1, part2};
        String keyStr = getXor(data, 16);
        return keyStr.getBytes();
    }

    private static String getXor(String[] data, int len) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < len; ++i) {
            int character = data[0].charAt(i);

            for (int j = 1; j < data.length; ++j) {
                character ^= data[j].charAt(i);
            }

            result.append((char) character);
        }

        return result.toString();
    }

    /**
     *
     * @param data
     * @param key
     * @return
     */
    protected static byte[] decryptBlowfish(byte[] data, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
            Cipher cipher = getInstance("Blowfish/CBC/NoPadding");
            cipher.init(2, keySpec, new IvParameterSpec(new byte[]{0, 1, 2, 3, 4, 5, 6, 7}));
            return cipher.doFinal(data);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            logger.log(SEVERE, "Impossible to decrypt Blowfish", e);
            return new byte[data.length];
        }
    }

    /**
     *
     * @param puid
     * @param format
     * @param id
     * @param mediaVersion
     * @return
     */
    public static String getDownloadURL(String puid, int format, String id, int mediaVersion) {
        try {
            String proxyLetter = puid.substring(0, 1);
            String separator = "¤";
            String data = puid + separator + format + separator + id + separator + mediaVersion;
            String dataHash = md5Hex(iso88591bytes(data));
            data = aes(dataHash + separator + data + separator);
            return data != null
                    ? format("http://e-cdn-proxy-{0}." + dawnDotCom() + "/mobile/1/{1}",
                            proxyLetter, data)
                    : null;
        } catch (Exception e) {
            throw new IllegalStateException("Impossible to get download URL", e);
        }
    }

    private static String aes(String clearText) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key2().getBytes(), "AES");
        Cipher cipher = getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, skeySpec);
        byte[] encrypted = cipher.doFinal(iso88591bytes(clearText));
        return printHexBinary(encrypted).toLowerCase();
    }

    private static byte[] iso88591bytes(String s) {
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Impossible to get bytes as ISO-8859-1");
        }
    }

    private Crypter() {
    }
}
