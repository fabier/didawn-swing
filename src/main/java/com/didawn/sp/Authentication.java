package com.didawn.sp;

/**
 *
 * @author fabier
 */
public class Authentication {

    private static final Authentication INSTANCE = new Authentication();

    /**
     *
     * @return
     */
    public static Authentication getInstance() {
	return INSTANCE;
    }

    private SpAuthResponse spitofyAuthResponse;

    private Authentication() {
    }

    /**
     *
     * @return
     */
    public SpAuthResponse getSpitofyAuthResponse() {
	return spitofyAuthResponse;
    }

    /**
     *
     * @param spitofyAuthResponse
     */
    public void setSpitofyAuthResponse(SpAuthResponse spitofyAuthResponse) {
	this.spitofyAuthResponse = spitofyAuthResponse;
    }
}
