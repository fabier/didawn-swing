package com.didawn.sp;

import static com.didawn.crypto.EncryptionUtil.decrypt;
import static com.didawn.sp.Authentication.getInstance;
import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;
import static java.awt.Desktop.Action.BROWSE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static javafx.application.Application.launch;
import static javafx.application.Platform.exit;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author fabier
 */
@SuppressWarnings("restriction")
public class AuthenticateHelper {

    private static final Logger LOGGER = getLogger(AuthenticateHelper.class.getName());

    /**
     *
     */
    public static final String CLIENT_ID_CRYPTED = "2379081cc9a74de975cbae1bf80b1d17a8028528228b709bb36211eab6e4bbb0f0c"
	    + "e2c7d78208197360b3a3ef8d6c7d375eac600de73f936b2c841d0548dbd15e379f3347f3cd5c336408230e160c4d078cfee7d211"
	    + "47b17fe6c73f222d77ddac6ef8ed85c2c9816e44a0ab1595679cfc1e64415af3da8a51f435868e1dd691c";

    /**
     *
     */
    public static final String CLIENT_SECRET_CRYPTED = "3c7441bc82d818c71db0f028053ab2d755c9a97a6de0dc1586bf2e0ccee5bae"
	    + "11e9cb9a776005f9657dbdd4e98848223183bdce33664ae1ad4e3d5371a054c77aff4c7531379fd0f90755c69da51797cbd1c69a"
	    + "da43ff029cd6a654933ab9cfb7ecd0dde7afcdc90abbc649439ae4d8ef303b2f044eb6b617f499865aca299dd";

    /**
     *
     */
    public static final String REDIRECT_URI = "http://localhost:8080";

    /**
     *
     */
    public static final String API_AUTHORIZE_URL = "https://accounts.spotify.com/authorize";

    private static final boolean USE_PURE_JAVA_IMPLEMENTATION = false;
    private static final boolean ALWAYS_ASK_TO_LOGIN = false;

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	SpAuthResponse authenticate = authenticate(USE_PURE_JAVA_IMPLEMENTATION);
	LOGGER.log(INFO, "authenticate = {0}", authenticate.getAccessToken());
	SpHelper helper = new SpHelper(authenticate.getAccessToken());
	helper.getInfoAboutMe();
	helper.getInfoAboutMyPlaylists();
    }

    /**
     *
     * @return
     */
    public static String clientId() {
	try {
	    return decrypt(CLIENT_ID_CRYPTED.getBytes());
	} catch (Exception e) {
	    throw new IllegalStateException("Impossible to get client_id", e);
	}
    }

    /**
     *
     * @return
     */
    public static String clientSecret() {
	try {
	    return decrypt(CLIENT_SECRET_CRYPTED.getBytes());
	} catch (Exception e) {
	    throw new IllegalStateException("Impossible to get client_secret", e);
	}
    }

    /**
     *
     * @param usePureJava
     * @return
     * @throws Exception
     */
    public static SpAuthResponse authenticate(boolean usePureJava) throws Exception {
	Server server = launchLocalServer();
	callSpitofyGetURL();
	Authentication authInstance = getInstance();
	synchronized (authInstance) {
	    while (authInstance.getSpitofyAuthResponse() == null) {
		authInstance.wait();
	    }
	}
	if (usePureJava) {
	    exit();
	}
	server.stop();
	return authInstance.getSpitofyAuthResponse();
    }

    private static Server launchLocalServer() throws Exception {
	Server server = new Server(8_080);
	ServletHandler servletHandler = new ServletHandler();
	servletHandler.addServletWithMapping(CallbackServlet.class, "/*");
	server.setHandler(servletHandler);
	server.start();
	return server;
    }

    private static void callSpitofyGetURL() throws URISyntaxException {
	URIBuilder builder = new URIBuilder(API_AUTHORIZE_URL);
	builder.addParameter("response_type", "code");
	builder.addParameter("client_id", clientId());
	builder.addParameter("scope", "playlist-read-private user-library-read");
	if (ALWAYS_ASK_TO_LOGIN) {
	    builder.addParameter("show_dialog", "true");
	}
	builder.addParameter("redirect_uri", REDIRECT_URI);
	URI uri = builder.build();
	openWebpage(uri);
    }

    /**
     *
     * @param uri
     */
    public static void openWebpage(URI uri) {
	if (USE_PURE_JAVA_IMPLEMENTATION) {
	    new Thread(() -> launch(WebViewSample.class, uri.toString())).start();
	} else {
	    Desktop desktop = isDesktopSupported() ? getDesktop() : null;
	    if (desktop != null && desktop.isSupported(BROWSE)) {
		try {
		    desktop.browse(uri);
		} catch (IOException e) {
		    LOGGER.log(INFO, "Impossible to open browser", e);
		}
	    }
	}
    }
}
