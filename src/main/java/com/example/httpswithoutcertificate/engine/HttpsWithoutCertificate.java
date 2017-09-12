package com.example.httpswithoutcertificate.engine;

import java.net.URI;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * HTTPS or HTTP 无证书请求
 * 
 * @author liuzhihu
 *
 */
@SuppressWarnings("deprecation")
public class HttpsWithoutCertificate {

	/**
	 * 请求https的地址
	 */
	private static final String userCheckURL = "https://zt.wps.cn/api/migu/check_relation?userId=1100801234&serviceId=760000028200&webId=1234&area=250";

	/**
	 * 免证书验证
	 * 
	 * @return
	 */
	public static String getHttpsWithoutCertificate() {

		// 信任所有的证书
		CloseableHttpClient client = createSSLClientDefault();// jdk1.6需要进行简单的配置才行

		// 使用http的get方式
		HttpGet httpGet = new HttpGet();

		// 设置连接超时时间 秒
		int timeout = 2000;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpGet.setConfig(requestConfig);

		String result = "";

		try {
			httpGet.setURI(new URI(userCheckURL));
			HttpResponse response = client.execute(httpGet);

			@SuppressWarnings("unused")
			int statusCode = response.getStatusLine().getStatusCode();// 接口响应的状态值
																		// 200
																		// 404
																		// 500

			if (null != response) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.getMessage();
		}

		return result;
	}

	/**
	 * 信任所有的证书
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static CloseableHttpClient createSSLClientDefault()

	{
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			e.getMessage();
		}
		return HttpClients.createDefault();
	}

	/**
	 * 
	 * main方法测试
	 */
	public static void main(String[] args) {
		System.out.println(getHttpsWithoutCertificate());
	}

}
