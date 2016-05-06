package ehc.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SmsSender {
	public static final String UNAUTHORIZED = "401 Unauthorized";
	public static final String BAD_REQUEST = "400 Bad Request";
	public static final String GOOD_REQUEST = "200 OK";

	private String user;
	private String pass;
	private String defaultNumberFrom;
	private String testingEnviromentMobileNumberTo;

	protected SmsSender(String user, String pass, String defaultNumberFrom, String testingEnviromentMobileNumberTo) {
		super();
		this.user = user;
		this.pass = pass;
		if (null != defaultNumberFrom) {
			this.defaultNumberFrom = defaultNumberFrom;
		}
		if (null != testingEnviromentMobileNumberTo) {
			this.testingEnviromentMobileNumberTo = testingEnviromentMobileNumberTo;
		}
	}

	protected String getUser() {
		return user;
	}

	protected String getPass() {
		return pass;
	}
	
	protected String getDefaultNumberFrom() {
		return defaultNumberFrom;
	}
	
	protected String getTestingEnviromentMobileNumberTo() {
		return testingEnviromentMobileNumberTo;
	}

	private List<String> getListOfMessageParameters(String messageFrom, String messageTo, String text) throws UnsupportedEncodingException {
		List<String> parameters = new ArrayList<String>();
		
		if (!StringUtils.isEmpty(user)) {
			parameters.add("user=" + user);
		}
		if (!StringUtils.isEmpty(pass)) {
			parameters.add("pass=" + pass);
		}
		if (null != getDefaultNumberFrom()) {
			parameters.add("from=" + URLEncoder.encode(getDefaultNumberFrom(), "UTF-8"));
		}
		else if (!StringUtils.isEmpty(messageFrom)) {
			parameters.add("from=" + URLEncoder.encode(messageFrom, "UTF-8"));
		}
		if (null != getTestingEnviromentMobileNumberTo()) {
			parameters.add("to=" + URLEncoder.encode(getTestingEnviromentMobileNumberTo(), "UTF-8"));
		}
		else if (!StringUtils.isEmpty(messageTo)) {
			// clean up the messageTo mobile number
			// allow only + and digits
			String regex = "[^\\+0-9]+";
			String replacement = "";
			String digitsAndPlus = messageTo.replaceAll(regex, replacement);
			parameters.add("to=" + URLEncoder.encode(digitsAndPlus, "UTF-8"));
		}
		if (!StringUtils.isEmpty(text)) {
			parameters.add("text=" + URLEncoder.encode(text, "UTF-8"));
		}
		// UNICODE:
		if (!StringUtils.isEmpty(text)) {
			parameters.add("t=UNICODE");
		}

		return parameters;
	}
	
	@SuppressWarnings("finally")
	public String sendMessage(String messageFrom, String messageTo, String text) throws IOException, MessagingException {
		String apiBaseUrl = "http://http.gtx-messaging.net/smsc.php?";
		List<String> parameters = new ArrayList<String>();
		String responseBody = null;

		parameters = getListOfMessageParameters(messageFrom, messageTo, text);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String requestUrl = apiBaseUrl + StringUtil.join("&", parameters);
			HttpGet httpget = new HttpGet(requestUrl);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			responseBody = httpclient.execute(httpget, responseHandler);
		} finally {
			httpclient.close();

			return responseBody;
		}
	}
}