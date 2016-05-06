package ehc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class URLBuilder {

	private final static String DEFAULT_ENCODING = "ISO-8859-1";
	private String encoding = DEFAULT_ENCODING;
	private String host;
	private int port = -1;
	private String path;
	private List<QueryParam> queryParams = new ArrayList<QueryParam>();
	private boolean encodingEnabled = true;

	public URLBuilder(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public URLBuilder(String host) {
		this.host = host;
	}

	public URLBuilder() {
	}

	public URLBuilder setEncoding(String enc) {
		this.encoding = enc;
		return this;
	}

	public URLBuilder setPath(String path) {
		this.path = path;
		return this;
	}
	
	public URLBuilder addPath(String added) {
		if (added == null)
			return this;
		if (path != null) {
			this.path += (path.endsWith("/") && added.startsWith("/"))
					? added.substring(1)
					: (!path.endsWith("/") && !added.startsWith("/"))
						? "/"+added
						: added;
		}
		else
			this.path = added;
		return this;
	}

	public void addQueryString(String queryString) {
		String[] paramExpressions = queryString.split("&");
		for (int i = 0; i < paramExpressions.length; i++) {
			QueryParam p = parseQueryParam(paramExpressions[i]);
			if (p != null)
				queryParams.add(p);
		}
	}

	public URLBuilder addParam(String name, String value) {
		queryParams.add(new QueryParam(name, value));
		return this;
	}
	
	public URLBuilder addParam(String name, int value) {
		queryParams.add(new QueryParam(name, value + ""));
		return this;
	}


	public void disableEncoding() {
		encodingEnabled = false;
	}

	public void enableEncoding() {
		encodingEnabled = true;
	}

	public String getQueryString() {
		StringBuffer buf = new StringBuffer();
		appendQueryString(buf);
		return buf.toString();
	}

	public String getURL() {
		StringBuffer buf = new StringBuffer();
		if (host != null) {
			buf.append(host);
		}
		if (port != -1) {
			buf.append(":");
			buf.append(port);
		}
		if (path != null) {
			buf.append(
					(buf.length() > 0)
						? (buf.toString().endsWith("/") && path.startsWith("/"))
							? path.substring(1)
							: (!buf.toString().endsWith("/") && !path.startsWith("/"))
								? "/"+path
								: path
						: path
			);
		}
		if (!queryParams.isEmpty()) {
			buf.append("?");
		}
		appendQueryString(buf);
		return buf.toString();
	}

	private void appendQueryString(StringBuffer buf) {
		for (Iterator<QueryParam> i = queryParams.iterator(); i.hasNext();) {
			QueryParam param = i.next();
			buf.append(param.toString());
			if (i.hasNext()) {
				buf.append("&");
			}
		}
	}

	public QueryParam parseQueryParam(String expr) {
		int pos = expr.indexOf('=');
		if (pos > 0 && pos < expr.length() - 1) {
			String name = expr.substring(0, pos);
			String value = expr.substring(pos + 1);
			return new QueryParam(name, value);
		}
		else
			return null;
	}

	public class QueryParam {
		private String name;
		private String value = "";

		public QueryParam(String name, String v) {
			this.name = name;
			if (v != null) {
				this.value = v;
			}
		}

		@Override
		public String toString() {
			try {
				String tmp = encodingEnabled ? URLEncoder.encode(value, encoding) : value;
				return name + "=" + tmp;
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
