package com.didawn.sp;

import static com.didawn.sp.AuthenticateHelper.REDIRECT_URI;
import static com.didawn.sp.AuthenticateHelper.clientId;
import static com.didawn.sp.AuthenticateHelper.clientSecret;
import static com.didawn.sp.Authentication.getInstance;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.http.impl.client.HttpClientBuilder.create;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 *
 * @author fabier
 */
public class CallbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1_850_048_144_803_066_410L;

    private static final Logger logger = Logger.getLogger(CallbackServlet.class.getName());
    private static final String API_TOKEN_URL = "https://accounts.spotify.com/api/token";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	String code = request.getParameter("code");
	String error = request.getParameter("error");
	request.getParameter("state");

	try {
	    if (code != null || error != null) {
		if (error != null) {
		    respond(response, "Authentication failed : " + error);
		} else {
		    HttpClient client = create().build();
		    HttpPost post = new HttpPost(API_TOKEN_URL);

		    // add header
		    String auth = encodeBase64String((clientId() + ":" + clientSecret()).getBytes());
		    post.setHeader("Authorization", "Basic " + auth);

		    List<NameValuePair> urlParameters = new ArrayList<>();
		    urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
		    urlParameters.add(new BasicNameValuePair("code", code));
		    urlParameters.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));

		    post.setEntity(new UrlEncodedFormEntity(urlParameters));

		    HttpResponse httpResponse = client.execute(post);

		    JsonReader reader = new JsonReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		    SpAuthResponse spitofyAuthResponse = new Gson().fromJson(reader, SpAuthResponse.class);

		    Authentication authInstance = getInstance();
		    authInstance.setSpitofyAuthResponse(spitofyAuthResponse);

		    respond(response,
			    "<html>\n" + "<head>\n" + "<script>\n" + "function loaded()\n" + "{\n"
				    + "    window.setTimeout(CloseMe, 1);\n" + "}\n" + "\n" + "function CloseMe() \n"
				    + "{\n" + "    window.close();\n" + "}\n" + "</script>\n" + "</head>\n"
				    + "<body onLoad=\"loaded()\">\n" + "<h1>Successful login</h1>\n" + "</body>");

		    synchronized (authInstance) {
			authInstance.notifyAll();
		    }
		}
	    } else {
		response.sendError(SC_NOT_FOUND);
	    }
	} catch (Exception e) {
	    logger.log(Level.INFO, "Impossible to handle callback response", e);
	}
    }

    private void respond(HttpServletResponse response, String message) throws IOException {
	response.setContentType("text/html; charset=utf-8");
	response.setStatus(SC_OK);
	try (Writer writer = response.getWriter()) {
	    writer.write(message);
	}
    }
}
