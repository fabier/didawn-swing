/*
 * Copyright (C) 2017 fabier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.didawn.crypto;

import static com.didawn.crypto.EncryptionUtil.areKeysPresent;
import static com.didawn.crypto.EncryptionUtil.decrypt;
import static com.didawn.crypto.EncryptionUtil.encrypt;
import static com.didawn.crypto.EncryptionUtil.generateKey;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author fabier
 */
public class EncryptionUtilTest {

    /**
     *
     */
    public EncryptionUtilTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void test() {
	testEncryptAndDecrypt("");
    }

    private void testEncryptAndDecrypt(String message) {
	try {

	    // Check if the pair of keys are present else generate those.
	    if (!areKeysPresent()) {
		// Method generates a pair of keys using the RSA algorithm and stores it
		// in their respective files
		generateKey();
	    }

	    // Encrypt the string using the public key
	    final byte[] cipherText = encrypt(message);

	    decrypt(cipherText);

	    // Printing the Original, Encrypted and Decrypted Text
	    LOG.log(Level.INFO, "Original: " + message);
	    LOG.log(Level.INFO, "Encrypted: " + encodeHexString(cipherText));

	} catch (Exception e) {
	}
    }

    private static final Logger LOG = Logger.getLogger(EncryptionUtilTest.class.getName());
}
