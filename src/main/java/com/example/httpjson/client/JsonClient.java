package com.example.httpjson.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 单例模式的json http post请求
 * 
 * @author liuzhihu
 *
 */
public final class JsonClient {

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	private static JsonClient instance = new JsonClient();

	private JsonClient() {

	}

	public static JsonClient getInstance() {
		return instance;
	}

	/**
	 * json 发送http post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("deprecation")
	public String httpPostWithJSON(String url, String json) throws ParseException, IOException {
		CloseableHttpResponse response = null;

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(url);

		// 设置超时时间
		Integer timeout = 2000;

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		StringEntity se = new StringEntity(json);
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
		httpPost.setEntity(se);
		response = httpClient.execute(httpPost);

		HttpEntity entity = null;
		String responseContent = null;
		if (null != response) {
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity);
			response.close();
		}

		return responseContent;
	}
}
