package com.didawn.crypto;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static javax.crypto.Cipher.getInstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author fabier
 */
public class EncryptionUtil {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     *
     */
    private static final String RESOURCES_DIR = "src/main/resources/";

    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = "private.key";

    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = "public.key";

    private static final Logger logger = getLogger(EncryptionUtil.class.getName());

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Private.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     */
    public static void generateKey() {
	try {
	    // Create files to store public and private key
	    createFile(privateKeyFile());
	    createFile(publicKeyFile());

	    final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
	    keyGen.initialize(1_024);
	    final KeyPair key = keyGen.generateKeyPair();

	    try ( // Saving the Public key in a file
		    ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile()))) {
		publicKeyOS.writeObject(key.getPublic());
	    }

	    try ( // Saving the Private key in a file
		    ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile()))) {
		privateKeyOS.writeObject(key.getPrivate());
	    }
	} catch (IOException | NoSuchAlgorithmException ex) {
	    logger.log(SEVERE, null, ex);
	}
    }

    private static void createFile(File file) throws IOException {
	if (file.getParentFile() != null) {
	    file.getParentFile().mkdirs();
	}
	if (!file.createNewFile()) {
	    throw new IllegalStateException("Impossible to create file : " + file.getAbsolutePath());
	}
    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {
	return privateKeyFile().exists() && publicKeyFile().exists();
    }

    /**
     *
     * @param text
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static byte[] encrypt(String text)
	    throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
	    BadPaddingException, ClassNotFoundException, IOException {
	ObjectInputStream inputStream = new ObjectInputStream(
		EncryptionUtil.class.getResourceAsStream("/" + PUBLIC_KEY_FILE));
	final PublicKey publicKey = (PublicKey) inputStream.readObject();
	return encrypt(text, publicKey);
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *            : original plain text
     * @param key
     *            :The public key
     * @return Encrypted text
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt(String text, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException,
	    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	// get an RSA cipher object and print the provider
	final Cipher cipher = getInstance(ALGORITHM);
	// encrypt the plain text using the public key
	cipher.init(ENCRYPT_MODE, key);
	return cipher.doFinal(text.getBytes());
    }

    /**
     *
     * @param text
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static String decrypt(byte[] text) throws InvalidKeyException, IllegalBlockSizeException,
	    BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IOException {
	ObjectInputStream inputStream = new ObjectInputStream(
		EncryptionUtil.class.getResourceAsStream("/" + PRIVATE_KEY_FILE));
	final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
	return decrypt(text, privateKey);
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *            :encrypted text
     * @param key
     *            :The private key
     * @return plain text
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static String decrypt(byte[] text, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException,
	    InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
	// get an RSA cipher object and print the provider
	final Cipher cipher = getInstance(ALGORITHM);

	// decrypt the text using the private key
	cipher.init(DECRYPT_MODE, key);
	return new String(cipher.doFinal(text));
    }

    private static File resourceDir() {
	return new File(RESOURCES_DIR);
    }

    private static File privateKeyFile() {
	return new File(resourceDir(), PRIVATE_KEY_FILE);
    }

    private static File publicKeyFile() {
	return new File(resourceDir(), PUBLIC_KEY_FILE);
    }

    private EncryptionUtil() {
    }
}
