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
 * HTTPS or HTTP ��֤������
 * 
 * @author liuzhihu
 *
 */
@SuppressWarnings("deprecation")
public class HttpsWithoutCertificate {

	/**
	 * ����https�ĵ�ַ
	 */
	private static final String userCheckURL = "https://zt.wps.cn/api/migu/check_relation?userId=1100801234&serviceId=760000028200&webId=1234&area=250";

	/**
	 * ��֤����֤
	 * 
	 * @return
	 */
	public static String getHttpsWithoutCertificate() {

		// �������е�֤��
		CloseableHttpClient client = createSSLClientDefault();// jdk1.6��Ҫ���м򵥵����ò���

		// ʹ��http��get��ʽ
		HttpGet httpGet = new HttpGet();

		// �������ӳ�ʱʱ�� ��
		int timeout = 2000;
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpGet.setConfig(requestConfig);

		String result = "";

		try {
			httpGet.setURI(new URI(userCheckURL));
			HttpResponse response = client.execute(httpGet);

			@SuppressWarnings("unused")
			int statusCode = response.getStatusLine().getStatusCode();// �ӿ���Ӧ��״ֵ̬
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
	 * �������е�֤��
	 * 
	 * @return
	 * @see [�ࡢ��#��������#��Ա]
	 */
	public static CloseableHttpClient createSSLClientDefault()

	{
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// ��������
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
	 * main��������
	 */
	public static void main(String[] args) {
		System.out.println(getHttpsWithoutCertificate());
	}

}
