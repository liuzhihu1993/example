package com.example.httpswithcertificate.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * https 带验证请求工具类
 * 
 * @author liuzhihu
 *
 */
@SuppressWarnings("deprecation")
public final class HttpsWithCertificateClient {

	private HttpsWithCertificateClient() {

	}

	/**
	 * get方式
	 * 
	 * @param httpget
	 * @param cerFactoryName
	 * @param keystore
	 * @param cerPath
	 * @return
	 * @throws Exception
	 */
	public static String get(HttpGet httpget, String cerFactoryName, String keystore, String cerPath) throws Exception {

		CloseableHttpClient httpclient = null;

		try {
			FileInputStream fis = new FileInputStream(new File(cerPath));
			BufferedInputStream bis = new BufferedInputStream(fis);

			CertificateFactory cerFactory = CertificateFactory.getInstance(cerFactoryName);

			Certificate cer = cerFactory.generateCertificate(bis);

			KeyStore keyStore = KeyStore.getInstance(keystore);

			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);

			X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			socketFactory.setHostnameVerifier(hostnameVerifier);

			httpclient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

		} catch (Exception e) {
			close(httpclient);
			try {
				throw new Exception(e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		CloseableHttpResponse response = null;
		String responseBody = "";
		try {
			response = httpclient.execute(httpget);
			int status = response.getStatusLine().getStatusCode();
			if (200 != status) {
				throw new Exception("the response StatusCode is " + String.valueOf(status));
			}
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String temp = "";
			while (null != temp) {
				responseBody = responseBody + temp;
				temp = bufferedReader.readLine();
			}

			if (StringUtils.isEmpty(responseBody)) {
				throw new Exception("the responseBody is null");
			}

			return responseBody;
		} catch (Exception e) {
			throw new Exception("the  client response is error");
		}
	}

	/**
	 * post 方式
	 * 
	 * @param httpPost
	 * @param cerFactoryName
	 * @param keystore
	 * @param cerPath
	 * @return
	 * @throws Exception
	 */
	public static String post(HttpPost httpPost, String cerFactoryName, String keystore, String cerPath)
			throws Exception {
		CloseableHttpClient httpclient = null;
		try {
			FileInputStream fis = new FileInputStream(new File(cerPath));
			BufferedInputStream bis = new BufferedInputStream(fis);

			CertificateFactory cerFactory = CertificateFactory.getInstance(cerFactoryName);

			Certificate cer = cerFactory.generateCertificate(bis);

			KeyStore keyStore = KeyStore.getInstance(keystore);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);

			X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			socketFactory.setHostnameVerifier(hostnameVerifier);

			httpclient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		} catch (Exception e) {
			close(httpclient);
			throw new Exception(e);
		}

		CloseableHttpResponse response = null;
		String responseBody = "";
		try {
			// 获得响应消息码流
			response = httpclient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();

			if (null != response) {
				@SuppressWarnings("unused")
				int resultCode = response.getStatusLine().getStatusCode();
			}

			if (200 != status) {
				throw new Exception("AEPclient response StatusCode is " + String.valueOf(status));
			}
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String temp = "";
			while (null != temp) {
				responseBody = responseBody + temp;
				temp = bufferedReader.readLine();
			}

			if (StringUtils.isEmpty(responseBody)) {
				throw new Exception("AEP responseBody is null");
			}

			return responseBody;
		} catch (Exception e) {
			throw new Exception("AEP client response is error");
		} finally {

			close(httpclient);

			httpPost.releaseConnection();
			(httpclient.getConnectionManager()).shutdown();
		}
	}

	/**
	 * 关闭流
	 * 
	 * @param httpclient
	 * @throws Exception
	 */
	private static void close(CloseableHttpClient httpclient) {
		if (null != httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				try {
					throw new Exception(e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
