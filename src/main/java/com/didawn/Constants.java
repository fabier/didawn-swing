package com.didawn;

import static com.didawn.crypto.EncryptionUtil.decrypt;
import static org.apache.commons.codec.binary.Hex.decodeHex;

/**
 *
 * @author fabier
 */
public class Constants {

    private static final String K1 = "1b3f53471d30ad5fc95a86d8504d39c4bf938b58d6745d1bf80d7afd90830a0fd01bf5f8d9f16da99"
	    + "37c3a99808edebe11627e79ee2b236b8bfc666c6da2d5504b445efa70ccc2a732c828d47406010c0b4329fbcc372e7bced9c5ceb"
	    + "9b0b2e5e7709359e9f9b75fa3da0e28211951397c431dd4bb566558502a95640d5d5eb1";

    private static final String K2 = "5a1103a0a46e15cace986fa3345e6fdc79bc71c3c27c18e5c754ace61b0d8aa05382588abbe43261a"
	    + "f7e66997f933ec217447734d59d124807cd282747dc7d97e0c49f0fd4c042d5a7dcc57d7729994c195a98418b0c84a3afb5db1c1"
	    + "d9d48b53722a8ecaf8b357e8da827a08350b28be357d2c0ad3e28c1d997f845cfe1c4e1";

    private static final String DATA = "316834980a0d7b0a71f09db50f415186522b760f9412fbdd84bbf6ba1423dad291343240b1dbe00"
	    + "3b663afbcb6ba7a68becb56321c7603d5176b72f72ade77ec522f301f897cf96da0fc3da8dea6421c903816ffb88c52dac0d1a71"
	    + "cfbf0ca797936aca1287b6592273151a84df02a6b3dc748bc04089d19d446f6c8a6a40ff1";

    /**
     *
     * @return
     */
    public static String key1() {
	try {
	    byte[] data = decodeHex(K1.toCharArray());
	    return decrypt(data);
	} catch (Exception ex) {
	    throw new IllegalStateException("Impossible fo find key1()", ex);
	}
    }

    /**
     *
     * @return
     */
    public static String key2() {
	try {
	    byte[] data = decodeHex(K2.toCharArray());
	    return decrypt(data);
	} catch (Exception ex) {
	    throw new IllegalStateException("Impossible fo find key2()", ex);
	}
    }

    /**
     *
     * @return
     */
    public static String dawn() {
	try {
	    byte[] data = decodeHex(DATA.toCharArray());
	    return decrypt(data);
	} catch (Exception ex) {
	    throw new IllegalStateException("Impossible fo find dawn()", ex);
	}
    }

    /**
     *
     * @return
     */
    public static String dawnDotCom() {
	return dawn() + ".com";
    }

    private Constants() {
    }
}
